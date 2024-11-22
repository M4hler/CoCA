package com.coca.server.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GeneratorService {
    private final SecureRandom generator;

    public GeneratorService() {
        generator = new SecureRandom();
    }

    public int rollDie(int bound) {
        return generator.nextInt(bound) + 1;
    }
}
