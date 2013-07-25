/**
 * File QuizValueEncoder.java
 * Date Apr 15, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.services.internal;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import de.lemo.apps.entities.Quiz;


public class QuizValueEncoderWorker implements QuizValueEncoder { 

	@Inject
	private Logger logger;
	private Map<Long, Quiz> quizMap;

	
	public void setUp(List<Quiz> quizzes)
	{
		this.quizMap = new HashMap<Long, Quiz>();
		for(Quiz q : quizzes)
			this.quizMap.put(q.getCombinedId(), q);
	}
	
    @Override
    public String toClient(Quiz value) {
        return String.valueOf(value.getCombinedId()); 
    }

    @Override
    public Quiz toValue(String id) { 
        // find the quiz object of the given ID 
    	
        return this.quizMap.get(Long.valueOf(id)); 
    }

    // let this ValueEncoder also serve as a ValueEncoderFactory
    @Override
    public ValueEncoder<Quiz> create(Class<Quiz> type) {
        return this; 
    }
} 