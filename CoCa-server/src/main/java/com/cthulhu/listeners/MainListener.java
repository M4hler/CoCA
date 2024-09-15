package com.cthulhu.listeners;

import com.cthulhu.events.Event;
import com.cthulhu.services.CoCaListenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MainListener implements MessageListener {
    private final ObjectMapper mapper;
    private final Map<Event, CustomListener<? extends Event>> listeners;

    public MainListener() {
        mapper = new ObjectMapper();
        listeners = CoCaListenerService.getCocaListeners();
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("onMessage");
            String body = message.getBody(String.class);
            System.out.println("Received message! " + body);

            for(var entry : listeners.entrySet()) {
                var event = tryParse(body, entry.getKey().getClass());
                if(event != null) {
                    var listener = entry.getValue();
                    listener.handleRequest(event);
                }
            }
        }
        catch (JMSException e) {
            System.out.println("Encountered error: " + e);
        }
    }

    private <T extends Event> T tryParse(String body, Class<T> toCast) {
        try {
            return mapper.readValue(body, toCast);
        }
        catch(Exception e) {
            return null;
        }
    }
}
