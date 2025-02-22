package ng.samuel.notdemo.ebankingms.customerservice.web;

import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerPageResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerRequestDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/get/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable String id){
        return customerService.getCustomerById(id);
    }

    @GetMapping("/find/{cin}")
    public CustomerResponseDTO getCustomerByCin(@PathVariable String cin){
        return customerService.getCustomerByCin(cin);
    }

    @GetMapping("/list")
    public CustomerPageResponseDTO getAllCustomers(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "9") int size) {
        return customerService.getAllCustomers(page, size);
    }

    @GetMapping("/search")
    public CustomerPageResponseDTO searchCustomers(@RequestParam(defaultValue = "") String keyword,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "9") int size) {
        return customerService.searchCustomers(keyword, page, size);
    }


    @PostMapping("/create")
    public boolean createCustomer(@RequestBody CustomerRequestDTO dto){
        return customerService.createCustomer(dto);
    }

    @PutMapping("/update/{id}")
    public CustomerResponseDTO updateCustomer(@PathVariable String id, @RequestBody CustomerRequestDTO dto){
        return customerService.updateCustomer(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomerById(@PathVariable String id){
        customerService.deleteCustomerById(id);
    }
}
