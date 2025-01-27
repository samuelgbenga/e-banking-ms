package ng.samuel.notdemo.ebankingms.authenticationservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.NotificationRequestDTO;
import ng.samuel.notdemo.ebankingms.authenticationservice.web.NotificationRestClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRestClient notificationRestClient;

    public NotificationService(NotificationRestClient notificationRestClient) {
        this.notificationRestClient = notificationRestClient;
    }


    @Async
    public void send(String to, String subject, String body) {
        NotificationRequestDTO dto = new NotificationRequestDTO(to, subject, body);
        notificationRestClient.sendNotification(dto);
    }
}
