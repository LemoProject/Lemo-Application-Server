/**
	 * File VisualisationHelperWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.List;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.restws.entities.EResourceType;


public interface VisualisationHelperWorker {
	
	List<Long> getGenderIds(List<GenderEnum> genderList);
	
	List<String> getActivityIds(List<EResourceType> activities);

}
