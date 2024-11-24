package com.coca.server.listeners;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.MainframeAllDataRequestEvent;
import com.coca.server.services.MainframeService;
import jakarta.jms.Queue;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = MainframeAllDataRequestEvent.class)
public class MainframeAllDataRequestEventListener extends CustomListener<MainframeAllDataRequestEvent> {
    private final MainframeService mainframeService;

    public MainframeAllDataRequestEventListener(MainframeService mainframeService) {
        this.mainframeService = mainframeService;
    }

    @Override
    protected void handle(MainframeAllDataRequestEvent event, Queue source) {
        mainframeService.respondWithAllData(source);
    }
}
