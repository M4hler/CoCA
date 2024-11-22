package com.coca.server.services;

import com.coca.server.models.Mainframe;
import com.coca.server.repositories.MainframeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainframeService {
    private final MainframeRepository repository;

    public MainframeService(MainframeRepository repository) {
        this.repository = repository;
    }

    public void save(Mainframe mainframe) {
        repository.save(mainframe);
    }

    public List<Mainframe> getAll() {
        return repository.findAll();
    }
}
