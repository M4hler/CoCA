package com.coca.server.services;

import com.coca.server.events.*;
import com.coca.server.listeners.MainListener;
import com.coca.server.models.Account;
import com.coca.server.models.MessageCode;
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
    private final Map<Account, Queue> serverQueues;
    private final Map<Account, Queue> playerQueues;
    private static final String QUEUE_SERVER_PREFIX = "queue_server";
    private static final String QUEUE_PLAYER_PREFIX = "queue_player";

    public MessageSenderService(ConnectionFactory connectionFactory, JmsTemplate jmsTemplate, MainListener mainListener) {
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        this.mainListener = mainListener;
        serverQueues = new HashMap<>();
        playerQueues = new HashMap<>();
    }

    public String createServerQueue(Account account) throws JMSException {
        var connection = connectionFactory.createConnection();
        connection.start();
        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        var queue = session.createQueue(QUEUE_SERVER_PREFIX + account.getName());

        serverQueues.put(account, queue);
        return queue.getQueueName();
    }

    public String createPlayerQueue(Account account) throws JMSException {
        var connection = connectionFactory.createConnection();
        connection.start();
        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        var queue = session.createQueue(QUEUE_PLAYER_PREFIX + account.getName());

        var consumer = session.createConsumer(queue);
        consumer.setMessageListener(mainListener);

        playerQueues.put(account, queue);
        return queue.getQueueName();
    }

    public void sendToAll(Event event) {
        for(var entry : serverQueues.entrySet()) {
            sendToQueue(event, entry.getValue());
        }
    }

    public void sendToAdminQueue(Event event) {
        for(var entry : serverQueues.entrySet()) {
            if(entry.getKey().isAdmin()) {
                sendToQueue(event, entry.getValue());
            }
        }
    }

    public void sendToQueue(Event event, Queue queue) {
        try {
            event.setMessageCode(MessageCode.getMessageCode(event.getClass()));
            jmsTemplate.convertAndSend(queue.getQueueName(), event);
        }
        catch(JMSException e) {
            System.out.println("Error while sending event: " + event.getClass() + " to queue " + e);
        }
    }

    public void respondToQueue(Event event, Queue source) {
        for (var entry : playerQueues.entrySet()) {
            try {
                if (entry.getValue().getQueueName().equals(source.getQueueName())) {
                    sendToQueue(event, serverQueues.get(entry.getKey()));
                }
            } catch (JMSException e) {
                System.out.println("Error when getting queue name");
            }
        }
    }

    public boolean isAdminOnline() {
        return serverQueues.entrySet().stream().anyMatch(x -> x.getKey().isAdmin());
    }
}
