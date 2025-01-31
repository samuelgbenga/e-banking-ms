package ng.samuel.notdemo.ebankingms.customerservice.service;


import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerPageResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerRequestDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerResponseDTO;

public interface CustomerService {

    CustomerResponseDTO getCustomerById(String id);
    CustomerResponseDTO getCustomerByCin(String cin);
    CustomerPageResponseDTO getAllCustomers(int page, int size);
    CustomerPageResponseDTO searchCustomers(String keyword, int page, int size);
    boolean createCustomer(CustomerRequestDTO dto);
    CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO dto);
    void deleteCustomerById(String id);
}
