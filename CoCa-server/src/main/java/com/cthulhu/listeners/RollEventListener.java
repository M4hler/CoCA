package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.RollEvent;
import com.cthulhu.services.BladeRunnerService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = RollEvent.class)
public class RollEventListener extends CustomListener<RollEvent> {
    private final BladeRunnerService bladeRunnerService;

    public RollEventListener(BladeRunnerService bladeRunnerService) {
        this.bladeRunnerService = bladeRunnerService;
    }

    @Override
    public void handle(RollEvent event) {
        System.out.println("Event: " + event.getBladeRunner() + " " + event.getSkill() + " " + event.getBonusDie());
        var bladeRunner = bladeRunnerService.getBladeRunner(event.getBladeRunner());
        System.out.println("Received: " + bladeRunner.getName() + " " + bladeRunner.getArchetype());
    }
}
