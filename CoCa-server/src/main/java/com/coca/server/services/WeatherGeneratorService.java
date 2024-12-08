package com.coca.server.services;

import com.coca.server.models.WeatherGenerator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;

@Service
public class WeatherGeneratorService {
    public WeatherGeneratorService(XmlValidationService xmlValidationService) {
        var result = xmlValidationService.validate("weatherGenerator.xsd", "weatherGenerator.xml");
        if (result) {
            var weatherGenerator = unmarshall("weatherGenerator.xml");
            validate(weatherGenerator);
        }
    }

    public WeatherGenerator unmarshall(String xmlName) {
        try {
            var context = JAXBContext.newInstance(WeatherGenerator.class);
            var xmlInput = getClass().getClassLoader().getResourceAsStream(xmlName);
            var source = new StreamSource(xmlInput);
            return (WeatherGenerator) context.createUnmarshaller().unmarshal(source);
        } catch (JAXBException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    private void validate(WeatherGenerator weatherGenerator) {
        var idMap = new HashMap<String, Integer>();
        var foundStart = false;
        for (var node : weatherGenerator.getNodes()) {
            idMap.merge(node.getId(), 1, Integer::sum);
            if (node.isStart()) {
                foundStart = true;
            }

            if (node.getTransitions() == null) {
                continue;
            }

            for (var transition : node.getTransitions()) {
                idMap.merge(transition.getTarget(), 1, Integer::sum);
            }
        }

        for (var entry : idMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("Found start: " + foundStart);
    }
}
