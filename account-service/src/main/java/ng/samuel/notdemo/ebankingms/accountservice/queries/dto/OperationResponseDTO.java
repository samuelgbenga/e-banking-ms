package ng.samuel.notdemo.ebankingms.accountservice.queries.dto;

import lombok.*;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OperationResponseDTO {
    private String id;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private OperationType type;
    private String description;
    private String createdBy;
    private String accountId;
}
