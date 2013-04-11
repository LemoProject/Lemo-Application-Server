/**
 * File LemoUserConfig.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.application.config;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Static name for the user configuration 
 *
 */
@XmlRootElement(name = "lemo-users")
class LemoUserConfig {

	@XmlElement(name = "user")
	public List<UserConfig> users;

}
