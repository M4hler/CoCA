package com.cthulhu.services;

import com.cthulhu.models.Npc;
import com.cthulhu.repositories.NpcRepository;
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
