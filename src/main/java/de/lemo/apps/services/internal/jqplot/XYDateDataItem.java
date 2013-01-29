/**
 * File ./de/lemo/apps/services/internal/jqplot/XYDateDataItem.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.services.internal.jqplot;

/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 * 
 * Copyright 2008 by chenillekit.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Created By Got5 - https://github.com/got5/tapestry5-jqPlot
 */

import java.io.Serializable;
import java.util.Date;
import org.apache.tapestry5.json.JSONArray;

/**
 * based on chenillekit impl from mlusetti
 */
public class XYDateDataItem implements Serializable, Comparable, DataJqPlotSerializer
{

	/**
*
*/
	private static final long serialVersionUID = 8668938983096465657L;

	/**
	 * The x-value.
	 */
	private Date _xValue;

	/**
	 * The y-value.
	 */
	private Number _yValue;

	/**
	 * Constructs a new data item.
	 * 
	 * @param xValue
	 *            the x-value.
	 * @param yValue
	 *            the y-value.
	 */
	public XYDateDataItem(final Date xValue, final Number yValue)
	{
		assert xValue != null;
		assert yValue != null;

		this._xValue = xValue;
		this._yValue = yValue;
	}

	/**
	 * Returns the x-value.
	 * 
	 * @return The x-value.
	 */
	public Date getXValue()
	{
		return this._xValue;
	}

	/**
	 * Returns the y-value.
	 * 
	 * @return The y-value.
	 */
	public Number getYValue()
	{
		return this._yValue;
	}

	/**
	 * Sets the y-value for this data item.
	 * 
	 * @param yValue
	 *            the new y-value.
	 */
	public void setYValue(final Number yValue)
	{
		assert yValue != null;
		this._yValue = yValue;
	}

	/**
	 * Sets the x-value for this data item.
	 * 
	 * @param xValue
	 *            the new x-value.
	 */
	public void setXValue(final Date xValue)
	{
		assert xValue != null;
		this._xValue = xValue;
	}

	/**
	 * Tests if this object is equal to another.
	 * 
	 * @param obj
	 *            the object to test against for equality (<code>null</code> permitted).
	 * @return A boolean.
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof XYDateDataItem)) {
			return false;
		}

		final XYDateDataItem that = (XYDateDataItem) obj;

		if (this._xValue != null ? !this._xValue.equals(that._xValue) : that._xValue != null) {
			return false;
		}
		if (this._yValue != null ? !this._yValue.equals(that._yValue) : that._yValue != null) {
			return false;
		}

		return true;
	}

	/**
	 * Returns a hash code.
	 * 
	 * @return A hash code.
	 */
	@Override
	public int hashCode()
	{
		int result;
		result = (this._xValue != null ? this._xValue.hashCode() : 0);
		result = (31 * result) + (this._yValue != null ? this._yValue.hashCode() : 0);
		return result;
	}

	/**
	 * Returns an integer indicating the order of this object relative to
	 * another object.
	 * <p/>
	 * For the order we consider only the x-value: negative == "less-than", zero == "equal", positive == "greater-than".
	 * 
	 * @param o1
	 *            the object being compared to.
	 * @return An integer indicating the order of this data pair object
	 *         relative to another object.
	 */
	@Override
	public int compareTo(final Object o1)
	{

		int result;

		// CASE 1 : Comparing to another TimeSeriesDataPair object
		// -------------------------------------------------------
		if (o1 instanceof XYDateDataItem)
		{
			final XYDateDataItem dataItem = (XYDateDataItem) o1;
			// double compare = _xValue.doubleValue() - dataItem.getXValue().doubleValue();
			// if (compare > 0.0)
			// result = 1;
			// else
			// {
			// if (compare < 0.0)
			// result = -1;
			// else
			// result = 0;
			// }
			return this._xValue.compareTo(dataItem.getXValue());
		} else {
			result = 1;
		}

		return result;
	}

	@Override
	public String toString()
	{
		return String.format("[%s,%s]", this._xValue.toString(), this._yValue.toString());
	}

	@Override
	public JSONArray toJSONArray() {
		// +(new Integer(_xValue.getYear())).toString()
		// String dateString = "2011"+"-"+(new Integer(_xValue.getMonth())).toString()+"-"+(new
		// Integer(_xValue.getDay())).toString();
		// Timestamp ts = new Timestamp(_xValue.getTime());
		return new JSONArray(this._xValue.getTime(), this._yValue);
	}
}