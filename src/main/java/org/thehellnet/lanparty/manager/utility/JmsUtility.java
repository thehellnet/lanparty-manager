package org.thehellnet.lanparty.manager.utility;

import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

public final class JmsUtility {

    private JmsUtility() {
    }

    public static JmsTemplate prepareJmsTemplate(ConnectionFactory connectionFactory, String destinationName) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestinationName(destinationName);
        return jmsTemplate;
    }
}
