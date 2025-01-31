package ng.samuel.notdemo.ebankingms.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Hello world!
 *
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
@EnableDiscoveryClient

public class CustomersServiceApp
{
    public static void main(String[] args) {
        SpringApplication.run(CustomersServiceApp.class, args);
    }
}
