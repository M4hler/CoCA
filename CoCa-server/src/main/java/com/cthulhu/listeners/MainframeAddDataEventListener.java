package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.MainframeAddDataEvent;
import com.cthulhu.services.MainframeService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = MainframeAddDataEvent.class)
public class MainframeAddDataEventListener extends CustomListener<MainframeAddDataEvent> {
    private final MainframeService mainframeService;

    public MainframeAddDataEventListener(MainframeService mainframeService) {
        this.mainframeService = mainframeService;
    }

    @Override
    protected void handle(MainframeAddDataEvent event) {
        mainframeService.save(event.getMainframe());
    }
}
