package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.ShiftChangeEvent;
import com.cthulhu.services.ShiftService;
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
