package com.cthulhu.services;

import com.cthulhu.enums.Shift;
import com.cthulhu.events.ShiftChangeResultEvent;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {
    private final MessageSenderService messageSenderService;

    public ShiftService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    public void changeShift(Shift shift) {
        var event = new ShiftChangeResultEvent(shift);
        messageSenderService.sendShiftChangeEvent(event);
    }
}
