package ng.samuel.notdemo.ebankingms.accountservice.command.web;


import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.*;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/commands")
public class AccountCommandRestController {


    private final CommandGateway commandGateway;
    private final CustomerRestClient customerRestClient;
    private final IdGenerator idGenerator;
    private final SecurityInformation securityInformation;

    public AccountCommandRestController(CommandGateway commandGateway, CustomerRestClient customerRestClient, IdGenerator idGenerator, SecurityInformation securityInformation) {
        this.commandGateway = commandGateway;
        this.customerRestClient = customerRestClient;
        this.idGenerator = idGenerator;
        this.securityInformation = securityInformation;
    }

    @PostMapping("/create")
    public CompletableFuture<String> create(@RequestBody @Valid AccountRequestDTO dto) {
        CustomerExistResponseDTO customer = checkCustomerExist(dto.customerId());
        if(customer == null){
            throw new CustomerNotFoundException(String.format("Customer with id %s not found", dto.customerId()));
        }
        CreateAccountCommand command = createCommand(customer.id(), customer.email(), dto.currency());
        return commandGateway.send(command);
    }

    @PutMapping("/update")
    public CompletableFuture<String> update(@RequestBody @Valid UpdateStatusRequestDTO dto) {
        if(dto.status().equals(AccountStatus.ACTIVATED) || dto.status().equals(AccountStatus.CREATED)){
            ActivateAccountCommand command = CommandFactory.createActivateAccountCommand(dto, securityInformation.getUsername());
            return commandGateway.send(command);
        } else if (dto.status().equals(AccountStatus.SUSPENDED)) {
            SuspendAccountCommand command = CommandFactory.createSuspendAccountCommand(dto, securityInformation.getUsername());
            return commandGateway.send(command);
        }else{
            throw new IllegalArgumentException("Unsupported status " + dto.status());
        }
    }

    @PostMapping("/credit")
    public CompletableFuture<String> credit(@RequestBody @Valid OperationRequestDTO dto) {
        CreditAccountCommand command = CommandFactory.createCreditAccountCommand(dto, securityInformation.getUsername());
        return commandGateway.send(command);
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debit(@RequestBody @Valid OperationRequestDTO dto) {
        DebitAccountCommand command = CommandFactory.createDebitAccountCommand(dto, securityInformation.getUsername());
        return commandGateway.send(command);
    }

    @PostMapping("/transfer")
    public List<CompletableFuture<String>> transfer(@RequestBody @Valid TransferRequestDTO dto){
        TransferProxy proxy = new TransferProxy();
        return proxy.transfer(dto, commandGateway, securityInformation);
    }

    @DeleteMapping("/delete/{id}")
    public CompletableFuture<String> delete(@PathVariable String id) {
        DeleteAccountCommand command = CommandFactory.createDeleteAccountCommand(id, securityInformation.getUsername());
        return commandGateway.send(command);
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
