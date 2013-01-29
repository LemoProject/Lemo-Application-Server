/**
 * File ./de/lemo/apps/integration/UserDAOImpl.java
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
import de.lemo.apps.entities.User;

public class UserDAOImpl implements UserDAO {

	@Inject
	private Session session;

	@Inject
	private Logger logger;

	@Override
	public boolean doExist(final User user) {
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("id", user.getId()));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public User getUser(final String username) {
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	@Override
	public void save(final User user) {
		this.session.persist(user);
	}

	@Override
	public void update(final User user) {
		this.session.update(user);
	}

	@Override
	public User login(final String username, final String password) {
		this.logger.info("Check login credentials with username " + username);
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("password", password));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

}
