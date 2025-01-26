package ng.samuel.notdemo.ebankingms.authenticationservice.dto;

public record LoginResponseDTO(String jwt, boolean passwordNeedToBeUpdate) {
}
