package ng.samuel.notdemo.ebankingms.accountservice.queries.dto;

import lombok.*;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.AccountStatus;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AccountResponseDTO {
    private String id;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
}
