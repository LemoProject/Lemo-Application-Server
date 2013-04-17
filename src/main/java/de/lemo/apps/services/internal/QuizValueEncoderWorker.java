/**
 * File QuizValueEncoder.java
 * Date Apr 15, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderFactory;
import de.lemo.apps.entities.Quiz;
import de.lemo.apps.restws.client.Initialisation;


public class QuizValueEncoderWorker implements QuizValueEncoder { 

    @Override
    public String toClient(Quiz value) {
        // return the given object's ID
        return String.valueOf(value.getCombinedId()); 
    }

    @Override
    public Quiz toValue(String id) { 
        // find the quiz object of the given ID 
        return new Quiz(Long.parseLong(id)); 
    }

    // let this ValueEncoder also serve as a ValueEncoderFactory
    @Override
    public ValueEncoder<Quiz> create(Class<Quiz> type) {
        return this; 
    }
} 