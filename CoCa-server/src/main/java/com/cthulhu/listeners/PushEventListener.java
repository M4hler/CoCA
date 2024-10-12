package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.PushEvent;
import com.cthulhu.services.BladeRunnerService;
import com.cthulhu.services.RollEventService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = PushEvent.class)
public class PushEventListener extends CustomListener<PushEvent> {
    private final BladeRunnerService bladeRunnerService;
    private final RollEventService rollEventService;

    public PushEventListener(BladeRunnerService bladeRunnerService, RollEventService rollEventService) {
        this.bladeRunnerService = bladeRunnerService;
        this.rollEventService = rollEventService;
    }

    @Override
    protected void handle(PushEvent event) {
        var bladeRunner = bladeRunnerService.getBladeRunner(event.getBladeRunner());
        rollEventService.pushRoll(bladeRunner.getName());
    }
}
