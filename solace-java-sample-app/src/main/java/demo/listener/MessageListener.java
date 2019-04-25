package demo.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.OperationLog;
import demo.repository.OperationRepositpory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageListener {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String DEFAULT_CONCURRENCY = "3";
    private static final String QUEUE_LISTENER_FACTORY = "queueListenerFactory";

    @Autowired
    private OperationRepositpory operationRepositpory;

    @JmsListener(
            destination = "${solace.queue.test-queue}",
            containerFactory = QUEUE_LISTENER_FACTORY,
            concurrency = DEFAULT_CONCURRENCY)
    public void startProcess(String message) throws IOException {
        OperationLog operationLog = objectMapper.readValue(message, OperationLog.class);
        operationRepositpory.save(operationLog);
    }
}
