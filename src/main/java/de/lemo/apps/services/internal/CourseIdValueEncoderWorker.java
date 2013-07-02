/**
 * File ./src/main/java/de/lemo/apps/services/internal/CourseIdValueEncoderWorker.java
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
	 * File CourseIdValueEncoderWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderFactory;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;


public class CourseIdValueEncoderWorker implements CourseIdValueEncoder {

	
	private CourseDAO courseDAO;

	public CourseIdValueEncoderWorker(CourseDAO courseDAO){
		this.courseDAO = courseDAO;
	}
	
	
	public String toClient(final Course value) {
		return String.valueOf(value.getId());
	}

	
	public Course toValue(final String id) {
		return this.courseDAO.getCourse(Long.parseLong(id));
	}

	public ValueEncoder<Course> create(final Class<Course> arg0) {
		return this;
	}

}