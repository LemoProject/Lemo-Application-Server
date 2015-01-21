/**
 * File ./src/main/java/de/lemo/apps/entities/UsageStatisticsContainer.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

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
