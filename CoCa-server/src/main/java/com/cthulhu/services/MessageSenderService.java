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
    private final List<Queue> queues;
    private static final String QUEUE_PREFIX = "queue_";

    public MessageSenderService(ConnectionFactory connectionFactory, JmsTemplate jmsTemplate) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        queues = new ArrayList<>();
    }

    public String createQueue(String name) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_PREFIX + name);

        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MainListener());

        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        queues.add(queue);
        return queue.getQueueName();
    }

//    public void test() {
//        jmsTemplate.convertAndSend("testQueue", "Hello World!");
//    }

    public void sendJoinEvent(String name) throws JMSException {
//        for(var entry : sessionsToProducers.entrySet()) {
//            var session = entry.getKey();
//            var producer = entry.getValue();
//            var message = session.createObjectMessage(new JoinEvent(name));
//            producer.send(message);
//            System.out.println("Send message by " + name);
//        }

        for(var queue : queues) {
            jmsTemplate.convertAndSend(queue.getQueueName(), new JoinEvent(name));
        }
    }

    public void sendMessage() throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("testQueue");

        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        String message = "Hello, World!";
        TextMessage textMessage = session.createTextMessage(message);

        producer.send(textMessage);
        System.out.println("Sending message");

        producer.close();
        session.close();
        connection.close();
    }
}
