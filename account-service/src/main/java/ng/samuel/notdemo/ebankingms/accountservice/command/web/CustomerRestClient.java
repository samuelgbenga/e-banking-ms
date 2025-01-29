package ng.samuel.notdemo.ebankingms.accountservice.command.web;

import ng.samuel.notdemo.ebankingms.accountservice.command.dto.CustomerExistResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient {

    @GetMapping("/intern/verify/{id}")
    CustomerExistResponseDTO checkCustomerExist(@PathVariable String id);
}
