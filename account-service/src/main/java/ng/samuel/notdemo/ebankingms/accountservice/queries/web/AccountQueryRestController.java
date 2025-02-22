package ng.samuel.notdemo.ebankingms.accountservice.queries.web;

import ng.samuel.notdemo.ebankingms.accountservice.queries.dto.AccountResponseDTO;
import ng.samuel.notdemo.ebankingms.accountservice.queries.dto.OperationResponseDTO;
import ng.samuel.notdemo.ebankingms.accountservice.queries.exception.AccountNotFoundException;
import ng.samuel.notdemo.ebankingms.accountservice.queries.exception.OperationNotFoundException;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetAccountByCustomerIdQuery;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetAccountByIdQuery;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetOperationByAccountId;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetOperationByIdQuery;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/queries")
public class AccountQueryRestController {

    private final QueryGateway queryGateway;

    public AccountQueryRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get-account/{id}")
    public AccountResponseDTO getAccountById(@PathVariable String id) {
        GetAccountByIdQuery query = new GetAccountByIdQuery(id);
        ResponseType<AccountResponseDTO> responseType = ResponseTypes.instanceOf(AccountResponseDTO.class);
        AccountResponseDTO response = queryGateway.query(query, responseType).join();
        if(response == null) {
            throw new AccountNotFoundException(String.format("Account with id %s not found", id));
        }
        return response;
    }

    @GetMapping("/find-account/{customerId}")
    public AccountResponseDTO getAccountByCustomerId(@PathVariable String customerId) {
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery(customerId);
        ResponseType<AccountResponseDTO> responseType = ResponseTypes.instanceOf(AccountResponseDTO.class);
        AccountResponseDTO response = queryGateway.query(query, responseType).join();
        if(response == null) {
            throw new AccountNotFoundException(String.format("Account with customer's id %s not found", customerId));
        }
        return response;
    }

    @GetMapping("/get-operation/{id}")
    public OperationResponseDTO getOperationById(@PathVariable String id) {
        GetOperationByIdQuery query = new GetOperationByIdQuery(id);
        ResponseType<OperationResponseDTO> responseType = ResponseTypes.instanceOf(OperationResponseDTO.class);
        OperationResponseDTO response = queryGateway.query(query, responseType).join();
        if(response == null) {
            throw new OperationNotFoundException(String.format("Operation with id %s not found", id));
        }
        return response;
    }

    @GetMapping("/all-operations")
    public List<OperationResponseDTO> getOperationById(@RequestParam(name = "accountId") String accountId,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "9") int size) {
        GetOperationByAccountId query = new GetOperationByAccountId(accountId, page, size);
        ResponseType<List<OperationResponseDTO>> responseType = ResponseTypes.multipleInstancesOf(OperationResponseDTO.class);
        return queryGateway.query(query, responseType).join();
    }


}
