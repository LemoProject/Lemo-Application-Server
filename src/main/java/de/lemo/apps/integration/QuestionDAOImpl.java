/**
 * File ./de/lemo/apps/integration/QuestionDAOImpl.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
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

public class QuestionDAOImpl implements QuestionDAO {

	@Inject
	private Logger logger;

	@Inject
	private Session session;

	@Override
	public Boolean doSave(final Question question) throws ObjectNotSavedException {
		try {
			// Transaction t = session.beginTransaction();
			this.session.save(question);
			this.session.flush();
			// t.commit();

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
