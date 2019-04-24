package demo.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

  @Autowired
  private JmsTemplate jmsTemplate;

  public void sendMessageToTopic(String destination, String message) {
    jmsTemplate.convertAndSend(destination, message);
  }
}
