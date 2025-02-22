package ng.samuel.notdemo.ebankingms.accountservice.command.commands;

import lombok.Getter;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CreditAccountCommand extends BaseCommand<String> {
    private final BigDecimal amount;
    private final OperationType type;
    private final String description;

    public CreditAccountCommand(String id, LocalDateTime commandDate, String commandBy, BigDecimal amount, OperationType type, String description) {
        super(id, commandDate, commandBy);
        this.amount = amount;
        this.type = type;
        this.description = description;
    }
}
