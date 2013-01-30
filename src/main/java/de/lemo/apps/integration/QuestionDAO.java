package de.lemo.apps.integration;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.Question;
import de.lemo.apps.exceptions.ObjectNotSavedException;

public interface QuestionDAO {

	@CommitAfter
	public Boolean doSave(Question question) throws ObjectNotSavedException;

	public Question doGet(Long Id);

}
