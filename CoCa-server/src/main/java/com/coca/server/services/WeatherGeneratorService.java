package com.coca.server.services;

import com.coca.server.models.WeatherGenerator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;

@Service
public class WeatherGeneratorService {
    public WeatherGeneratorService(XmlValidationService xmlValidationService) {
        var result = xmlValidationService.validate("weatherGenerator.xsd", "weatherGenerator.xml");
        if(result) {
            var weatherGenerator = unmarshall("weatherGenerator.xml");
        }
    }

    public WeatherGenerator unmarshall(String xmlFile) {
        try {
            var context = JAXBContext.newInstance(WeatherGenerator.class);
            var source = new StreamSource(new ClassPathResource("/src/main/resources/" + xmlFile).getPath());
            return (WeatherGenerator) context.createUnmarshaller().unmarshal(source);
        }
        catch(JAXBException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}
