package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
