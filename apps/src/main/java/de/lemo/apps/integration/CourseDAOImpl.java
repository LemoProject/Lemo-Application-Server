package de.lemo.apps.integration;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;

public class CourseDAOImpl implements CourseDAO{
	
	@Inject
    private Session session;
    
    @Inject
    private Logger logger;
    
    
    
	public List<Course> findAll() {
		Criteria criteria = session.createCriteria(Course.class);
        List<Course> results = criteria.list();
        if (results.size() == 0) {
                return null;
        }
        return results;
	}

	
	public List<Course> findAllByOwner(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean doExist(Course course){
        Criteria criteria = session.createCriteria(Course.class);
        criteria.add(Restrictions.eq("id", course.getId()));
        List<Course> results = criteria.list();
        if (results.size() == 0) {
                return false;
        }
        return true;
    }
    
    public Course getCourse(String coursename){
        Criteria criteria = session.createCriteria(Course.class);
        criteria.add(Restrictions.eq("coursename", coursename));
        List<Course> results = criteria.list();
        if (results.size() == 0) {
                return null;
        }
        return results.get(0);
    }
    
    public Course getCourse(Long id){
        Criteria criteria = session.createCriteria(Course.class);
        criteria.add(Restrictions.eq("id", id));
        List<Course> results = criteria.list();
        if (results.size() == 0) {
                return null;
        }
        return results.get(0);
    }
    
    
    public void save(Course course) {
        session.persist(course);
    }
    
    public void update(Course course) {
        session.update(course);
    }

	

}
