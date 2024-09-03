package com.cthulhu.controllers;

import com.cthulhu.events.BladeRunnerDataEvent;
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
            if(tryParse(body, JoinEvent.class)) {
                var event = mapper.readValue(body, JoinEvent.class);
                System.out.println("JoinEvent: " + event.getName());
                if(controller != null) {
                    controller.test(event.getName());
                }
            }
            else if(tryParse(body, BladeRunnerDataEvent.class)) {
                var event = mapper.readValue(body, BladeRunnerDataEvent.class);
                if(event.getBladeRunner() != null) {
                    System.out.println("BladeRunner data: " + event.getBladeRunner().getName());
                    controller.test2(event.getBladeRunner().getName());
                }
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
