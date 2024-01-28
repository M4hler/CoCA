package com.cthulhu.services;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    private final ActiveMQConnectionFactory connectionFactory;

    public MessageSender(ActiveMQConnectionFactory connectionFactory) throws JMSException {
        this.connectionFactory = connectionFactory;
        sendMessage();
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
