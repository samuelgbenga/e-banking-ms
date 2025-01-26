package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
