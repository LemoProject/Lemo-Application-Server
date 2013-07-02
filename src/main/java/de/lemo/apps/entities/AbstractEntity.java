/**
 * File ./src/main/java/de/lemo/apps/entities/AbstractEntity.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
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

/**
 * File AbstractEntity.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import org.apache.tapestry5.beaneditor.NonVisual;

@MappedSuperclass
public class AbstractEntity implements Serializable {

	protected static final long serialVersionUID = -5269831219220124237L;
	protected long id;
	protected long version;
	protected Date created;
	protected Date modified;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NonVisual
	public Long getId() {
		return this.id;
	}

	@NonVisual
	public void setId(final long id) {
		this.id = id;
	}

	@Version
	@Column(name = "OPTLOCK")
	public Long getVersion() {
		return this.version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@NonVisual
	public void setVersion(final Long version) {
		this.version = version;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	@NonVisual
	public void setCreated(final Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return this.modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	@NonVisual
	public void setModified(final Date modified) {
		this.modified = modified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final AbstractEntity other = (AbstractEntity) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

}