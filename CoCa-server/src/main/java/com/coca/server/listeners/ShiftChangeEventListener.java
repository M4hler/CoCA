package com.coca.server.listeners;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.ShiftChangeEvent;
import com.coca.server.services.ShiftService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = ShiftChangeEvent.class)
public class ShiftChangeEventListener extends CustomListener<ShiftChangeEvent> {
    private final ShiftService shiftService;

    public ShiftChangeEventListener(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @Override
    protected void handle(ShiftChangeEvent event) {
        shiftService.changeShift(event.getShift());
    }
}
