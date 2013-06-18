package de.lemo.apps.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.apache.tapestry5.beaneditor.DataType;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.annotations.NaturalId;
import de.lemo.apps.application.DateWorkerImpl;
import de.lemo.apps.restws.entities.CourseObject;

/**
 * The course is a domain class which is responsible for ....
 * <p>
 * Based on the basic system structure, next to getter and setter methods there are only a few methods with .... Most of
 * the domainspecific logic is Aufgrund der allgemeinen Systemarchitektur sind neben Getter- und Setter-Methoden nur
 * wenige Methoden direkt in dieser Domainklasse implementiert. Ein Grossteil der Methoden die Klassenuebergreifende
 * Funktionen implementieren sind im Rahmen des Application-Layer als Teil einer Service Klasse *Worker.java
 * implementiert.
 * <p>
 * ACHTUNG: Important methods are commonly defined at the end of the class and are often marked with a @Transient
 * annotation, to indicate that hibernate can ignore these methods
 * <p>
 * Die Klasse wird mittels @Entity Annotation als zu persistierende Klasse gekennzeichnet und darauf hin via Hibernate
 * persistiert. Hierfuer erweitert sie die abstrakte Klasse AbstractEntity, welche uebergeordnete Methoden und Attribute
 * enthaelt, die Hibernate fuer jede zu persistierende Klasse erwartet (z.B. ID, etc.). Das Datenbank-Mapping kann
 * Hibernate fuer Basisdatentypen (String, Integer, Date, etc.) selbst vornehmen. Erst bei komplexen Datentypen bzw.
 * expl. Assoziationen ist eine Auszeichnung durch zusaetzliche Annotationen an den Getter-Methoden der jeweiligen
 * Attribute notwendig. Beispiele hierfuer sind z.B. @OneToOne fuer eine 1:1 Assoziation und @OneToMany fuer eine 1:n
 * Assoziation.
 * <p>
 * 
 * @version 1.0
 * @author Andreas Pursian
 */

@Entity
@Table(name = "course")
public class Course extends AbstractEntity {

	private static final long serialVersionUID = -5156611987477622933L;

	private Long courseId;
	private String courseName;
	private String courseDescription;
	private Date lastRequestDate;
	private Date firstRequestDate;
	private Long maxParticipants;
	private Long enroledParticipants;
	private Boolean needUpdate;
	private Long dataVersion;

	@Inject
	public Course() {
	}

	public Course(final Long courseId) {
		if (courseId != null) {
			this.courseId = courseId;
			this.needUpdate = true;
		}
	}

	public Course(final CourseObject courseObject) {
		if (courseObject != null) {
			updateCourse(courseObject);
		}
	}

	public Course(final String courseName, final String courseDescription, final Date begin,
			final Date end, final Long maxParticipants, final Long enroledParticipants) {
		this.courseName = courseName;
		this.courseDescription = courseDescription;
		this.lastRequestDate = end;
		this.firstRequestDate = begin;
		this.maxParticipants = maxParticipants;
		this.enroledParticipants = enroledParticipants;
		this.needUpdate = false;
	}

	/**
	 * @return the courseId
	 */
	@NaturalId
	@Column(unique = true)
	public Long getCourseId() {
		return this.courseId;
	}

	/**
	 * @param courseId
	 *            the courseId to set
	 */
	public void setCourseId(final Long courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return this.courseName;
	}

	/**
	 * @param courseName
	 *            the courseName to set
	 */
	public void setCourseName(final String courseName) {
		this.courseName = courseName;
	}

	/**
	 * @return the courseDescription
	 */
	public String getCourseDescription() {
		return this.courseDescription;
	}

	/**
	 * @param courseDescription
	 *            the courseDescription to set
	 */
	@DataType(value = "longtext")
	@Lob
	public void setCourseDescription(final String courseDescription) {
		this.courseDescription = courseDescription;
	}

	/**
	 * @return the beginDate
	 */
	public Date getLastRequestDate() {
		return this.lastRequestDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setLastRequestDate(final Date lastRequestDate) {
		this.lastRequestDate = lastRequestDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getFirstRequestDate() {
		return this.firstRequestDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setFirstRequestDate(final Date firstRequestDate) {
		this.firstRequestDate = firstRequestDate;
	}

	/**
	 * @return the maxParticipants
	 */
	public Long getMaxParticipants() {
		return this.maxParticipants;
	}

	/**
	 * @param maxParticipants
	 *            the maxParticipants to set
	 */
	public void setMaxParticipants(final Long maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	/**
	 * @return the enroledParticipants
	 */
	public Long getEnroledParticipants() {
		return this.enroledParticipants;
	}

	/**
	 * @param enroledParticipants
	 *            the enroledParticipants to set
	 */
	public void setEnroledParticipants(final Long enroledParticipants) {
		this.enroledParticipants = enroledParticipants;
	}

	/**
	 * @return the needUpdate
	 */
	public Boolean getNeedUpdate() {
		return needUpdate;
	}

	/**
	 * @param needUpdate
	 *            the needUpdate to set
	 */
	public void setNeedUpdate(Boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	/**
	 * @return the version
	 */
	public Long getDataVersion() {
		return dataVersion;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setDataVersion(Long dataVersion) {
		this.dataVersion = dataVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Course other = (Course) obj;
		if (courseId == null) {
			if (other.courseId != null) {
				return false;
			}
		} else if (!courseId.equals(other.courseId)) {
			return false;
		}
		return true;
	}

	public void updateCourse(CourseObject courseObject) {
		this.courseName = courseObject.getTitle();
		this.courseDescription = courseObject.getDescription();

		if (this.courseName == null && this.courseDescription != null && this.courseDescription.length() > 1) {
			final int maxLength = 35;
			int length = this.courseDescription.length();
			if (length > maxLength) {
				length = maxLength;
			}
			this.courseName = this.courseDescription.substring(0, length - 1);
		}

		if (courseObject.getFirstRequest() != null) {
			this.firstRequestDate = new java.util.Date((long) courseObject.getFirstRequest()
					* DateWorkerImpl.MILLISEC_MULTIPLIER);
		}

		if (courseObject.getLastRequest() != null) {
			this.lastRequestDate = new java.util.Date((long) courseObject.getLastRequest()
					* DateWorkerImpl.MILLISEC_MULTIPLIER);
		}
		this.enroledParticipants = courseObject.getParticipants();
		this.courseId = courseObject.getId();
		this.needUpdate = false;
	}
	
	@Override
	public String toString(){
		
		final StringBuilder builder = new StringBuilder();
		if(this.courseName!=null && !this.courseName.equals("")) builder.append(this.courseName);
		else if(this.courseDescription!=null && !this.courseDescription.equals("")) builder.append(this.courseDescription.substring(0, 7));
		builder.append(" (");
		builder.append(this.courseId);
		builder.append(")");
		return builder.toString();
	}
	
}
