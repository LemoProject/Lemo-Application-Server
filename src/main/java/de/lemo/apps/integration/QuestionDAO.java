/**
	 * File QuestionDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.integration;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.Question;
import de.lemo.apps.exceptions.ObjectNotSavedException;

public interface QuestionDAO {

	@CommitAfter
	Boolean doSave(Question question) throws ObjectNotSavedException;

	Question doGet(Long Id);

}
