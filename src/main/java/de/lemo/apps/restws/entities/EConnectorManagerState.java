/**
 * File ./src/main/java/de/lemo/apps/restws/entities/EConnectorManagerState.java
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
 * File ./main/java/de/lemo/dms/connectors/EConnectorState.java
 * Date 2013-01-24
 * Project Lemo Learning Analytics
 */

package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlRootElement;
import de.lemo.apps.exceptions.EnumValueNotFoundException;

/**
 * States of the connector update process.
 * 
 * @author Boris Wenzlaff
 * @author Leonard Kappe
 */
@XmlRootElement
public enum EConnectorManagerState {
	/**
	 * No connector update is running, the user may start one.
	 */
	READY,
	/**
	 * A connector update is currently running, the user has to wait for it to finish.
	 */
	IN_PROGRESS,
	/**
	 * No connector update is running, there are no connector to update.
	 */
	NO_CONNECTORS,
	/**
	 * The configuration is faulty or missing.
	 */
	CONFIGURATION_ERROR;

	@Override
	public String toString() {
		return this.name();
	};
	
	public static EConnectorManagerState fromValue(String v) throws EnumValueNotFoundException{ 
		if(v!=null) {
			v = v.trim();
			if(v.equals(""))
				return null;
			
			for (EConnectorManagerState c: EConnectorManagerState.values()) {
				if (c.equals(v.toUpperCase())) 
				{ 
					return c; 
				} 
			} 
		}
		//Unable to Parse EConnectorManagerState: 
		throw new EnumValueNotFoundException(); 
	} 
}
