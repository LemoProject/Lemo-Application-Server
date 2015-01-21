/**
 * File ./src/main/java/de/lemo/apps/application/DateWorker.java
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
	 * File DateWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.tapestry5.json.JSONLiteral;

/**
 * Interface for helper class for date operations
 * @author Boris Wenzlaff
 *
 */
public interface DateWorker {

	int daysBetween(Calendar startDate, Calendar endDate);

	int daysBetween(Date startDate, Date endDate);

	String getLocalizedDate(Date date, Locale locale);

	String getLocalizedDateTime(Date date, Locale locale);

	JSONLiteral getDatePickerParams(Locale locale);

}
