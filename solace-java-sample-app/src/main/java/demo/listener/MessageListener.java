package demo.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.translator.JasonTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageListener {

    private static final String DEFAULT_CONCURRENCY = "3";
    private static final String QUEUE_LISTENER_FACTORY = "queueListenerFactory";

    @Autowired
    ObjectMapper objectMapper;

    @JmsListener(
            destination = "${solace.queue.test-queue}",
            containerFactory = QUEUE_LISTENER_FACTORY,
            concurrency = DEFAULT_CONCURRENCY)
    public void startProcess(String originalMessage) throws IOException {
        if (originalMessage != null) {
            JasonTranslator jasonTranslator = objectMapper.readValue(originalMessage, JasonTranslator.class);
            System.out.println(jasonTranslator.toString());
        }
    }
}
