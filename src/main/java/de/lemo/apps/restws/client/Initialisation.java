/**
 * File ./de/lemo/apps/restws/client/Initialisation.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.client;

import java.util.Date;
import java.util.List;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.ResultListStringObject;

public interface Initialisation {

	public Date getStartTime();

	public CourseObject getCourseDetails(Long id);

	public ResultListCourseObject getCoursesDetails(List<Long> ids);

	public ResultListStringObject getRatedObjects(List<Long> courseIds);

}
