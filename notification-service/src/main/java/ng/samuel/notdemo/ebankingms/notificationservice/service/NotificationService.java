package ng.samuel.notdemo.ebankingms.notificationservice.service;


import ng.samuel.notdemo.ebankingms.notificationservice.dto.NotificationRequestDTO;

public interface NotificationService {

    void send(NotificationRequestDTO dto);
}
