package com.coca.server.services;

import com.coca.server.events.MainframeAllDataEvent;
import com.coca.server.models.Mainframe;
import com.coca.server.repositories.MainframeRepository;
import jakarta.jms.Queue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainframeService {
    private final MessageSenderService messageSenderService;
    private final MainframeRepository repository;

    public MainframeService(MessageSenderService messageSenderService, MainframeRepository repository) {
        this.messageSenderService = messageSenderService;
        this.repository = repository;
    }

    public void save(Mainframe mainframe) {
        repository.save(mainframe);
    }

    public List<Mainframe> getAll() {
        return repository.findAll();
    }

    public void respondWithAllData(Queue queue) {
        var data = getAll();
        var event = new MainframeAllDataEvent(data);
        messageSenderService.respondToQueue(event, queue);
    }
}
