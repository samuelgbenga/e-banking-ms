package ng.samuel.notdemo.ebankingms.authenticationservice.web;

import ng.samuel.notdemo.ebankingms.authenticationservice.dto.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {

    @PostMapping("/bank/notifications/send")
    void sendNotification(@RequestBody NotificationRequestDTO request);
}
