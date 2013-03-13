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
