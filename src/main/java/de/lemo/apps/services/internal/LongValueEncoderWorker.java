package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;

public class LongValueEncoderWorker implements LongValueEncoder {

    @Override
    public String toClient(Long value) {
        return String.valueOf(value);
    }

    @Override
    public Long toValue(String id) {
        return Long.parseLong(id);
    }

    @Override
    public ValueEncoder<Long> create(Class<Long> type) {
        return this;
    }

}