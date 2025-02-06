package ng.samuel.notdemo.ebankingms.authenticationservice.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get JWT token from the SecurityContext (assuming the JWT is stored in the Authentication object)
        if (authentication != null && authentication.getCredentials() instanceof String) {
            String token = (String) authentication.getCredentials();
            // Add Authorization header with the Bearer token to every request
           // log.info("this is the token: {}", token);
            requestTemplate.header("Authorization", "Bearer " + token);
        } else {
            // Optional: Log a warning if the token is not available
            log.warn("No JWT token found in the SecurityContext.");
        }


    }
}
