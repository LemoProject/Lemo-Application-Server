package de.lemo.apps.integration;



import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import de.lemo.apps.entities.Question;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.ObjectNotSavedException;

public class QuestionDAOImpl implements QuestionDAO{
	
	@Inject 
	private Logger logger;
	
	@Inject
	private Session session;
	
	public Boolean doSave(Question question) throws ObjectNotSavedException{
		try{
			//Transaction t = session.beginTransaction();
			session.save(question);
			session.flush();
			//t.commit();
			
		}catch (Exception e){
			logger.warn("Could not save Question!");
			throw new ObjectNotSavedException("QuestionDAO: Could not save Question!");
		}
		
		return true;
	}

	@Override
	public Question doGet(Long id) {
	    Criteria criteria = session.createCriteria(Question.class);
        criteria.add(Restrictions.eq("id", id));
        List<Question> results = criteria.list();
        if (results.size() == 0) {
                return null;
        }
        return results.get(0);
		
	}
	

}
