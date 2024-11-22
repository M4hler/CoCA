package com.coca.server.listeners;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.AcceptEvent;
import com.coca.server.services.RollEventService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = AcceptEvent.class)
public class AcceptEventListener extends CustomListener<AcceptEvent> {
    private final RollEventService rollEventService;

    public AcceptEventListener(RollEventService rollEventService) {
        this.rollEventService = rollEventService;
    }

    @Override
    protected void handle(AcceptEvent event) {
        rollEventService.accept(event.getBladeRunner());
    }
}
