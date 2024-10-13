package com.cthulhu.listeners;

import com.cthulhu.events.Event;
import com.cthulhu.models.MessageCode;
import com.cthulhu.services.CoCaListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import lombok.Setter;

import java.util.Map;

@Setter
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
            String body = message.getBody(String.class);
            var tree = mapper.readTree(body);
            int messageCode = tree.get("messageCode").asInt();
            var eventClass = MessageCode.getEvent(messageCode);
            var event = tryParse(body, eventClass);
            if(event != null) {
                var listener = listeners.get(eventClass);
                listener.handleRequest(event);
            }
        }
        catch (JMSException | JsonProcessingException e) {
            System.out.println("Encountered error: " + e);
        }
    }

    private <T extends Event> T tryParse(String body, Class<T> clazz) {
        try {
            return mapper.readValue(body, clazz);
        }
        catch(JsonProcessingException e) {
            return null;
        }
    }
}
