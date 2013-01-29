/**
 * File ./de/lemo/apps/services/internal/GenericValueEncoder.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.services.internal;

import java.util.List;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;

/**
 * @author johndoe
 */
public class GenericValueEncoder<T> implements ValueEncoder<T> {

	private PropertyAdapter idFieldAdapter = null;
	private final List<T> list;

	public GenericValueEncoder(final List<T> list, final String idField, final PropertyAccess access) {
		if ((idField != null) && !idField.equalsIgnoreCase("null")) {
			if (list.size() > 0) {
				this.idFieldAdapter = access.getAdapter(list.get(0).getClass()).getPropertyAdapter(idField);
			}
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
