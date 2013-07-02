/**
 * File ./src/main/java/de/lemo/apps/services/internal/MysqlDialectUtf8.java
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
	 * File LongValueEncoder.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * 
	 * adding custom MySQL Dialect with UTF-8 as default charset and InnoDB table engine
	 */
package de.lemo.apps.services.internal;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * Static String with SQL dialect 
 *
 */
public class MysqlDialectUtf8 extends MySQL5InnoDBDialect {

	@Override
	public String getTableTypeString() {
		return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}
}
