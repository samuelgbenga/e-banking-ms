package ng.samuel.notdemo.ebankingms.accountservice.command.aggregate;



import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.accountservice.command.commands.*;
import ng.samuel.notdemo.ebankingms.accountservice.command.exception.AccountNotActivatedException;
import ng.samuel.notdemo.ebankingms.accountservice.command.exception.AmountNotSufficientException;
import ng.samuel.notdemo.ebankingms.accountservice.command.exception.BalanceNotSufficientException;
import ng.samuel.notdemo.ebankingms.accountservice.command.exception.NotAuthorizedException;
import ng.samuel.notdemo.ebankingms.accountservice.command.utils.factory.EventFactory;
import ng.samuel.notdemo.ebankingms.accountservice.common.events.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Aggregate
@Getter
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private String email;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;

    public AccountAggregate() {
        super();
    }

    // create account command
    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {

        AccountCreatedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);
    }

    // create account event
    // apply is included here to automatically activate the user account upon creation
    @EventSourcingHandler
    public void on(@NotNull AccountCreatedEvent event) {
        log.info("AccountCreatedEvent handled");

        // Capture authentication before processing the event
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        this.accountId = event.getId();
        this.status = event.getStatus();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.customerId = event.getCustomerId();
        this.email = event.getEmail();
        this.createdBy = event.getEventBy();
        this.createdDate = event.getEventDate();
        AccountActivatedEvent accountActivatedEvent = EventFactory.create(this.accountId, this.createdDate, this.createdBy, AccountStatus.ACTIVATED);

        // Restore authentication after event processing
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AggregateLifecycle.apply(accountActivatedEvent);
    }

    // activate account command
    @CommandHandler
    public void handle(ActivateAccountCommand command) {
        log.info("AccountActivatedEvent command");
        AccountActivatedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);
    }

    // activate account event
    @EventSourcingHandler
    public void on(@NotNull AccountActivatedEvent event) {

        // Capture authentication before processing the event
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("AccountActivatedEvent handled");
        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.status = event.getStatus();

        // Restore authentication after event processing
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // suspend account command
    @CommandHandler
    public void handle(SuspendAccountCommand command) {
        log.info("SuspendAccountCommand handled");
        AccountSuspendedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);
    }

    // suspend account event
    @EventSourcingHandler
    public void on(@NotNull AccountSuspendedEvent event) {
        log.info("AccountSuspendedEvent handled");
        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.status = event.getStatus();
    }

    // credit account command
    @CommandHandler
    public void handle(@NotNull CreditAccountCommand command) {
        log.info("CreditAccountCommand handled");
        if(command.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountNotSufficientException(" amount not sufficient => " + command.getAmount());
        }else if (!this.status.equals(AccountStatus.ACTIVATED)) {
            throw new AccountNotActivatedException("Account not activated => " + this.status);
        }else{
            AccountCreditedEvent event = EventFactory.create(command);
            AggregateLifecycle.apply(event);
        }
    }

    //credit account event
    @EventSourcingHandler
    public void on(@NotNull AccountCreditedEvent event) {
        log.info("AccountCreditedEvent handled");
        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.balance = this.balance.add(event.getAmount());
    }


    // debit command
    @CommandHandler
    public void handle(@NotNull DebitAccountCommand command) {
        log.info("DebitAccountCommand handled");
        if (this.balance.compareTo(BigDecimal.ZERO) < 0 || this.balance.compareTo(command.getAmount()) < 0) {
            throw new BalanceNotSufficientException("Balance not sufficient => " + this.balance);
        } else if (!this.status.equals(AccountStatus.ACTIVATED)) {
            throw new AccountNotActivatedException("Account not activated => " + this.status);
        } else {
            AccountDebitedEvent event = EventFactory.create(command);
            AggregateLifecycle.apply(event);
        }
    }

    // debit event
    @EventSourcingHandler
    public void on(@NotNull AccountDebitedEvent event) {
        log.info("# AccountDebitedEvent handled");
        this.accountId = event.getId();
        this.balance = this.balance.subtract(event.getAmount());
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
    }


    // delete account command
    @CommandHandler
    public void handle(@NotNull DeleteAccountCommand command) {
        log.info("DeleteAccountCommand handled");
        if (this.balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new NotAuthorizedException("Not authorized : The balance must be 0 in order to delete the account.=> " + this.balance);
        }
        AccountDeletedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);


    }

    // delete account event
    @EventSourcingHandler
    public void on(@NotNull AccountDeletedEvent event) {

        // Capture authentication before processing the event
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("AccountDeletedEvent handled");
        log.info("AccountDeletedEvent delete {}", authentication.toString());

        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.status = event.getStatus();
        this.email = null;
        this.customerId = null;

        // Restore authentication after event processing
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
