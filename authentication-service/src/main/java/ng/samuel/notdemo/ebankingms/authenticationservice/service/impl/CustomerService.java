package ng.samuel.notdemo.ebankingms.authenticationservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.CustomerDto;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.UserRequestDTO;
import ng.samuel.notdemo.ebankingms.authenticationservice.utils.mapper.Mappers;
import ng.samuel.notdemo.ebankingms.authenticationservice.web.CustomerRestClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRestClient customerRestClient;
    public CustomerService(CustomerRestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }

    public boolean createCustomer(UserRequestDTO userRequestDTO){
        CustomerDto customerDto = Mappers.fromUserRequestDto(userRequestDTO);
        return customerRestClient.createCustomer(customerDto);
    }
}
