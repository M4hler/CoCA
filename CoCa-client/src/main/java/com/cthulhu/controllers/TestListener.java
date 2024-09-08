package com.cthulhu.controllers;

import com.cthulhu.events.BladeRunnerDataEvent;
import com.cthulhu.events.Event;
import com.cthulhu.events.JoinEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import lombok.Setter;

@Setter
public class TestListener implements MessageListener {
    private final ObjectMapper mapper;
    private SessionController controller;

    public TestListener() {
        mapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("onMessage in javafx application");
            String body = message.getBody(String.class);
            var event = tryParse(body, JoinEvent.class);
            if(event != null) {
                System.out.println("JoinEvent: " + event.getName());
                if(controller != null) {
                    controller.test(event.getName());
                }
                return;
            }

            var event1 = tryParse(body, BladeRunnerDataEvent.class);
            if(event1 != null) {
                if(event1.getBladeRunner() != null) {
                    System.out.println("BladeRunner data: " + event1.getBladeRunner().getName());
                    controller.test2(event1.getBladeRunner().getName());
                }
            }
        }
        catch (JMSException e) {
            e.printStackTrace();
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
