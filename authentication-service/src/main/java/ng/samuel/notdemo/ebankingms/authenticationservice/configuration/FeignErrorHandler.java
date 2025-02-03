package ng.samuel.notdemo.ebankingms.authenticationservice.configuration;


import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.NotFoundException;
import ng.samuel.notdemo.ebankingms.authenticationservice.exception.ServiceDownException;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorHandler  implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new NotFoundException("Customer not found");
        }
        if (response.status() == 503) {
            return new ServiceDownException("The service is down");
        }
        // Customize other error handling logic here
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
