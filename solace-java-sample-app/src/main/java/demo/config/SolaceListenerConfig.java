package demo.config;

import org.springframework.messaging.converter.MessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import javax.jms.ConnectionFactory;
import java.util.Arrays;

@EnableJms
@Configuration
public class SolaceListenerConfig implements JmsListenerConfigurer {

    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public DefaultJmsListenerContainerFactory queueListenerFactory(ConnectionFactory connectionFactory, SolaceErrorHandler solaceErrorHandler) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(solaceErrorHandler);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        CachingConnectionFactory ccf = new CachingConnectionFactory(connectionFactory);
        JmsTemplate jmsTemplate = new JmsTemplate(ccf);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        DefaultMessageHandlerMethodFactory factory = handlerMethodFactory();
        registrar.setMessageHandlerMethodFactory(factory);
    }

    /**
     * 自定义jms factory，通过定义message converter 实现就json的支持
     */
    @Bean
    public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        MappingJackson2MessageConverter mappingJackson2MessageConverter =
                new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setObjectMapper(objectMapper);
        MessageConverter messageConverter =
                new CompositeMessageConverter(
                        Arrays.asList(new GenericMessageConverter(), mappingJackson2MessageConverter));
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
