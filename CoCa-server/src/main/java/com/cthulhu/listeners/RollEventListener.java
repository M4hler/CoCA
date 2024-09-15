package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.RollEvent;
import com.cthulhu.services.BladeRunnerService;
import com.cthulhu.services.RollEventService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = RollEvent.class)
public class RollEventListener extends CustomListener<RollEvent> {
    private final BladeRunnerService bladeRunnerService;
    private final RollEventService rollEventService;

    public RollEventListener(BladeRunnerService bladeRunnerService, RollEventService rollEventService) {
        this.bladeRunnerService = bladeRunnerService;
        this.rollEventService = rollEventService;
    }

    @Override
    public void handle(RollEvent event) {
        System.out.println("Event: " + event.getBladeRunner() + " " + event.getSkill() + " " + event.getBonusDie());
        var bladeRunner = bladeRunnerService.getBladeRunner(event.getBladeRunner());
        var attributeDie = bladeRunner.getAttributeValueForSkill(event.getSkill().toLowerCase());
        var skillDie = bladeRunner.getSkillValue(event.getSkill().toLowerCase());
        var bonusDie = event.getBonusDie() != null ? event.getBonusDie() : 0;
        System.out.println("After processing: " + bladeRunner.getName() + " " + attributeDie + " " + skillDie + " " + bonusDie);
    }
}
