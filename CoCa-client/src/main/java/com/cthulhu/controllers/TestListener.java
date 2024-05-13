package com.cthulhu.controllers;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

public class TestListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("onMessage in javafx application");
            String body = message.getBody(String.class);
            System.out.println("Received message! " + body);
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
