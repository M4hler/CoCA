package com.coca.server.services;

import org.springframework.stereotype.Service;

@Service
public class WeatherGeneratorService {
    public WeatherGeneratorService(XmlValidationService xmlValidationService) {
        var result = xmlValidationService.validate("weatherGenerator.xsd", "weatherGenerator.xml");
        System.out.println("RESULT: " + result);
    }
}
