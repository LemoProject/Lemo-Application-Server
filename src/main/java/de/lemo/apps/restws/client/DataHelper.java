
package de.lemo.apps.restws.client;

import java.util.HashMap;
import de.lemo.apps.restws.entities.ResultListLongObject;


public interface DataHelper {
	
	public HashMap<Long, ResultListLongObject> convertJSONStringToResultListHashMap(String jsonString);

}
