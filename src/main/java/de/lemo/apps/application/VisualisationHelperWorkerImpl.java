/**
	 * File VisualisationHelperWorkerImpl.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.ArrayList;
import java.util.List;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.restws.entities.EResourceType;


public class VisualisationHelperWorkerImpl implements VisualisationHelperWorker {
	
	
	public List<Long> getGenderIds(List<GenderEnum> genderList){
		
		ArrayList<Long> gender = null;
		if ((genderList != null) && !genderList.isEmpty()) {
			gender = new ArrayList<Long>();
			for (final GenderEnum gen : genderList) {
				gender.add(gen.value());
			}
		}
		
		return gender;
	}

	
	public List<String> getActivityIds(List<EResourceType> activities) {
		ArrayList<String> types = null;
		if ((activities != null) && !activities.isEmpty()) {
			types = new ArrayList<String>();
			for (final EResourceType resourceType : activities) {
				types.add(resourceType.name().toUpperCase());
			}
		}
		
		return types;
	}
	
}
