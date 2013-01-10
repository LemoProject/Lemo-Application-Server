package de.lemo.apps.application.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lemo-users")
class LemoUserConfig {

    @XmlElement(name = "apps")
    public List<UserConfig> users;

}
