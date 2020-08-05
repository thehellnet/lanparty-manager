package org.thehellnet.lanparty.manager.configuration;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;

@Configuration
@EnableJms
public class ActiveMQConfiguration implements TestAwareConfiguration {

    private ActiveMQConnectionFactory connectionFactory;
    private DefaultJmsListenerContainerFactory jmsListenerContainerFactory;

    @Bean("connectionFactory")
    public ActiveMQConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = new ActiveMQConnectionFactory();
            connectionFactory.setBrokerURL(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
            connectionFactory.setTrustAllPackages(true);
        }
        return connectionFactory;
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
