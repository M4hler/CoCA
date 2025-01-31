package com.coca.server.listeners;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.PushEvent;
import com.coca.server.services.RollEventService;
import jakarta.jms.Queue;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = PushEvent.class)
public class PushEventListener extends CustomListener<PushEvent> {
    private final RollEventService rollEventService;

    public PushEventListener(RollEventService rollEventService) {
        this.rollEventService = rollEventService;
    }

    @Override
    protected void handle(PushEvent event, Queue source) {
        rollEventService.pushRoll(event.getBladeRunnerId());
    }
}
