package com.cthulhu.listeners;

import com.cthulhu.events.Event;
import com.cthulhu.events.JoinEvent;
import com.cthulhu.events.RollEvent;
import com.cthulhu.services.BladeRunnerService;
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
    private final BladeRunnerService bladeRunnerService;

    public MainListener(BladeRunnerService bladeRunnerService) {
        mapper = new ObjectMapper();
        listeners = CoCaListenerService.getCocaListeners();
        this.bladeRunnerService = bladeRunnerService;
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
//            var event = tryParse(body, RollEvent.class);
//            if(event != null) {
//                System.out.println("Event: " + event.getBladeRunner() + " " + event.getSkill() + " " + event.getBonusDie());
//                var bladeRunner = bladeRunnerService.getBladeRunner(event.getBladeRunner());
//                System.out.println("Received: " + bladeRunner.getName() + " " + bladeRunner.getArchetype());
//            }
//            else {
//                System.out.println("Other message");
//            }
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
