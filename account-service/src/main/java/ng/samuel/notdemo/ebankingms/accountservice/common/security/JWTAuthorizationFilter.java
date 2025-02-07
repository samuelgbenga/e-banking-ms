package ng.samuel.notdemo.ebankingms.accountservice.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.accountservice.common.properties.ApplicationProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    private static final String ROLES = "roles";

    private final ApplicationProperties properties;

    public JWTAuthorizationFilter(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            DecodedJWT jwt = validateToken(token);
            if (jwt == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
                return;
            }

            if (!isTokenExpired(jwt)) {
                setUpSpringAuthentication(jwt, request);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired");
                return;
            }
        } catch (JWTVerificationException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private DecodedJWT validateToken(String token) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(properties.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            log.info("Token validated successfully. Subject: {}", jwt.getSubject());
            return jwt;
        } catch (JWTVerificationException ex) {
            log.error("JWT verification failed: {}", ex.getMessage());
            throw ex;
        }
    }

    private boolean isTokenExpired(@NotNull DecodedJWT jwt) {
        Date expiration = jwt.getExpiresAt();
        log.info("Token Expiration Time: {}, Current Time: {}", expiration, new Date());
        return expiration.before(new Date());
    }

    private void setUpSpringAuthentication(@NotNull DecodedJWT jwt, HttpServletRequest request) {
        String username = jwt.getSubject();
        List<String> roles = jwt.getClaims().get(ROLES).asList(String.class);

        if (username != null && roles != null) {
            List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

            UserDetails userDetails = new User(username, "", authorities);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, jwt, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("User '{}' authenticated with roles: {}", username, roles);
            log.info("SecurityContext: {}", SecurityContextHolder.getContext().getAuthentication());
        } else {
            log.warn("JWT did not contain username or roles, authentication not set.");
        }
    }
}