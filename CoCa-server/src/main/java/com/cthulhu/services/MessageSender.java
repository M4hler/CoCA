package com.cthulhu.services;

import com.cthulhu.listeners.MainListener;
import jakarta.jms.*;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    private final ConnectionFactory connectionFactory;
    private static final String QUEUE_PREFIX = "queue_";

    public MessageSender(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String createQueue(String name) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_PREFIX + name);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MainListener());

        return queue.getQueueName();
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
