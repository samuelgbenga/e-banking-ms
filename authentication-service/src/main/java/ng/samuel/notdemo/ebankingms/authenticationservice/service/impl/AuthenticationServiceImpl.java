package ng.samuel.notdemo.ebankingms.authenticationservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.authenticationservice.configuration.ApplicationProperties;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.LoginRequestDTO;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.LoginResponseDTO;
import ng.samuel.notdemo.ebankingms.authenticationservice.entity.Role;
import ng.samuel.notdemo.ebankingms.authenticationservice.entity.User;
import ng.samuel.notdemo.ebankingms.authenticationservice.exception.UserNotAuthenticatedException;
import ng.samuel.notdemo.ebankingms.authenticationservice.exception.UserNotEnabledException;
import ng.samuel.notdemo.ebankingms.authenticationservice.exception.UserNotFoundException;
import ng.samuel.notdemo.ebankingms.authenticationservice.repository.UserRepository;
import ng.samuel.notdemo.ebankingms.authenticationservice.service.AuthenticationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ApplicationProperties properties;
    private final NotificationService notificationService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, ApplicationProperties properties, NotificationService notificationService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.properties = properties;
        this.notificationService = notificationService;
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        log.info("In authenticate()");
        Authentication authenticationRequest =  new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        if (authenticationResponse.isAuthenticated()) {
            User user = getUserByUsername(dto.username());
            if(!user.isEnabled()){
                throw new UserNotEnabledException(String.format("User %s is not enabled", dto.username()));
            }
            log.info("Authentication successful");
            final String jwt = generateToken(user);
            updateUserLastLoginDate(user);
            return new LoginResponseDTO(jwt, user.isPasswordNeedToBeModified());
        }else{
            log.error("Authentication failed for user: {}", dto.username());
            throw new UserNotAuthenticatedException("User not authenticated");
        }
    }

    private void updateUserLastLoginDate(@NotNull User user) {
        user.setLastLogin(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        sendNotificationToUser(updatedUser);
    }

    private void sendNotificationToUser(@NotNull User updatedUser) {
        final String subject = "Authentication Successful";
        final String body = String.format("You have just been successfully authenticated at %s", updatedUser.getLastLogin().toString());
        notificationService.send(updatedUser.getEmail(), subject, body);
    }

    // implements  create token
    private String generateToken(@NotNull User user) {
        List<String> roles = getListOfNamesOfRoles(user.getRoles());
        return createJwt(user, roles);
    }

// get user by there username
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User not found with username or email: {}", username);
            return new UserNotFoundException("User not found");
        });
    }

    // get the list of the names of rolse
    private List<String> getListOfNamesOfRoles(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        return roles.stream().map(Role::getName).toList();
    }

    // creates the jwt token with the claims and subject
    private String createJwt(@NotNull User user, @NotNull List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(properties.getJwtSecret());
        Date expiration = new Date(System.currentTimeMillis() + properties.getJwtExpiration());
        return JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withClaim("fullName", user.getFullName())
                .withClaim("id", user.getId())
                .withExpiresAt(expiration)
                .sign(algorithm);
    }
}
