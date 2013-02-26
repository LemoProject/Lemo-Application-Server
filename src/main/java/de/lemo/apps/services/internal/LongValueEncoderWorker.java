/**
	 * File LongValueEncoderWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;

public class LongValueEncoderWorker implements LongValueEncoder {

	@Override
	public String toClient(final Long value) {
		return String.valueOf(value);
	}

	@Override
	public Long toValue(final String id) {
		return Long.parseLong(id);
	}

	@Override
	public ValueEncoder<Long> create(final Class<Long> type) {
		return this;
	}

}