package de.lemo.apps.services.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import de.lemo.apps.entities.LearningType;

public class LearningTypeValueEncoderWorker implements LearningTypeValueEncoder {
	
	@Inject
	private Logger logger;
	private Map<Long, LearningType> learningTypeMap;

	
	public void setUp(List<LearningType> learningObjects)
	{
		this.learningTypeMap = new HashMap<Long, LearningType>();
		for(LearningType q : learningObjects)
			this.learningTypeMap.put(q.getId(), q);
	}
	
    @Override
    public String toClient(LearningType value) {
        return String.valueOf(value.getId()); 
    }

    @Override
    public LearningType toValue(String id) { 
    	
        return this.learningTypeMap.get(Long.valueOf(id)); 
    }

    // let this ValueEncoder also serve as a ValueEncoderFactory
    @Override
    public ValueEncoder<LearningType> create(Class<LearningType> type) {
        return this; 
    }
}
