/**
 * File ./src/main/java/de/lemo/apps/restws/entities/SCTime.java
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

package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * service container for time formats as long
 * 
 * @author Boris Wenzlaff
 */
@XmlRootElement
public class SCTime {

	private long time;

	public long getTime() {
		return this.time;
	}

	public void setTime(final long time) {
		this.time = time;
	}

}
