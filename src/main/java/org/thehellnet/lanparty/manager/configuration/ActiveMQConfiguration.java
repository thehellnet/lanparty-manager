package org.thehellnet.lanparty.manager.configuration;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;

import java.util.Collections;

import static org.apache.activemq.ActiveMQConnectionFactory.DEFAULT_BROKER_URL;

@Configuration
@EnableJms
public class ActiveMQConfiguration implements TestAwareConfiguration {

    private static final String DEFAULT_DESTINATION_NAME = "server-logline-queue";

    private ActiveMQConnectionFactory connectionFactory;
    private JmsTemplate jmsTemplate;
    private DefaultJmsListenerContainerFactory jmsListenerContainerFactory;

    @Bean("connectionFactory")
    public ActiveMQConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = new ActiveMQConnectionFactory();
            connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
            connectionFactory.setTrustedPackages(Collections.singletonList("org.thehellnet.lanparty.manager"));
        }
        return connectionFactory;
    }

    @Bean("jmsTemplate")
    public JmsTemplate getJmsTemplate() {
        if (jmsTemplate == null) {
            jmsTemplate = new JmsTemplate();
            jmsTemplate.setConnectionFactory(getConnectionFactory());
            jmsTemplate.setDefaultDestinationName(DEFAULT_DESTINATION_NAME);
        }
        return jmsTemplate;
    }

    @Bean("jmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory getJmsListenerContainerFactory() {
        if (jmsListenerContainerFactory == null) {
            jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
            jmsListenerContainerFactory.setConnectionFactory(getConnectionFactory());
            jmsListenerContainerFactory.setConcurrency("1-1");
        }
        return jmsListenerContainerFactory;
    }
}
