package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
