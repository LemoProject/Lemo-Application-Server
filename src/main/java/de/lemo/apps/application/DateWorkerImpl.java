/**
 * 
 */
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
 * @author johndoe
 * 
 */
public class DateWorkerImpl implements DateWorker {

    @Inject
    private Messages messages;

    @Inject
    private Logger log;

    private AssetSource assetSource;

    public DateWorkerImpl(Logger logger, AssetSource assetSource) {
        this.assetSource = assetSource;
    }

    public Integer daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        Integer daysBetween = 0;
        while(date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public Integer daysBetween(Date startDate, Date endDate) {

        Calendar _startDate = Calendar.getInstance();
        Calendar _endDate = (Calendar) _startDate.clone();
        _endDate.setTime(endDate);
        _startDate.setTime(startDate);
        Integer daysBetween = 0;
        while(_startDate.before(_endDate)) {
            _startDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    /**
     * Gibt das Datum in der aktuell vom Nutzer gewaehlten Locale Einstellung aus.
     * 
     * @param inputDate
     * @return Ein Objekt vom Typ String
     */
    public String getLocalizedDate(Date date, Locale currentLocale) {
        SimpleDateFormat df_date = new SimpleDateFormat("MMM dd, yyyy", currentLocale);
        return df_date.format(date);
    }

    /**
     * Gibt das Datum und die Uhrzeit in der aktuell vom Nutzer gewaehlten Locale Einstellung aus.
     * 
     * @param inputDate
     * @return Ein Objekt vom Typ String
     */
    public String getLocalizedDateTime(Date date, Locale currentLocale) {
        SimpleDateFormat df_date = new SimpleDateFormat("MMM dd, yyyy (hh:mm)", currentLocale);
        return df_date.format(date);
    }

    public String getCalendarIconPath() {
        return assetSource.getContextAsset("layout/images/glyphicons_045_calendar.png", null).toString();
    }

    public JSONLiteral getDatePickerParams(Locale locale) {
        String monthNamesShort = "";
        try {
            /*
             * Force jquery ui datepicker to use short month names of the current Java version. Datepicker uses the correct
             * new ones, though on the backend it's depending on the Java version. The short name for march in german
             * changed from 'Mrz' to 'Mär' in Java 8 for example. Don't mind the 13th month, datepicker ignores it anyway.
             */
            monthNamesShort = new JSONArray(new java.text.DateFormatSymbols(locale).getShortMonths()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONLiteral datePickerConfig = new JSONLiteral("{" +
                "  nextText: 'Next Month'" +
                ", prevText: 'Previous Month'" +
                ", changeMonth: true" +
                ", changeYear: true" +
                ", buttonText: '" + messages.get("chooseDate") + "'" +
                // ", minDate: '01.07.2012'" +
                // ", maxDate: '"+this.course.getLastRequestDate()+
                ", buttonImage: '" + getCalendarIconPath() + "'" +
                ", buttonImageOnly: false" +
                ", showButtonPanel: true" +
                ", dateFormat: 'M dd, yy'" +
                ", monthNamesShort: " + monthNamesShort +
                ", showOn: 'both'" +
                "}");
        return datePickerConfig;
    }

}
