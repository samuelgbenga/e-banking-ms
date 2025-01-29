package ng.samuel.notdemo.ebankingms.accountservice.command.commands;


import lombok.Getter;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.AccountStatus;

import java.time.LocalDateTime;

@Getter
public class SuspendAccountCommand extends BaseCommand<String>{
    private final AccountStatus status;

    public SuspendAccountCommand(String id, LocalDateTime commandDate, String commandBy, AccountStatus status) {
        super(id, commandDate, commandBy);
        this.status = status;
    }
}
