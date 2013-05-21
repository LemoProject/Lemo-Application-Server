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
		return this.name().toLowerCase();
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
		System.out.println("Unable to Parse EConnectorManagerState: "+v);
		throw new EnumValueNotFoundException(); 
	} 
}
