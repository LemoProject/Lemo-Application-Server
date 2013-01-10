package de.lemo.apps.application.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

@XmlType
class UserConfig {

    @XmlAttribute(required = true)
    public String name;

    @XmlAttribute(required = true)
    public String password;

    @XmlElement
    @XmlList
    public String courses;

}
