package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class ServiceDownException extends RuntimeException{
    public ServiceDownException(String message) {
        super(message);
    }
}
