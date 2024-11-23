package com.coca.server.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.util.List;

@XmlRootElement(name = "weatherGenerator")
@Getter
public class WeatherGenerator {
    @XmlElement(name = "node")
    private List<Node> nodes;
}
