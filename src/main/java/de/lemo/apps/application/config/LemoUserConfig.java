/**
 * File ./de/lemo/apps/application/config/LemoUserConfig.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.application.config;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lemo-user-import")
class LemoUserConfig {

	@XmlElement(name = "user")
	public List<UserConfig> users;

}
