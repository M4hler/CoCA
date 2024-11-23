package com.coca.server.models;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;

import java.util.List;

@Getter
public class Node {
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "transition")
    private List<Transition> transitions;
}
