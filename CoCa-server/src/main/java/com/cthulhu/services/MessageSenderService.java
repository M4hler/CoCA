package com.cthulhu.services;

import com.cthulhu.events.JoinEvent;
import com.cthulhu.listeners.MainListener;
import jakarta.jms.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageSenderService {
    private final ConnectionFactory connectionFactory;
    private final JmsTemplate jmsTemplate;
    private final List<Topic> topics;
    private static final String QUEUE_PREFIX = "queue_";

    public MessageSenderService(ConnectionFactory connectionFactory, JmsTemplate jmsTemplate) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        topics = new ArrayList<>();
    }

    public String createQueue(String name) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(QUEUE_PREFIX + name);

        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MainListener());

        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        topics.add(topic);
        return topic.getTopicName();
    }

    public void sendJoinEvent(String name) throws JMSException {
        for(var topic : topics) {
            jmsTemplate.convertAndSend(topic.getTopicName(), new JoinEvent(name));
        }
    }
}
