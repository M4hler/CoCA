package com.coca.server.listeners;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.MainframeAddDataEvent;
import com.coca.server.services.MainframeService;
import jakarta.jms.Queue;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = MainframeAddDataEvent.class)
public class MainframeAddDataEventListener extends CustomListener<MainframeAddDataEvent> {
    private final MainframeService mainframeService;

    public MainframeAddDataEventListener(MainframeService mainframeService) {
        this.mainframeService = mainframeService;
    }

    @Override
    protected void handle(MainframeAddDataEvent event, Queue source) {
        mainframeService.save(event.getMainframe());
    }
}
