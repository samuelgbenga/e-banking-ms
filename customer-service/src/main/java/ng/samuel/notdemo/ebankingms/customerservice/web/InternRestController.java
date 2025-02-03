package ng.samuel.notdemo.ebankingms.customerservice.web;


import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerExistResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/intern")
public class InternRestController {

    private final CustomerService customerService;

    public InternRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/verify/{id}")
    public CustomerExistResponseDTO getCustomerById(@PathVariable String id){
        return customerService.customerExist(id);
    }

}
