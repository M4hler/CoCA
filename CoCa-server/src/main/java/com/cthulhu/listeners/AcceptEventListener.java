package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.AcceptEvent;
import com.cthulhu.services.RollEventService;
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
