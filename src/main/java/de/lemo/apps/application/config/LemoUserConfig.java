package de.lemo.apps.application.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lemo-user-import")
class LemoUserConfig {


    @XmlElement(name = "user")
    public List<UserConfig> users;

}
