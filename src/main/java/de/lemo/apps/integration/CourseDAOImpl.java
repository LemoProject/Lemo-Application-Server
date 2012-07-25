package de.lemo.apps.integration;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.restws.entities.CourseObject;

public class CourseDAOImpl implements CourseDAO{
	
	@Inject
    private Session session;
    
    @Inject
    private Logger logger;
    
    
    
	public List<Course> findAll() {
		Criteria criteria = session.createCriteria(Course.class);
        List<Course> results = criteria.list();
        if (results.size() == 0) {
                return new ArrayList<Course>();
        }
        return results;
	}

	
	public List<Course> findAllByOwner(User user) {
		Criteria criteria = session.createCriteria(Course.class);
		criteria.add(Restrictions.in("courseId", user.getMyCourses()));
		List<Course> results = criteria.list();
        if (results.size() == 0) {
                return new ArrayList<Course>();
        }
        return results;
	}
	
	public List<Course> findFavoritesByOwner(User user) {
		Criteria criteria = session.createCriteria(Course.class);
		criteria.add(Restrictions.in("courseId", user.getMyCourses()));
		criteria.add(Restrictions.eq("isFavorite", true));
		List<Course> results = criteria.list();
        if (results.size() == 0) {
                return new ArrayList<Course>();
        }
        return results;
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
	
	public boolean doExistByForeignCourseId(Long courseId){
        Criteria criteria = session.createCriteria(Course.class);
        criteria.add(Restrictions.eq("courseId", courseId));
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
    
    public Course getCourseByDMSId(Long id){
        Criteria criteria = session.createCriteria(Course.class);
        criteria.add(Restrictions.eq("courseId", id));
        List<Course> results = criteria.list();
        if (results.size() == 0) {
                return null;
        }
        return results.get(0);
    }
    
    public void toggleFavorite(Long id){
    	System.out.println("CourseId:" +id);
        Course favCourse = this.getCourse(id);
        Boolean favStatus = favCourse.getIsFavorite();
        System.out.println("FavStatus:" +favStatus);
        if(favStatus==null) {
        	favStatus=true;
        } else favStatus =!favStatus;
        System.out.println("FavStatus2:" +favStatus);
        favCourse.setIsFavorite(favStatus);
        System.out.println("FavStatus3:" +favCourse.getIsFavorite());
        session.update(favCourse);
    }
    
    public void save(Course course) {
        session.persist(course);
    }
    
    public void save(CourseObject courseObject) {
    	Course course = new Course(courseObject);
        session.persist(course);
    }
    
    public void update(Course course) {
        session.update(course);
    }

	

}
