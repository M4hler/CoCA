package com.cthulhu.services;

import com.cthulhu.events.BladeRunnerDataEvent;
import com.cthulhu.events.JoinEvent;
import com.cthulhu.events.RollResultEvent;
import com.cthulhu.listeners.MainListener;
import com.cthulhu.models.Account;
import com.cthulhu.models.BladeRunner;
import jakarta.jms.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageSenderService {
    private final ConnectionFactory connectionFactory;
    private final JmsTemplate jmsTemplate;
    private final MainListener mainListener;
    private final Map<Account, Queue> queues;
    private static final String QUEUE_SERVER_PREFIX = "queue_server";
    private static final String QUEUE_PLAYER_PREFIX = "queue_player";

    public MessageSenderService(ConnectionFactory connectionFactory, JmsTemplate jmsTemplate, MainListener mainListener) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        this.mainListener = mainListener;
        queues = new HashMap<>();
    }

    public String createServerQueue(Account account) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_SERVER_PREFIX + account.getName());

        queues.put(account, queue);
        return queue.getQueueName();
    }

    public String createPlayerQueue(Account account) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_PLAYER_PREFIX + account.getName());

        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(mainListener);

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

    public void sendRollResultEvent(RollResultEvent event) {
        for(var queue : queues.entrySet()) {
            try {
                jmsTemplate.convertAndSend(queue.getValue().getQueueName(), event);
            }
            catch(JMSException e) {
                System.out.println("Error while sending to queue bound to account: "
                        + queue.getKey().getName() + ", error: " + e);
            }
        }
    }

    public boolean isAdminOnline() {
        return queues.entrySet().stream().anyMatch(x -> x.getKey().isAdmin());
    }
}
