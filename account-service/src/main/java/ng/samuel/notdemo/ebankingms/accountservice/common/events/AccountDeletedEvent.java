package ng.samuel.notdemo.ebankingms.accountservice.common.events;

import lombok.Getter;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.AccountStatus;

import java.time.LocalDateTime;

@Getter
public class AccountDeletedEvent extends BaseEvent<String> {

    private final AccountStatus status;

    public AccountDeletedEvent(String id, LocalDateTime eventDate, String eventBy, AccountStatus status) {
        super(id, eventDate, eventBy);
        this.status = status;
    }
}
