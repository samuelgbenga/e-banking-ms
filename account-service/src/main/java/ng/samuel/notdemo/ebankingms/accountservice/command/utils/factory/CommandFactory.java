package ng.samuel.notdemo.ebankingms.accountservice.command.utils.factory;

import ng.samuel.notdemo.ebankingms.accountservice.command.commands.*;
import ng.samuel.notdemo.ebankingms.accountservice.command.dto.OperationRequestDTO;
import ng.samuel.notdemo.ebankingms.accountservice.command.dto.TransferRequestDTO;
import ng.samuel.notdemo.ebankingms.accountservice.command.dto.UpdateStatusRequestDTO;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.AccountStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.OperationType;


import java.time.LocalDateTime;

public class CommandFactory {

    private CommandFactory() {
        super();
    }

    @NotNull
    @Contract("_, _ -> new")
    public static CreditAccountCommand createCreditAccountCommand(@NotNull final OperationRequestDTO dto, final String username){
        return new CreditAccountCommand(
                dto.accountId(),
                LocalDateTime.now(),
                username,
                dto.amount(),
                OperationType.CREDIT,
                dto.description()
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static DebitAccountCommand createDebitAccountCommand(@NotNull final OperationRequestDTO dto, final String username){
        return new DebitAccountCommand(
                dto.accountId(),
                LocalDateTime.now(),
                username,
                dto.amount(),
                OperationType.DEBIT,
                dto.description()
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static ActivateAccountCommand createActivateAccountCommand(@NotNull final UpdateStatusRequestDTO dto, final String username){
        return new ActivateAccountCommand(
                dto.accountId(),
                LocalDateTime.now(),
                username,
                AccountStatus.ACTIVATED
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static SuspendAccountCommand createSuspendAccountCommand(@NotNull final UpdateStatusRequestDTO dto, final String username){
        return new SuspendAccountCommand(
                dto.accountId(),
                LocalDateTime.now(),
                username,
                AccountStatus.SUSPENDED
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static DeleteAccountCommand createDeleteAccountCommand(@NotNull final String accountId, final String username){
        return new DeleteAccountCommand(accountId,LocalDateTime.now(), username, AccountStatus.DELETED);
    }


    @NotNull
    @Contract("_, _ -> new")
    public static DebitAccountCommand createDebitAccountCommand(@NotNull final TransferRequestDTO dto, final String username){
        return new DebitAccountCommand(
                dto.accountIdFrom(),
                LocalDateTime.now(),
                username,
                dto.amount(),
                OperationType.DEBIT,
                dto.description()
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static CreditAccountCommand createCreditAccountCommand(@NotNull final TransferRequestDTO dto, final String username){
        return new CreditAccountCommand(
                dto.accountIdTo(),
                LocalDateTime.now(),
                username,
                dto.amount(),
                OperationType.CREDIT,
                dto.description()
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static CreditAccountCommand createCreditAccountCommandReverse(@NotNull final TransferRequestDTO dto, final String username){
        return new CreditAccountCommand(
                dto.accountIdFrom(),
                LocalDateTime.now(),
                username,
                dto.amount(),
                OperationType.CREDIT,
                dto.description()
        );
    }

}
