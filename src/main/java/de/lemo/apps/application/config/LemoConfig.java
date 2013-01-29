/**
 * File ./de/lemo/apps/application/config/LemoConfig.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.application.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
class LemoConfig {

	@XmlElement(name = "apps")
	public ApplicationServer applicationServer;

}
