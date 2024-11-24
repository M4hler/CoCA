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
        var connection = connectionFactory.createConnection();
        connection.start();
        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        var queue = session.createQueue(QUEUE_SERVER_PREFIX + account.getName());

        queues.put(account, queue);
        return queue.getQueueName();
    }

    public String createPlayerQueue(Account account) throws JMSException {
        var connection = connectionFactory.createConnection();
        connection.start();
        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        var queue = session.createQueue(QUEUE_PLAYER_PREFIX + account.getName());

        var consumer = session.createConsumer(queue);
        consumer.setMessageListener(mainListener);

        return queue.getQueueName();
    }

    public void sendToAll(Event event) {
        for(var queue : queues.entrySet()) {
            sendToQueue(event, queue.getValue());
        }
    }

    public void sendToAdminQueue(Event event) {
        for(var queue : queues.entrySet()) {
            if(queue.getKey().isAdmin()) {
                sendToQueue(event, queue.getValue());
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

    public boolean isAdminOnline() {
        return queues.entrySet().stream().anyMatch(x -> x.getKey().isAdmin());
    }
}
