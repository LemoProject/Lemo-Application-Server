
package de.lemo.apps.restws.client;

import java.util.HashMap;
import java.util.Map;
import de.lemo.apps.restws.entities.ResultListLongObject;

/**
 * Helper to converts Maos 
 *
 */
public interface DataHelper {
	
	Map<Long, ResultListLongObject> convertJSONStringToResultListHashMap(String jsonString);

}
