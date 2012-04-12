package de.lemo.apps.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.tapestry5.ioc.annotations.Inject;

@Entity
@Table(name = "course")
public class Course extends AbstractEntity{

	private static final long serialVersionUID = -5156611987477622933L;

	
	private String courseName;
	private String courseDescription;
	private Date beginDate;
	private Date endDate;
	private Integer maxParticipants;
	private Integer enroledParticipants;
	
	@Inject
	public Course()
    {}
	
	public Course(	final String courseName, final String courseDescription, final Date begin, 
			  		final Date end, final Integer maxParticipants, final Integer enroledParticipants)
	    {
	        this.courseName = courseName;
	        this.courseDescription = courseDescription;
	        this.beginDate = begin;
	        this.endDate = end;
	        this.maxParticipants = maxParticipants;
	        this.enroledParticipants = enroledParticipants;
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
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
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
	public Integer getMaxParticipants() {
		return maxParticipants;
	}
	/**
	 * @param maxParticipants the maxParticipants to set
	 */
	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	/**
	 * @return the enroledParticipants
	 */
	public Integer getEnroledParticipants() {
		return enroledParticipants;
	}
	/**
	 * @param enroledParticipants the enroledParticipants to set
	 */
	public void setEnroledParticipants(Integer enroledParticipants) {
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
