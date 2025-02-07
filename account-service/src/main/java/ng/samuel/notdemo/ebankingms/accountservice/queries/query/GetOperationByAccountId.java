package ng.samuel.notdemo.ebankingms.accountservice.queries.query;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetOperationByAccountId {
    private String accountId;
    private int page;
    private int size;
}
