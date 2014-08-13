package de.lemo.apps.services.internal;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;

import de.lemo.apps.entities.LearningObject;

public interface LearningObjectValueEncoder extends ValueEncoder<LearningObject>, ValueEncoderFactory<LearningObject>{
	
	public void setUp(List<LearningObject> learningObjects);

}
