/**
 * File ./src/main/java/de/lemo/apps/application/config/UserConfig.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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
 * File UserConfig.java
 * Date Feb 14, 2013
 * 
 * @author Leonard Kappe
 */
package de.lemo.apps.application.config;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

/**
 * Class reprasentation for the user configuration in user.xml
 *
 */
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
