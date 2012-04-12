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
    
    public boolean doExist(User user){
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("id", user.getId()));
        List<User> results = criteria.list();
        if (results.size() == 0) {
                return false;
        }
        return true;
    }
    
    public User getUser(String username){
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("username", username));
        List<User> results = criteria.list();
        if (results.size() == 0) {
                return null;
        }
        return results.get(0);
    }
    
    
    public void save(User user) {
        session.persist(user);
    }
    
    public void update(User user) {
        session.update(user);
    }
    
    public User login(String username, String password){
    	logger.info("Check login credentials with username "+ username);
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("username", username));
        criteria.add(Restrictions.eq("password", password));
        List<User> results = criteria.list();
        if (results.size() == 0) {
                return null;
        }
        return results.get(0);
    }


}
