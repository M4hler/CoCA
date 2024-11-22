package com.coca.server.listeners;

import com.coca.server.annotations.CoCaListener;
import com.coca.server.events.NpcDataEvent;
import com.coca.server.services.NpcService;
import org.springframework.stereotype.Service;

@Service
@CoCaListener(handles = NpcDataEvent.class)
public class NpcDataEventListener extends CustomListener<NpcDataEvent> {
    private final NpcService npcService;

    public NpcDataEventListener(NpcService npcService) {
        this.npcService = npcService;
    }

    @Override
    protected void handle(NpcDataEvent event) {
        npcService.save(event.getNpc());
    }
}
