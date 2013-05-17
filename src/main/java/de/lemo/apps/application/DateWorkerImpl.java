/**
 * File DateWorkerImpl.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;

public class DateWorkerImpl implements DateWorker {

	@Inject
	private Logger logger;

	@Inject
	private Messages messages;

	/**
	 * Defining all constants used within this class
	 */
	public static final String DATE_FORMAT = "MMM dd, yyyy";
	public static final int MILLISEC_MULTIPLIER = 1000;

	@Inject
	@Path("de/lemo/apps/images/icons/glyphicons_045_calendar.png")
	private Asset calendarIcon;

	public int daysBetween(final Calendar startDate, final Calendar endDate) {
		// TODO a loop seems inefficient for this. Maybe use joda time, it has a `daysBetween` method.
		// Is the the result correct? - We get '1 day' even if the dates are only seconds apart.
		final Calendar date = (Calendar) startDate.clone();
		int daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	public int daysBetween(final Date startDate, final Date endDate) {
		// TODO see daysBetween(Calendar, Calendar)
		final Calendar startDateTemp = Calendar.getInstance();
		final Calendar endDateTemp = (Calendar) startDateTemp.clone();
		endDateTemp.setTime(endDate);
		startDateTemp.setTime(startDate);
		int daysBetween = 0;
		while (startDateTemp.before(endDateTemp)) {
			startDateTemp.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	/**
	 * Gibt das Datum in der aktuell vom Nutzer gewaehlten Locale Einstellung aus.
	 * 
	 * @param inputDate
	 * @return A String object in date notation
	 */
	public String getLocalizedDate(final Date date, final Locale currentLocale) {
		final SimpleDateFormat dfDate = new SimpleDateFormat(DateWorkerImpl.DATE_FORMAT, currentLocale);
		return dfDate.format(date);
	}

	/**
	 * Gibt das Datum und die Uhrzeit in der aktuell vom Nutzer gewaehlten Locale Einstellung aus.
	 * 
	 * @param the
	 *            Date which should be localized, the current locale
	 * @return A String object in Date + Time notation
	 */
	public String getLocalizedDateTime(final Date date, final Locale currentLocale) {
		final SimpleDateFormat dfDate = new SimpleDateFormat(DateWorkerImpl.DATE_FORMAT, currentLocale);
		return dfDate.format(date);
	}

	public JSONLiteral getDatePickerParams(final Locale locale) {
		String monthNamesShort = "";
		try {
			/*
			 * Force jquery ui datepicker to use short month names of the current Java version. Datepicker uses the
			 * correct
			 * new ones, though on the backend it's depending on the Java version. The short name for march in german
			 * changed from 'Mrz' to 'Mär' in Java 8 for example. Don't mind the 13th month, datepicker ignores it
			 * anyway.
			 */
			monthNamesShort = new JSONArray(new java.text.DateFormatSymbols(locale).getShortMonths()).toString();
		} catch (final JSONException e) {
			logger.error(e.getMessage());
		}
		final JSONLiteral datePickerConfig = new JSONLiteral("{" +
				"  nextText: 'Next Month'" +
				", prevText: 'Previous Month'" +
				", changeMonth: true" +
				", changeYear: true" +
				", buttonText: '" + this.messages.get("chooseDate") + "'" +
				// ", minDate: '01.07.2012'" +
				", maxDate: '0'"+
				", buttonImage: '" + this.calendarIcon.toClientURL() + "'" +
				", buttonImageOnly: false" +
				", showButtonPanel: true" +
				", dateFormat: '"+ this.messages.get("customDateFormatDatePicker") +"'"+
				", monthNamesShort: " + monthNamesShort +
				", showOn: 'both'" +
				"}");
		return datePickerConfig;
	}

}
