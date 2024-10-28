package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.NpcDataEvent;
import com.cthulhu.services.NpcService;
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
