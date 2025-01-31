package com.coca.server.services;

import com.coca.server.models.BladeRunner;
import com.coca.server.repositories.BladeRunnerRepository;
import org.springframework.stereotype.Service;

@Service
public class BladeRunnerService {
    private final BladeRunnerRepository repository;

    public BladeRunnerService(BladeRunnerRepository repository) {
        this.repository = repository;
    }

    public BladeRunner getBladeRunner(int id) {
        return repository.findById(id);
    }
}
