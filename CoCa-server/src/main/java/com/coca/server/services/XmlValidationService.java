package com.coca.server.services;

import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

@Service
public class XmlValidationService {
    public boolean validate(String xsdName, String xmlName) {
        try {
            var classLoader = getClass().getClassLoader();
            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            var xsdInput = classLoader.getResourceAsStream(xsdName);
            var schema = factory.newSchema(new StreamSource(xsdInput));
            var validator = schema.newValidator();

            var xmlInput = classLoader.getResourceAsStream(xmlName);
            var source = new StreamSource(xmlInput);
            validator.validate(source);

            return true;
        } catch (SAXException | IOException e) {
            System.out.println("Error: " + e);
            return false;
        }
    }
}
