package ng.samuel.notdemo.ebankingms.authenticationservice.dto;


import ng.samuel.notdemo.ebankingms.authenticationservice.utils.validator.Password;

public record UpdatePasswordRequestDTO(

        @Password(message = "field 'oldPassword' is not well formated : it must have at least two uppercase letters, two lowercase letters, two numbers and one special character.")
        String oldPassword,

        @Password(message = "field 'newPassword' is not well formated : it must have at least two uppercase letters, two lowercase letters, two numbers and one special character.")
        String newPassword,

        @Password(message = "field 'newPasswordConfirm' is not well formated : it must have at least two uppercase letters, two lowercase letters, two numbers and one special character.")
        String newPasswordConfirm) {
}
