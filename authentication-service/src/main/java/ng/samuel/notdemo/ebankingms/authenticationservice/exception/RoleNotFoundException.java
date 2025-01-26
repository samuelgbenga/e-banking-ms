package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
