package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.BladeRunnerDataEvent;
import lombok.Setter;

import java.util.function.Consumer;

@CoCaListener(handles = BladeRunnerDataEvent.class)
public class BladeRunnerDataEventListener extends CustomListener<BladeRunnerDataEvent> {
    @Setter
    private Consumer<String> hook;

    @Override
    protected void handle(BladeRunnerDataEvent event) {
        if(event.getBladeRunner() != null) {
            hook.accept(event.getBladeRunner().getName());
        }
    }
}
