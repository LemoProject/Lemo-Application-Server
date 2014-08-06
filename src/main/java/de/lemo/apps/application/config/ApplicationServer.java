/**
 * File ./src/main/java/de/lemo/apps/application/config/ApplicationServer.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

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
	
	@XmlElementWrapper(name = "users", required = true)
	@XmlElement(name = "user", required = true)
	public List<UserConfig> users = Lists.newArrayList();
	
	@XmlElement(name = "user-dn-template", required = true)
	public String userDnTemplate;

	@XmlElement(name = "context-factory-url", required = true)
	public String contextFactoryUrl;
	
}
