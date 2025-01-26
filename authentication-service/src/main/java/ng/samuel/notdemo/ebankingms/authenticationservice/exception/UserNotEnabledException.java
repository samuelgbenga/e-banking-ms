package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class UserNotEnabledException extends RuntimeException {
    public UserNotEnabledException(String message) {
        super(message);
    }
}
