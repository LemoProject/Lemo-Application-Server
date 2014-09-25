package de.lemo.apps.services.internal;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;

import de.lemo.apps.entities.LearningType;

public interface LearningTypeValueEncoder extends ValueEncoder<LearningType>, ValueEncoderFactory<LearningType>{
	
	public void setUp(List<LearningType> learningObjects);

}
