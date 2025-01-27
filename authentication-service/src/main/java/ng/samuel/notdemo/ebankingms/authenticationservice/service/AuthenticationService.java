package ng.samuel.notdemo.ebankingms.authenticationservice.service;


import ng.samuel.notdemo.ebankingms.authenticationservice.dto.LoginRequestDTO;
import ng.samuel.notdemo.ebankingms.authenticationservice.dto.LoginResponseDTO;

public interface AuthenticationService {

    LoginResponseDTO authenticate(LoginRequestDTO dto);
}
