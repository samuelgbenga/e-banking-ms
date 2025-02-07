package ng.samuel.notdemo.ebankingms.accountservice.queries.web;

import jakarta.validation.Valid;
import ng.samuel.notdemo.ebankingms.accountservice.queries.dto.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("NOTIFICATION-SERVICE")
public interface NotificationRestClient {

    @PostMapping("/mailing/send")
    void sendNotification(@RequestBody @Valid NotificationRequestDTO notification);
}
