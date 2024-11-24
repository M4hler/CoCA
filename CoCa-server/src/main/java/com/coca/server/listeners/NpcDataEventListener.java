package com.coca.server.listeners;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.NpcDataEvent;
import com.coca.server.services.NpcService;
import jakarta.jms.Queue;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = NpcDataEvent.class)
public class NpcDataEventListener extends CustomListener<NpcDataEvent> {
    private final NpcService npcService;

    public NpcDataEventListener(NpcService npcService) {
        this.npcService = npcService;
    }

    @Override
    protected void handle(NpcDataEvent event, Queue source) {
        npcService.save(event.getNpc());
    }
}
