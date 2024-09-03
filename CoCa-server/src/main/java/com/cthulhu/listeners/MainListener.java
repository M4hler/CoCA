package com.cthulhu.listeners;

import com.cthulhu.events.RollEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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

            if(tryParse(body, RollEvent.class)) {
                var event = mapper.readValue(body, RollEvent.class);
                System.out.println("Event: " + event.getDie());
            }
            else {
                System.out.println("Other message");
            }
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean tryParse(String body, Class<?> clazz) {
        try {
            mapper.readValue(body, clazz);
            return true;
        }
        catch(JsonProcessingException e) {
            return false;
        }
    }
}
