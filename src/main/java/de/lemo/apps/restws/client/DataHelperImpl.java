
package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import de.lemo.apps.restws.entities.ResultListLongObject;


public class DataHelperImpl implements DataHelper{
	
	@Inject
	private Logger logger;
	
	public HashMap<Long, ResultListLongObject> convertJSONStringToResultListHashMap(String jsonString){
		
		
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
							// logger.debug("EntryNodes Complete:"+entryNodesArray);
							if (entryNodesArray.isArray()) {
								this.logger.debug("Entries is Array ....");
								entryList = new ArrayList<Long>();
								final Iterator<JsonNode> enit = entryNodesArray.getElements();
								while (enit.hasNext()) {
									final Long enitValue = enit.next().getValueAsLong(0L);
									// logger.debug("Entry Values: "+enitValue);
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
						// logger.debug("EntryNodes :"+entryNodesArray);
		
						if (entryNodesArray.isArray()) {
							this.logger.debug("Entries is Array ....");
							final Iterator<JsonNode> enit = entryNodesArray.getElements();
							while (enit.hasNext()) {
								Long enitValue = enit.next().getValueAsLong(0L);
								// logger.debug("Entry Values: "+enitValue);
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
