package com.coca.server.listeners;

import com.coca.server.events.Event;
import com.coca.server.models.MessageCode;
import com.coca.server.services.CoCaListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MainListener implements MessageListener {
    private final ObjectMapper mapper;
    private final Map<Class<? extends Event>, CustomListener<? extends Event>> listeners;

    public MainListener() {
        mapper = new ObjectMapper();
        listeners = CoCaListenerService.getCocaListeners();
    }

    @Override
    public void onMessage(Message message) {
        try {
            var queue = (Queue) message.getJMSDestination();
            var body = message.getBody(String.class);
            var tree = mapper.readTree(body);
            var messageCode = tree.get("messageCode").asInt();
            var eventClass = MessageCode.getEvent(messageCode);
            var event = tryParse(body, eventClass);
            if (event != null) {
                var listener = listeners.get(eventClass);
                listener.handleRequest(event, queue);
            }
        } catch (JMSException | JsonProcessingException e) {
            System.out.println("Encountered error: " + e);
        }
    }

    private <T extends Event> T tryParse(String body, Class<T> toCast) {
        try {
            return mapper.readValue(body, toCast);
        } catch (Exception e) {
            return null;
        }
    }
}
