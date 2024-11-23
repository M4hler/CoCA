package com.coca.server.models;

import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Getter;

@Getter
public class Transition {
    @XmlAttribute(name = "target")
    private String target;
    @XmlAttribute(name = "weight")
    private int weight;
}
