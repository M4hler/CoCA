package com.cthulhu.controllers;

import com.cthulhu.events.JoinEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

public class TestListener implements MessageListener {
    private final ObjectMapper mapper;

    public TestListener() {
        mapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("onMessage in javafx application");
            String body = message.getBody(String.class);
            var event = mapper.readValue(body, JoinEvent.class);
            System.out.println("event: " + event.getName());
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
