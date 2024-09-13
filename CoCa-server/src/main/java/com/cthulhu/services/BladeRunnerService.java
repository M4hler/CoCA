package com.cthulhu.services;

import com.cthulhu.models.BladeRunner;
import com.cthulhu.repositories.BladeRunnerRepository;
import org.springframework.stereotype.Service;

@Service
public class BladeRunnerService {
    private final BladeRunnerRepository repository;

    public BladeRunnerService(BladeRunnerRepository repository) {
        this.repository = repository;
    }

    public BladeRunner getBladeRunner(String name) {
        return repository.findByName(name);
    }
}
