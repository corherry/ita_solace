package demo.config;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class SolaceErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable throwable) {
        System.out.println("[Solace-Error] - " + throwable.getMessage());
    }
}
