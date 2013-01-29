/**
 * File ./de/lemo/apps/application/config/PropertyConfig.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.application.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType
class PropertyConfig {

	@XmlAttribute(name = "name", required = true)
	public String key;

	@XmlValue
	public String value;

}
