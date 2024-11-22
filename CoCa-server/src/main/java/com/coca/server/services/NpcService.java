package com.coca.server.services;

import com.coca.server.repositories.NpcRepository;
import com.coca.server.models.Npc;
import org.springframework.stereotype.Service;

@Service
public class NpcService {
    private final NpcRepository repository;

    public NpcService(NpcRepository repository) {
        this.repository = repository;
    }

    public void save(Npc npc) {
        repository.save(npc);
    }
}
