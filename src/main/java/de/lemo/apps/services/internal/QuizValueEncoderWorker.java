/**
 * File ./src/main/java/de/lemo/apps/services/internal/QuizValueEncoderWorker.java
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