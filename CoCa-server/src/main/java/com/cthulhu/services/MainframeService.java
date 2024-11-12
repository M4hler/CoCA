package com.cthulhu.services;

import com.cthulhu.models.Mainframe;
import com.cthulhu.repositories.MainframeRepository;
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
