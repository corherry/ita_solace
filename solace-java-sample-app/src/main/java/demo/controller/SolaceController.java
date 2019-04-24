package demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.sender.MessageSender;
import demo.translator.JasonTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solace")
public class SolaceController {
    @Autowired
    private MessageSender sender;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${solace.topic.test-topic}")
    private String TOPIC;

    @PostMapping
    public void send(@RequestBody JasonTranslator jasonTranslator) throws JsonProcessingException {
        System.out.println("***************** solace send message to topic");
        sender.sendMessageToTopic(TOPIC, objectMapper.writeValueAsString(jasonTranslator));
    }

}
