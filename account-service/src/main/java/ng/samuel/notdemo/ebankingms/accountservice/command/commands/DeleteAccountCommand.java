package ng.samuel.notdemo.ebankingms.accountservice.command.commands;

import java.time.LocalDateTime;

import lombok.Getter;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.AccountStatus;

@Getter
public class DeleteAccountCommand extends BaseCommand<String> {

    private final AccountStatus status;

    public DeleteAccountCommand(String id, LocalDateTime commandDate, String commandBy, AccountStatus status) {
        super(id, commandDate, commandBy);
        this.status = status;
    }
}
