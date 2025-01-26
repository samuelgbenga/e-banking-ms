package ng.samuel.notdemo.ebankingms.authenticationservice.exception;

public class VerificationCodeExpiredException extends RuntimeException {
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}
