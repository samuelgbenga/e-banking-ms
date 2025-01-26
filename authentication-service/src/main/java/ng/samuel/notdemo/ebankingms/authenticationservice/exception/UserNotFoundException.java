package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
