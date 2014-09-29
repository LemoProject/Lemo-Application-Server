package de.lemo.apps.services.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import de.lemo.apps.entities.LearningObject;

public class LearningObjectValueEncoderWorker implements LearningObjectValueEncoder {
	
	@Inject
	private Logger logger;
	private Map<Long, LearningObject> learningMap;

	
	public void setUp(List<LearningObject> learningObjects)
	{
		this.learningMap = new HashMap<Long, LearningObject>();
		for(LearningObject q : learningObjects)
			this.learningMap.put(q.getId(), q);
	}
	
    @Override
    public String toClient(LearningObject value) {
        return String.valueOf(value.getId()); 
    }

    @Override
    public LearningObject toValue(String id) { 
    	
        return this.learningMap.get(Long.valueOf(id)); 
    }

    // let this ValueEncoder also serve as a ValueEncoderFactory
    @Override
    public ValueEncoder<LearningObject> create(Class<LearningObject> type) {
        return this; 
    }
}
