/**
	 * File QuestionDAOImplDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.integration;

import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import de.lemo.apps.entities.Question;
import de.lemo.apps.exceptions.ObjectNotSavedException;

/**
 * implementation of the questionDao interface
 */
public class QuestionDAOImpl implements QuestionDAO {

	@Inject
	private Logger logger;

	@Inject
	private Session session;

	public Boolean doSave(final Question question) throws ObjectNotSavedException {
		try {
			
			this.session.save(question);
			this.session.flush();
			

		} catch (final Exception e) {
			this.logger.warn("Could not save Question!");
			throw new ObjectNotSavedException("QuestionDAO: Could not save Question!");
		}

		return true;
	}

	@Override
	public Question doGet(final Long id) {
		final Criteria criteria = this.session.createCriteria(Question.class);
		criteria.add(Restrictions.eq("id", id));
		final List<Question> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);

	}

}
