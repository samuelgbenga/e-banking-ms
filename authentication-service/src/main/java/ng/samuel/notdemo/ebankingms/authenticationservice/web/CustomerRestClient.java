package ng.samuel.notdemo.ebankingms.authenticationservice.web;


import feign.FeignException;
import ng.samuel.notdemo.ebankingms.authenticationservice.configuration.FeignConfig;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CUSTOMER-SERVICE", configuration = FeignConfig.class )
public interface CustomerRestClient {

    @PostMapping("/customers/create")
    boolean createCustomer(@RequestBody CustomerDto request);
}
