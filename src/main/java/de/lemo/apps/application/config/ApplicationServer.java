/**
	 * File ApplicationServer.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application.config;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import com.google.common.collect.Lists;

@XmlType
class ApplicationServer {

	private static final String DEFAULT_NAME = "Lemo Application Server";

	@XmlAttribute
	public String name = ApplicationServer.DEFAULT_NAME;

	@XmlElement(name = "dms-url", required = true)
	public String dataManagementServerURL;

	@XmlElementWrapper(name = "database", required = true)
	@XmlElement(name = "property", required = true)
	public List<PropertyConfig> appDbConfig = Lists.newArrayList();
}
