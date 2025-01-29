package ng.samuel.notdemo.ebankingms.accountservice.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ng.samuel.notdemo.ebankingms.accountservice.common.enums.Currency;

public record AccountRequestDTO(
        @NotBlank(message="field 'customerId' is mandatory: it can not be blank")
        String customerId,

        @NotNull(message="field 'currency' is mandatory: it can not be null")
        Currency currency) {
}
