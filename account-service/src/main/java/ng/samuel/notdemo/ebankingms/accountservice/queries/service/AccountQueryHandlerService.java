package ng.samuel.notdemo.ebankingms.accountservice.queries.service;

import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.accountservice.queries.dto.AccountResponseDTO;
import ng.samuel.notdemo.ebankingms.accountservice.queries.dto.OperationResponseDTO;
import ng.samuel.notdemo.ebankingms.accountservice.queries.entity.Account;
import ng.samuel.notdemo.ebankingms.accountservice.queries.entity.Operation;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetAccountByCustomerIdQuery;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetAccountByIdQuery;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetOperationByAccountId;
import ng.samuel.notdemo.ebankingms.accountservice.queries.query.GetOperationByIdQuery;
import ng.samuel.notdemo.ebankingms.accountservice.queries.repository.AccountRepository;
import ng.samuel.notdemo.ebankingms.accountservice.queries.repository.OperationRepository;
import ng.samuel.notdemo.ebankingms.accountservice.queries.utils.mapper.Mapper;
import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountQueryHandlerService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountQueryHandlerService(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @QueryHandler
    public AccountResponseDTO handle(@NotNull GetAccountByIdQuery query){
        log.info("GetAccountByIdQuery handled");
        Account account =  accountRepository.findById(query.getAccountId()).orElse(null);
        if(account == null){
            log.warn("Account not found for id {}", query.getAccountId());
            return null;
        }
        log.info("Account found with id {}", query.getAccountId());
        return Mapper.fromAccount(account);
    }

    @QueryHandler
    public AccountResponseDTO handle(@NotNull GetAccountByCustomerIdQuery query){
        log.info("GetAccountByCustomerId handled");
        Account account =  accountRepository.findByCustomerId(query.getCustomerId()).orElse(null);
        if(account == null){
            log.warn("Account with customer's id {} not found", query.getCustomerId());
            return null;
        }
        log.info("Account with customer's id {} found", query.getCustomerId());
        return Mapper.fromAccount(account);
    }

    @QueryHandler
    public OperationResponseDTO handle(@NotNull GetOperationByIdQuery query){
        log.info("GetOperationByIdQuery handled");
        Operation operation = operationRepository.findById(query.getOperationId()).orElse(null);
        if(operation == null){
            log.warn("Operation not found for id {}", query.getOperationId());
            return null;
        }
        log.info("Operation found with id {}", query.getOperationId());
        return Mapper.fromOperation(operation);
    }

    @QueryHandler
    public List<OperationResponseDTO> handle(@NotNull GetOperationByAccountId query){
        log.info("GetOperationByAccountId handled");
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        Page<Operation> operationPage = operationRepository.findByAccountId(query.getAccountId(), pageable);
        log.info("Operations found for account {}", query.getAccountId());
        return Mapper.fromOperations(operationPage.getContent());
    }
}
