package de.lemo.apps.services.internal.jqplot;

/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 * 
 * Copyright 2008 by chenillekit.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
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
public class XYDateDataItem implements Serializable, Comparable, DataJqPlotSerializer {

	/**
*
*/
	private static final long serialVersionUID = 8668938983096465657L;

	/**
	 * The x-value.
	 */
	private Date xValue;

	/**
	 * The y-value.
	 */
	private Number yValue;

	/**
	 * Constructs a new data item.
	 * 
	 * @param xValue
	 *            the x-value.
	 * @param yValue
	 *            the y-value.
	 */
	public XYDateDataItem(final Date xValue, final Number yValue) {
		assert xValue != null;
		assert yValue != null;

		this.xValue = xValue;
		this.yValue = yValue;
	}

	/**
	 * Returns the x-value.
	 * 
	 * @return The x-value.
	 */
	public Date getXValue() {
		return this.xValue;
	}

	/**
	 * Returns the y-value.
	 * 
	 * @return The y-value.
	 */
	public Number getYValue() {
		return this.yValue;
	}

	/**
	 * Sets the y-value for this data item.
	 * 
	 * @param yValue
	 *            the new y-value.
	 */
	public void setYValue(final Number yValue) {
		assert yValue != null;
		this.yValue = yValue;
	}

	/**
	 * Sets the x-value for this data item.
	 * 
	 * @param xValue
	 *            the new x-value.
	 */
	public void setXValue(final Date xValue) {
		assert xValue != null;
		this.xValue = xValue;
	}

	/**
	 * Tests if this object is equal to another.
	 * 
	 * @param obj
	 *            the object to test against for equality (<code>null</code> permitted).
	 * @return A boolean.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof XYDateDataItem)) {
			return false;
		}

		final XYDateDataItem that = (XYDateDataItem) obj;

		if (this.xValue != null ? !this.xValue.equals(that.xValue) : that.xValue != null) {
			return false;
		}
		if (this.yValue != null ? !this.yValue.equals(that.yValue) : that.yValue != null) {
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
	public int hashCode() {
		int result;
		result = (this.xValue != null ? this.xValue.hashCode() : 0);
		result = 31 * result + (this.yValue != null ? this.yValue.hashCode() : 0);
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
	public int compareTo(final Object o1) {

		int result;

		// CASE 1 : Comparing to another TimeSeriesDataPair object
		// -------------------------------------------------------
		if (o1 instanceof XYDateDataItem) {
			final XYDateDataItem dataItem = (XYDateDataItem) o1;
			return this.xValue.compareTo(dataItem.getXValue());
		} else {
			result = 1;
		}

		return result;
	}

	@Override
	public String toString() {
		return String.format("[%s,%s]", this.xValue.toString(), this.yValue.toString());
	}

	public JSONArray toJSONArray() {
		return new JSONArray(this.xValue.getTime(), this.yValue);
	}
}