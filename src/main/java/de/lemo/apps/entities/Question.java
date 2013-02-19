/**
 	 * File Question.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "question")
@Table(name = "Question")
public class Question extends AbstractEntity {

	
	private static final long serialVersionUID = 1402869189061923941L;

	private String name;

	private String question;

	@NotNull(message = "name can't be null")
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return this.question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(final String question) {
		this.question = question;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}

		final Question that = (Question) o;

		return this.getId() != null ? this.getId().equals(that.getId()) : that.getId() == null;
	}

	@Override
	public int hashCode() {
		return (this.getId() != null ? this.getId().hashCode() : 0);
	}

	@Override
	public String toString() {
		return this.getName();
	}
}