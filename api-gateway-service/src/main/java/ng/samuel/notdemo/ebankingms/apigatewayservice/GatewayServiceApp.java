package ng.samuel.notdemo.ebankingms.apigatewayservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

/**
 * Hello world!
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class GatewayServiceApp
{
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApp.class, args);
    }


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/customer-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://CUSTOMER-SERVICE"))
                .route(r -> r.path("/authentication-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://AUTHENTICATION-SERVICE"))
                .route(r -> r.path("/notification-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://NOTIFICATION-SERVICE"))
                .route(r -> r.path("/account-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://ACCOUNT-SERVICE"))
                .build();
    }

}
