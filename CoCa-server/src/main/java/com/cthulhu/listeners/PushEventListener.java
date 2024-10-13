package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.PushEvent;
import com.cthulhu.services.RollEventService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = PushEvent.class)
public class PushEventListener extends CustomListener<PushEvent> {
    private final RollEventService rollEventService;

    public PushEventListener(RollEventService rollEventService) {
        this.rollEventService = rollEventService;
    }

    @Override
    protected void handle(PushEvent event) {
        rollEventService.pushRoll(event.getBladeRunner());
    }
}
