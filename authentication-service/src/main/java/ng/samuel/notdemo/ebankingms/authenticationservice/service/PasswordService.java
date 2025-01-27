package ng.samuel.notdemo.ebankingms.authenticationservice.service;


import ng.samuel.notdemo.ebankingms.authenticationservice.dto.ChangePasswordRequestDTO;

public interface PasswordService {

    void requestCodeToResetPassword(String email);

    void resetPassword(ChangePasswordRequestDTO dto);

}
