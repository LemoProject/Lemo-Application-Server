/**
 * File ./src/main/java/de/lemo/apps/services/internal/GenericValueEncoder.java
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
	 * File GenericValueEncoder.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.services.internal;

import java.util.List;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;


public class GenericValueEncoder<T> implements ValueEncoder<T> {

	private PropertyAdapter idFieldAdapter = null;
	private final List<T> list;

	public GenericValueEncoder(final List<T> list, final String idField, final PropertyAccess access) {
		if (((idField != null) && !idField.equalsIgnoreCase("null")) && list.size() > 0) {
			this.idFieldAdapter = access.getAdapter(list.get(0).getClass()).getPropertyAdapter(idField);
		}
		this.list = list;
	}

	@Override
	public String toClient(final T obj) {
		if (this.idFieldAdapter == null) {
			return this.nvl(obj);
		} else {
			return this.nvl(this.idFieldAdapter.get(obj));
		}
	}

	@Override
	public T toValue(final String string) {
		if (this.idFieldAdapter == null) {
			for (final T obj : this.list) {
				if (this.nvl(obj).equals(string)) {
					return obj;
				}
			}
		} else {
			for (final T obj : this.list) {
				if (this.nvl(this.idFieldAdapter.get(obj)).equals(string)) {
					return obj;
				}
			}
		}
		return null;
	}

	private String nvl(final Object o) {
		if (o == null) {
			return "";
		} else {
			return o.toString();
		}
	}
}
