/**
 * File UserConfig.java
 * Date Feb 14, 2013
 * Copyright TODO (INSERT COPYRIGHT)
 * 
 * @author Leonard Kappe
 */
package de.lemo.apps.application.config;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

@XmlType
class UserConfig {

	@XmlAttribute(required = true)
	public String name;

	@XmlAttribute(required = true)
	public String password;

	@XmlAttribute
	public String email;

	@XmlAttribute(name = "fullname")
	public String fullName;

	@XmlList
	@XmlAttribute
	public List<String> roles;

	@XmlList
	@XmlAttribute
	public List<Long> courses;

}
