package com.cthulhu.services;

import com.cthulhu.events.BladeRunnerDataEvent;
import com.cthulhu.events.JoinEvent;
import com.cthulhu.listeners.MainListener;
import com.cthulhu.models.Account;
import com.cthulhu.models.BladeRunner;
import jakarta.jms.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageSenderService {
    private final ConnectionFactory connectionFactory;
    private final JmsTemplate jmsTemplate;
    private final Map<Account, Topic> queues;
    private static final String QUEUE_PREFIX = "queue_";

    public MessageSenderService(ConnectionFactory connectionFactory, JmsTemplate jmsTemplate) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        queues = new HashMap<>();
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

        queues.put(account, topic);
        return topic.getTopicName();
    }

    public void sendJoinEvent(String name, BladeRunner bladeRunner) throws JMSException {
        for(var queue : queues.entrySet()) {
            jmsTemplate.convertAndSend(queue.getValue().getTopicName(), new JoinEvent(name));
            if(queue.getKey().isAdmin()) {
                jmsTemplate.convertAndSend(queue.getValue().getTopicName(), new BladeRunnerDataEvent(bladeRunner));
            }
        }
    }

    public boolean isAdminOnline() {
        return queues.entrySet().stream().anyMatch(x -> x.getKey().isAdmin());
    }
}
