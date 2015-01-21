/**
 * File ./src/main/java/de/lemo/apps/services/internal/QuizValueEncoderWorker.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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