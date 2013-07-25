
package de.lemo.apps.services.internal;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;
import de.lemo.apps.entities.Quiz;


public interface QuizValueEncoder extends ValueEncoder<Quiz>, ValueEncoderFactory<Quiz>{

	
	public void setUp(List<Quiz> quizzes);
}
