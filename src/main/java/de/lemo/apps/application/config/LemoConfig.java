/**
	 * File LemoConfig.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
class LemoConfig {

	@XmlElement(name = "apps")
	public ApplicationServer applicationServer;

}
