package com.cthulhu.listeners;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
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
