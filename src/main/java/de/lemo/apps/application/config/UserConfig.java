package de.lemo.apps.application.config;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType
class UserConfig {

	@XmlAttribute(required = true)
	public String username;

	@XmlAttribute(required = true)
	public String password;

	@XmlAttribute
	public String email;

	@XmlAttribute(name = "fullname")
	public String fullName;

	@XmlValue
	@XmlList
	public List<Long> courses;

}
