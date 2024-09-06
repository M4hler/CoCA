package com.cthulhu.listeners;

import com.cthulhu.events.Event;
import com.cthulhu.events.RollEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class MainListener implements MessageListener {
    private final ObjectMapper mapper;

    public MainListener() {
        mapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("onMessage");
            String body = message.getBody(String.class);
            System.out.println("Received message! " + body);

            var event = tryParse(body, RollEvent.class);
            if(event != null) {
                System.out.println("Event: " + event.getDie());
            }
            else {
                System.out.println("Other message");
            }
        }
        catch (JMSException e) {
            e.printStackTrace();
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
