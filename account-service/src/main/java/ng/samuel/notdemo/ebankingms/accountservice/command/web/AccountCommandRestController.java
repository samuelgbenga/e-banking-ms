package ng.samuel.notdemo.ebankingms.accountservice.command.web;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.accountservice.command.commands.*;
import ng.samuel.notdemo.ebankingms.accountservice.command.dto.*;
import ng.samuel.notdemo.ebankingms.accountservice.command.exception.CustomerNotFoundException;
import ng.samuel.notdemo.ebankingms.accountservice.command.utils.factory.CommandFactory;
import ng.samuel.notdemo.ebankingms.accountservice.command.utils.generator.IdGenerator;
import ng.samuel.notdemo.ebankingms.accountservice.command.utils.proxy.TransferProxy;
import ng.samuel.notdemo.ebankingms.accountservice.common.security.SecurityInformation;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.bind.annotation.*;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RestController
@RequestMapping("/accounts/commands")
public class AccountCommandRestController {


    private final CommandGateway commandGateway;
    private final CustomerRestClient customerRestClient;
    private final IdGenerator idGenerator;
    private final SecurityInformation securityInformation;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;

    public AccountCommandRestController(CommandGateway commandGateway, CustomerRestClient customerRestClient, IdGenerator idGenerator, SecurityInformation securityInformation) {
        this.commandGateway = commandGateway;
        this.customerRestClient = customerRestClient;
        this.idGenerator = idGenerator;
        this.securityInformation = securityInformation;
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Valid AccountRequestDTO dto) {

        CustomerExistResponseDTO customer = checkCustomerExist(dto.customerId());
        if(customer == null){
            throw new CustomerNotFoundException(String.format("Customer with id %s not found", dto.customerId()));
        }
        CreateAccountCommand command = createCommand(customer.id(), customer.email(), dto.currency());

        CompletableFuture<String> result = commandGateway.send(command);

        return ResponseEntity.ok(result.join());

    }

//    @PostMapping("/create")
//    public CompletableFuture<Object> create(@RequestBody @Valid AccountRequestDTO dto) {
//
//
//        CustomerExistResponseDTO customer = checkCustomerExist(dto.customerId());
//        if(customer == null){
//            throw new CustomerNotFoundException(String.format("Customer with id %s not found", dto.customerId()));
//        }
//        CreateAccountCommand command = createCommand(customer.id(), customer.email(), dto.currency());
//
//        // Capture authentication before processing the event
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Use Spring Security context-aware callable
//        Object result =  CompletableFuture.supplyAsync(() -> {
//            try {
//                // Restore authentication inside the async execution
//                SecurityContext context = SecurityContextHolder.createEmptyContext();
//                context.setAuthentication(authentication);
//                SecurityContextHolder.setContext(context);
//
//                // Send command and get result
//                return commandGateway.send(command).join();
//            } finally {
//                // Clear security context to prevent memory leaks
//                SecurityContextHolder.clearContext();
//            }
//        });
//
//        return (CompletableFuture<Object>) result;
//    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody @Valid UpdateStatusRequestDTO dto) {
        if(dto.status().equals(AccountStatus.ACTIVATED) || dto.status().equals(AccountStatus.CREATED)){
            ActivateAccountCommand command = CommandFactory.createActivateAccountCommand(dto, securityInformation.getUsername());
            CompletableFuture<String> result =  commandGateway.send(command);
           return ResponseEntity.ok(result.join());
        } else if (dto.status().equals(AccountStatus.SUSPENDED)) {
            SuspendAccountCommand command = CommandFactory.createSuspendAccountCommand(dto, securityInformation.getUsername());
            CompletableFuture<String> result =  commandGateway.send(command);
            return ResponseEntity.ok(result.join());
        }else{
            throw new IllegalArgumentException("Unsupported status " + dto.status());
        }
    }

    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestBody @Valid OperationRequestDTO dto) {
        CreditAccountCommand command = CommandFactory.createCreditAccountCommand(dto, securityInformation.getUsername());
        CompletableFuture<String> result = commandGateway.send(command);
        return ResponseEntity.ok(result.join());
    }

    @PostMapping("/debit")
    public ResponseEntity<String> debit(@RequestBody @Valid OperationRequestDTO dto) {
        DebitAccountCommand command = CommandFactory.createDebitAccountCommand(dto, securityInformation.getUsername());
        CompletableFuture<String> result =  commandGateway.send(command);
        return ResponseEntity.ok(result.join());
    }

    @PostMapping("/transfer")
    public List<CompletableFuture<String>> transfer(@RequestBody @Valid TransferRequestDTO dto){
        TransferProxy proxy = new TransferProxy();
        return proxy.transfer(dto, commandGateway, securityInformation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        DeleteAccountCommand command = CommandFactory.createDeleteAccountCommand(id, securityInformation.getUsername());
        CompletableFuture<String> result = commandGateway.send(command);
        return ResponseEntity.ok(result.join());
    }


    @Nullable
    private CustomerExistResponseDTO checkCustomerExist(String customerId) {
        try{
            return customerRestClient.checkCustomerExist(customerId);
        }catch(Exception e) {
            return null;
        }
    }

    @NotNull
    @Contract("_, _, _ -> new")
    private CreateAccountCommand createCommand(String customerId, String email, Currency currency) {
        return new CreateAccountCommand(idGenerator.autoGenerateId(), LocalDateTime.now(),
                securityInformation.getUsername(), AccountStatus.CREATED, BigDecimal.ZERO, currency, customerId, email
        );
    }

    @GetMapping
    public String get() {
       return "samuel the man";
    }


}
