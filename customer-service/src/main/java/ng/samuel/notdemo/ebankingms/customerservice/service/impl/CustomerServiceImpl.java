package ng.samuel.notdemo.ebankingms.customerservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerExistResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerPageResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerRequestDTO;
import ng.samuel.notdemo.ebankingms.customerservice.dto.CustomerResponseDTO;
import ng.samuel.notdemo.ebankingms.customerservice.entity.Customer;
import ng.samuel.notdemo.ebankingms.customerservice.exception.CustomerNotFoundException;
import ng.samuel.notdemo.ebankingms.customerservice.exception.FieldValidationException;
import ng.samuel.notdemo.ebankingms.customerservice.repository.CustomerRepository;
import ng.samuel.notdemo.ebankingms.customerservice.service.CustomerService;
import ng.samuel.notdemo.ebankingms.customerservice.utils.mapper.Mapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public CustomerResponseDTO getCustomerById(String id) {
        log.info("In getCustomerById()");
        Customer customer = findCustomerById(id);
        log.info("Customer with id '{}' found",id);
        return Mapper.fromCustomerDto(customer);
    }

    @Override
    public CustomerResponseDTO getCustomerByCin(String cin) {
        log.info("In getCustomerByCin()");
        Customer customer = customerRepository.findByCin(cin)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with cin '%s' not found", cin)));
        log.info("Customer with cin '{}' found",cin);
        return Mapper.fromCustomerDto(customer);
    }

    @Override
    public CustomerPageResponseDTO getAllCustomers(int page, int size) {
        log.info("In getAllCustomers()");
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        log.info("{} customers found",customers.getTotalElements());
        return Mapper.fromPageOfCustomers(customers);
    }

    @Override
    public CustomerPageResponseDTO searchCustomers(String keyword, int page, int size) {
        log.info("In searchCustomers()");
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.search("%"+keyword+"%", pageable);
        log.info("{} customers found.",customers.getTotalElements());
        return Mapper.fromPageOfCustomers(customers);
    }

    @Transactional
    @Override
    public boolean createCustomer(@NotNull CustomerRequestDTO dto) {
        log.info("In createCustomer()");
        validationBeforeCreateCustomer(dto.getEmail(), dto.getCin());
        Customer customer = Mapper.fromCustomerDto(dto);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created with id {}",savedCustomer.getId());
        return customerRepository.existsById(savedCustomer.getId());
    }


    @Transactional
    @Override
    public CustomerResponseDTO updateCustomer(String id, @NotNull CustomerRequestDTO dto) {
        log.info("In updateCustomer()");
        Customer customer = findCustomerById(id);
        validationBeforeUpdateUser(customer.getEmail(), customer.getCin(), dto.getEmail(), dto.getCin());
        Customer customerToUpdate = Mapper.updateCustomerItems(customer, dto);
        Customer customerUpdated = customerRepository.save(customerToUpdate);
        log.info("Customer with id {} updated", customerUpdated.getId());
        return Mapper.fromCustomerDto(customerUpdated);
    }

    @Transactional
    @Override
    public void deleteCustomerById(String id) {
        log.info("In deleteCustomerById()");
        customerRepository.deleteById(id);
        log.info("Customer with id '{}' deleted",id);
    }

    @Override
    public CustomerExistResponseDTO customerExist(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow( () -> new CustomerNotFoundException(String.format("Customer with id %s not found", id)));
        log.info("Customer with {} id exist.", id);
        return new CustomerExistResponseDTO( customer.getId(), customer.getEmail());

    }

    private Customer findCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow( () -> new CustomerNotFoundException(String.format("Customer with id %s not found", id)));
    }

    private void validationBeforeCreateCustomer(String email, String cin) {
        List<String> messages = new ArrayList<>();
        if(customerRepository.existsByCin(cin)) {
            messages.add(String.format("Customer with cin '%s' already exist", cin));
        }
        if(customerRepository.existsByEmail(email)) {
            messages.add(String.format("Customer with email '%s' already exist", email));
        }
        if(!messages.isEmpty()) {
            throw new FieldValidationException("Invalid data", messages);
        }
    }

    private void validationBeforeUpdateUser(@NotNull String oldEmail, String oldCin, String newEmail, String newCin) {
        List<String> messages = new ArrayList<>();
        if(!oldEmail.equals(newEmail) && customerRepository.existsByEmail(newEmail)) {
            messages.add(String.format("Customer with email '%s' already exist", newEmail));
        }
        if(!oldCin.equals(newCin) && customerRepository.existsByCin(newCin)) {
            messages.add(String.format("Customer with CIN '%s' already exist", newCin));
        }
        if(!messages.isEmpty()) {
            throw new FieldValidationException("Invalid data", messages);
        }
    }
}
