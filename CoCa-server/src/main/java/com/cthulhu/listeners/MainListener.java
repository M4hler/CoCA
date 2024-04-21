package com.cthulhu.listeners;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

public class MainListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("onMessage");
            String body = message.getBody(String.class);
            System.out.println("Received message! " + body);
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
