package de.lemo.apps.entities;

import java.util.Date;

public class UsageStatisticsContainer {
	
	private Long courseId;
	
	private Long averageRequest;
	
	private Long OverallRequest;
	
	private Long MaxRequest;
	
	private Date MaxRequestDate;
	
	public UsageStatisticsContainer(Long courseId){
		this.courseId = courseId;
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
	 * @return the averageRequest
	 */
	public Long getAverageRequest() {
		return averageRequest;
	}

	/**
	 * @param averageRequest the averageRequest to set
	 */
	public void setAverageRequest(Long averageRequest) {
		this.averageRequest = averageRequest;
	}

	/**
	 * @return the overallRequest
	 */
	public Long getOverallRequest() {
		return OverallRequest;
	}

	/**
	 * @param overallRequest the overallRequest to set
	 */
	public void setOverallRequest(Long overallRequest) {
		OverallRequest = overallRequest;
	}

	/**
	 * @return the maxRequest
	 */
	public Long getMaxRequest() {
		return MaxRequest;
	}

	/**
	 * @param maxRequest the maxRequest to set
	 */
	public void setMaxRequest(Long maxRequest) {
		MaxRequest = maxRequest;
	}

	/**
	 * @return the maxRequestDate
	 */
	public Date getMaxRequestDate() {
		return MaxRequestDate;
	}

	/**
	 * @param maxRequestDate the maxRequestDate to set
	 */
	public void setMaxRequestDate(Date maxRequestDate) {
		MaxRequestDate = maxRequestDate;
	}
	

}
