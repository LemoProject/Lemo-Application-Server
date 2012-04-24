package de.lemo.apps.entities;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.tapestry5.ioc.annotations.Inject;

import de.lemo.apps.restws.entities.CourseObject;

@Entity
@Table(name = "course")
public class Course extends AbstractEntity{

	private static final long serialVersionUID = -5156611987477622933L;

	
	private Long courseId;
	private String courseName;
	private String courseDescription;
	private Date lastRequestDate;
	private Date endDate;
	private Long maxParticipants;
	private Long enroledParticipants;
	
	@Inject
	public Course() {}
	
	public Course(CourseObject courseObject){
		this.courseName = courseObject.getTitle();
        this.courseDescription = courseObject.getDescription();
        Timestamp timeStamp = new Timestamp(courseObject.getLastRequest());
        this.lastRequestDate = new java.sql.Date(timeStamp.getTime());
        this.enroledParticipants = courseObject.getParticipants();
        this.courseId=courseObject.getId();
	}
	
	public Course(	final String courseName, final String courseDescription, final Date begin, 
			  		final Date end, final Long maxParticipants, final Long enroledParticipants)
	    {
	        this.courseName = courseName;
	        this.courseDescription = courseDescription;
	        this.lastRequestDate = begin;
	        this.endDate = end;
	        this.maxParticipants = maxParticipants;
	        this.enroledParticipants = enroledParticipants;
	    }
	
	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the courseDescription
	 */
	public String getCourseDescription() {
		return courseDescription;
	}
	/**
	 * @param courseDescription the courseDescription to set
	 */
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	/**
	 * @return the beginDate
	 */
	public Date getLastRequestDate() {
		return lastRequestDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setLastRequestDate(Date lastRequestDate) {
		this.lastRequestDate = lastRequestDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the maxParticipants
	 */
	public Long getMaxParticipants() {
		return maxParticipants;
	}
	/**
	 * @param maxParticipants the maxParticipants to set
	 */
	public void setMaxParticipants(Long maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	/**
	 * @return the enroledParticipants
	 */
	public Long getEnroledParticipants() {
		return enroledParticipants;
	}
	/**
	 * @param enroledParticipants the enroledParticipants to set
	 */
	public void setEnroledParticipants(Long enroledParticipants) {
		this.enroledParticipants = enroledParticipants;
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			return (obj instanceof Course && ((Course) obj).getCourseName().equals(courseName));
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return courseName == null ? 0 : courseName.hashCode();
	}
}
