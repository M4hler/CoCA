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
    private final Map<Account, Queue> queues;
    private static final String QUEUE_SERVER_PREFIX = "queue_server";
    private static final String QUEUE_PLAYER_PREFIX = "queue_player";

    public MessageSenderService(ConnectionFactory connectionFactory, JmsTemplate jmsTemplate) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        queues = new HashMap<>();
    }

    public String createServerQueue(Account account) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_SERVER_PREFIX + account.getName());

        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        queues.put(account, queue);
        return queue.getQueueName();
    }

    public String createPlayerQueue(Account account) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_PLAYER_PREFIX + account.getName());

        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MainListener());

        return queue.getQueueName();
    }

    public void sendJoinEvent(String name, BladeRunner bladeRunner) throws JMSException {
        for(var queue : queues.entrySet()) {
            jmsTemplate.convertAndSend(queue.getValue().getQueueName(), new JoinEvent(name));
            if(queue.getKey().isAdmin()) {
                jmsTemplate.convertAndSend(queue.getValue().getQueueName(), new BladeRunnerDataEvent(bladeRunner));
            }
        }
    }

    public boolean isAdminOnline() {
        return queues.entrySet().stream().anyMatch(x -> x.getKey().isAdmin());
    }
}
