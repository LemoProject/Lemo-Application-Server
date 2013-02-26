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
import org.apache.tapestry5.json.JSONArray;

/**
 * inspired from chenillekit project author mlusetti
 */
public class TextValueDataItem implements Serializable, DataJqPlotSerializer {

	/**
*
*/
	private static final long serialVersionUID = -2264289557350915525L;

	/**
	 * The text
	 */
	private String text;

	/**
	 * The value.
	 */
	private Number value;

	/**
	 * Constructs a new data item.
	 * 
	 * @param text
	 *            the label.
	 * @param value
	 *            the value.
	 */
	public TextValueDataItem(final String text, final Number value) {
		assert text != null;
		assert value != null;

		this.text = text;
		this.value = value;
	}

	/**
	 * Returns the text.
	 * 
	 * @return The texr.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Returns the value.
	 * 
	 * @return The value.
	 */
	public Number getValue() {
		return this.value;
	}

	/**
	 * Sets the value for this data item.
	 * 
	 * @param value
	 *            the new value.
	 */
	public void setValue(final Number value) {
		assert value != null;
		this.value = value;
	}

	/**
	 * Sets the text for this data item.
	 * 
	 * @param text
	 *            the new text.
	 */
	public void setXValue(final String text) {
		assert text != null;
		this.text = text;
	}

	/**
	 * Returns a hash code.
	 * 
	 * @return A hash code.
	 */
	@Override
	public int hashCode() {
		int result;
		result = (this.text != null ? this.text.hashCode() : 0);
		result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return String.format("[%s,%s]", this.text.toString(), this.value.toString());
	}

	public JSONArray toJSONArray() {
		return new JSONArray(this.text, this.value);
	}
}