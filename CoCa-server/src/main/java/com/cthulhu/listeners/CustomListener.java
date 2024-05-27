package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

//@CoCaListener(handles = JoinEvent.class)
public class CustomListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

    }
}
