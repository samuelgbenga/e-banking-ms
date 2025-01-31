package ng.samuel.notdemo.ebankingms.authenticationservice.web;


import ng.samuel.notdemo.ebankingms.authenticationservice.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient {

    @PostMapping("/customer/create")
    boolean createCustomer(@RequestBody CustomerDto request);
}
