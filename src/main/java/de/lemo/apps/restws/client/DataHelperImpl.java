/**
 * File ./src/main/java/de/lemo/apps/restws/client/DataHelperImpl.java
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


package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import de.lemo.apps.restws.entities.ResultListLongObject;

/**
 * Helper to converts Maps and other JSON data formats 
 */
public class DataHelperImpl implements DataHelper{
	
	@Inject
	private Logger logger;
	
	public 
	Map<Long, ResultListLongObject> convertJSONStringToResultListHashMap(String jsonString){
		
		
		HashMap<Long, ResultListLongObject> resultList = new HashMap<Long, ResultListLongObject>();

		try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonObj = mapper.readTree(jsonString);
				
				JsonNode entries = jsonObj.get("entries");
				JsonNode keys = jsonObj.get("keys");
				
				
				if ((entries != null) && (keys != null)) {
					this.logger.debug("Entries: " + jsonObj.get("entries").toString());
					// if more than one course result is returned
					if (entries.isArray() && keys.isArray()) {
						final Iterator<JsonNode> kit = keys.getElements();
						final Iterator<JsonNode> eit = entries.getElements();
						List<Long> entryList = new ArrayList<Long>();
						while (kit.hasNext()) {
							final Long keyValue = Long.parseLong(kit.next().getValueAsText());
							final JsonNode entryNodes = eit.next();
							final JsonNode entryNodesArray = entryNodes.get("elements");
							if (entryNodesArray.isArray()) {
								this.logger.debug("Entries is Array ....");
								entryList = new ArrayList<Long>();
								final Iterator<JsonNode> enit = entryNodesArray.getElements();
								while (enit.hasNext()) {
									final Long enitValue = enit.next().getValueAsLong(0L);
									entryList.add(enitValue);
								}
							} else {
								this.logger.debug("Entries is NO Array ....");
							}
							ResultListLongObject entryValues = new ResultListLongObject(entryList);
							resultList.put(keyValue, entryValues);
		
							this.logger.debug("Course Key: " + keyValue);
						}
						// if only one course result is returned
					} else {
						final Long keyValue = Long.parseLong(keys.getValueAsText());
						final JsonNode entryNodesArray = entries.get("elements");
						List<Long> entryList = new ArrayList<Long>();
		
						if (entryNodesArray.isArray()) {
							this.logger.debug("Entries is Array ....");
							final Iterator<JsonNode> enit = entryNodesArray.getElements();
							while (enit.hasNext()) {
								Long enitValue = enit.next().getValueAsLong(0L);
								entryList.add(enitValue);
							}
						} else {
							this.logger.debug("Entries is NO Array ....");
						}
						final ResultListLongObject entryValues = new ResultListLongObject(entryList);
						resultList.put(keyValue, entryValues);
		
						this.logger.debug("Course Key: " + keyValue);
		
					}
				} else {
					this.logger.debug("Entries not found!");
				}
				
		} catch (JsonProcessingException e) {
			logger.error("Could not parse JSON String. "+ e.getMessage());
		} catch (IOException e) {
			logger.error("Could not parse JSON String. "+ e.getMessage());
		}
		return resultList;
	}

}
