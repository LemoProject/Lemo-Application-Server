package de.lemo.apps.entities;

import java.util.Date;

public class UsageStatisticsContainer {

	private Long courseId;

	private Long averageRequest;

	private Long overallRequest;

	private Long maxRequest;

	private Date maxRequestDate;

	public UsageStatisticsContainer(final Long courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the courseId
	 */
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
	 * @return the averageRequest
	 */
	public Long getAverageRequest() {
		return this.averageRequest;
	}

	/**
	 * @param averageRequest
	 *            the averageRequest to set
	 */
	public void setAverageRequest(final Long averageRequest) {
		this.averageRequest = averageRequest;
	}

	/**
	 * @return the overallRequest
	 */
	public Long getOverallRequest() {
		return this.overallRequest;
	}

	/**
	 * @param overallRequest
	 *            the overallRequest to set
	 */
	public void setOverallRequest(final Long overallRequest) {
		this.overallRequest = overallRequest;
	}

	/**
	 * @return the maxRequest
	 */
	public Long getMaxRequest() {
		return this.maxRequest;
	}

	/**
	 * @param maxRequest
	 *            the maxRequest to set
	 */
	public void setMaxRequest(final Long maxRequest) {
		this.maxRequest = maxRequest;
	}

	/**
	 * @return the maxRequestDate
	 */
	public Date getMaxRequestDate() {
		return this.maxRequestDate;
	}

	/**
	 * @param maxRequestDate
	 *            the maxRequestDate to set
	 */
	public void setMaxRequestDate(final Date maxRequestDate) {
		this.maxRequestDate = maxRequestDate;
	}

}
