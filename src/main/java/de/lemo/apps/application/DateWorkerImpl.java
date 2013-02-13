package de.lemo.apps.application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.services.AssetSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;

/**
 * @author Andreas Pursian
 */
public class DateWorkerImpl implements DateWorker {

	@Inject
	private Messages messages;

	@Inject
	private Logger log;

	private final AssetSource assetSource;

	/**
	 * Defining all constants used within this class
	 */
	public static final String DATE_FORMAT = "MMM dd, yyyy";
	public static final String CALENDAR_ICON_PATH = "layout/images/glyphicons_045_calendar.png";

	public DateWorkerImpl(final Logger logger, final AssetSource assetSource) {
		this.assetSource = assetSource;
	}

	public Integer daysBetween(final Calendar startDate, final Calendar endDate) {
		final Calendar date = (Calendar) startDate.clone();
		Integer daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	public Integer daysBetween(final Date startDate, final Date endDate) {

		final Calendar startDateTemp = Calendar.getInstance();
		final Calendar endDateTemp = (Calendar) startDateTemp.clone();
		endDateTemp.setTime(endDate);
		startDateTemp.setTime(startDate);
		Integer daysBetween = 0;
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
		final SimpleDateFormat df_date = new SimpleDateFormat(DateWorkerImpl.DATE_FORMAT, currentLocale);
		return df_date.format(date);
	}

	/**
	 * Gibt das Datum und die Uhrzeit in der aktuell vom Nutzer gewaehlten Locale Einstellung aus.
	 * 
	 * @param the
	 *            Date which should be localized, the current locale
	 * @return A String object in Date + Time notation
	 */
	public String getLocalizedDateTime(final Date date, final Locale currentLocale) {
		final SimpleDateFormat df_date = new SimpleDateFormat(DateWorkerImpl.DATE_FORMAT, currentLocale);
		return df_date.format(date);
	}

	public String getCalendarIconPath() {
		return this.assetSource.getContextAsset(DateWorkerImpl.CALENDAR_ICON_PATH, null).toString();
	}

	public JSONLiteral getDatePickerParams(final Locale locale) {
		String monthNamesShort = "";
		try {
			/*
			 * Force jquery ui datepicker to use short month names of the current Java version. Datepicker uses the
			 * correct
			 * new ones, though on the backend it's depending on the Java version. The short name for march in german
			 * changed from 'Mrz' to 'MŠr' in Java 8 for example. Don't mind the 13th month, datepicker ignores it
			 * anyway.
			 */
			monthNamesShort = new JSONArray(new java.text.DateFormatSymbols(locale).getShortMonths()).toString();
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		final JSONLiteral datePickerConfig = new JSONLiteral("{" +
				"  nextText: 'Next Month'" +
				", prevText: 'Previous Month'" +
				", changeMonth: true" +
				", changeYear: true" +
				", buttonText: '" + this.messages.get("chooseDate") + "'" +
				// ", minDate: '01.07.2012'" +
				// ", maxDate: '"+this.course.getLastRequestDate()+
				", buttonImage: '" + this.getCalendarIconPath() + "'" +
				", buttonImageOnly: false" +
				", showButtonPanel: true" +
				", dateFormat: 'M dd, yy'" +
				", monthNamesShort: " + monthNamesShort +
				", showOn: 'both'" +
				"}");
		return datePickerConfig;
	}

}
