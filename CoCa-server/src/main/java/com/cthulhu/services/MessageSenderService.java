package com.cthulhu.services;

import com.cthulhu.events.JoinEvent;
import com.cthulhu.listeners.MainListener;
import com.cthulhu.models.Account;
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
    private final List<Account> onlineAccounts;
    private static final String QUEUE_PREFIX = "queue_";

    public MessageSenderService(ConnectionFactory connectionFactory, JmsTemplate jmsTemplate) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        topics = new ArrayList<>();
        onlineAccounts = new ArrayList<>();
    }

    public String createQueue(Account account) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(QUEUE_PREFIX + account.getName());

        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MainListener());

        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        topics.add(topic);
        onlineAccounts.add(account);
        return topic.getTopicName();
    }

    public void sendJoinEvent(String name) throws JMSException {
        for(var topic : topics) {
            jmsTemplate.convertAndSend(topic.getTopicName(), new JoinEvent(name));
        }
    }

    public boolean isAdminOnline() {
        return onlineAccounts.stream().anyMatch(Account::isAdmin);
    }
}
