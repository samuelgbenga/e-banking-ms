package ng.samuel.notdemo.ebankingms.accountservice.command.utils.factory;

import ng.samuel.notdemo.ebankingms.accountservice.command.commands.*;
import ng.samuel.notdemo.ebankingms.accountservice.common.events.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import ng.samuel.notdemo.ebankingms.accountservice.common.enums.AccountStatus;


import java.time.LocalDateTime;

public class EventFactory {

    private EventFactory() {
        super();
    }


    @NotNull
    @Contract("_ -> new")
    public static AccountCreatedEvent create(@NotNull final CreateAccountCommand command) {
        return new AccountCreatedEvent(
                command.getId(),
                command.getCommandDate(),
                command.getCommandBy(),
                command.getStatus(),
                command.getBalance(),
                command.getCurrency(),
                command.getCustomerId(),
                command.getEmail()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountActivatedEvent create(@NotNull final ActivateAccountCommand command) {
        return new AccountActivatedEvent(
                command.getId(),
                command.getCommandDate(),
                command.getCommandBy(),
                command.getStatus()
        );
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static AccountActivatedEvent create(final String accountId, final LocalDateTime commandDate, final String commandBy, final AccountStatus status) {
        return new AccountActivatedEvent(
                accountId,
                commandDate,
                commandBy,
                status
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountSuspendedEvent create(@NotNull final SuspendAccountCommand command) {
        return new AccountSuspendedEvent(
                command.getId(),
                command.getCommandDate(),
                command.getCommandBy(),
                command.getStatus()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountCreditedEvent create(@NotNull final CreditAccountCommand command){
        return new AccountCreditedEvent(
                command.getId(),
                command.getCommandDate(),
                command.getCommandBy(),
                command.getAmount(),
                command.getType(),
                command.getDescription()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountDebitedEvent create(@NotNull final DebitAccountCommand command){
        return new AccountDebitedEvent(
                command.getId(),
                command.getCommandDate(),
                command.getCommandBy(),
                command.getAmount(),
                command.getType(),
                command.getDescription()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountDeletedEvent create(@NotNull final DeleteAccountCommand command) {
        return new AccountDeletedEvent(
                command.getId(),
                command.getCommandDate(),
                command.getCommandBy()
        );
    }


}
