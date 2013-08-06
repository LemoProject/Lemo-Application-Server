/**
 * File ./src/main/java/de/lemo/apps/entities/User.java
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
 * File User.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.util.ByteSource;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NaturalId;

/**
 * User object that represents an user in the lemo application
 */
@Entity
@Table(name = "user")
public class User extends AbstractEntity {

	private static final long serialVersionUID = -432098998274596203L;
	
	private static final int COLUMN_LENGTH = 128;

	private List<Course> myCourses = new ArrayList<Course>();
	private List<Course> favoriteCourses = new ArrayList<Course>();
	private List<Widget> myWidgets = new ArrayList<Widget>();

	private String username;
	private String fullname;
	private String email;
	private Date lastLogin;
	private Date currentLogin;

	private boolean accountLocked;
	private boolean credentialsExpired;

	private String encryptedPassword;
	private byte[] passwordSalt;
	private String tempPassword;
	private List<Roles> roles = new ArrayList<Roles>();

	private Long widget1;
	private Long widget2;
	private Long widget3;

	public User() {
	}

	public User(final String fullname, final String username, final String email) {
		this.fullname = fullname;
		this.username = username;
		this.email = email;
	}

	public User(final String fullname, final String username, final String email, final String password) {
		this(fullname, username, email);
		this.setPassword(password);
	}

	public User(final Long id, final String username, 
			final String fullname, final String email, final String password) {
		this.id = id;
		this.username = username;
		this.fullname = fullname;
		this.email = email;
		this.setPassword(password);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("id ");
		builder.append(this.id);
		builder.append(",");
		builder.append("username ");
		builder.append(this.username);
		return builder.toString();
	}

	@Validate("required,regexp=^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z-]+[\\.]{1}[0-9a-zA-Z]+[\\.]?[0-9a-zA-Z]+$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NaturalId
	@Column(unique = true)
	@Index(name = "user_username")
	@Validate("required")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setFullname(final String fullname) {
		this.fullname = fullname;
	}

	@Validate("required")
	public String getFullname() {
		return this.fullname;
	}

	@Transient
	@Validate("required")
	public String getPassword() {
		return "";
	}
	
	@Transient
	@Validate("required")
	public String getPasswordConfirmation() {
		return "";
	}

	public void setPassword(final String newPassword) {
		if (newPassword != null && !newPassword.equals(this.encryptedPassword) && !"".equals(newPassword)) {
			ByteSource saltSource = new SecureRandomNumberGenerator().nextBytes();
			this.passwordSalt = saltSource.getBytes();
			this.encryptedPassword = new Sha1Hash(newPassword, saltSource).toString();
		}
	}
	
	public void setPasswordConf(final String newPassword) {
		if (newPassword != null && !newPassword.equals(this.encryptedPassword) && !"".equals(newPassword)) {
			ByteSource saltSource = new SecureRandomNumberGenerator().nextBytes();
			this.passwordSalt = saltSource.getBytes();
			if(this.tempPassword != null && this.tempPassword.equals(new Sha1Hash(newPassword, saltSource).toString()))
				this.encryptedPassword = new Sha1Hash(newPassword, saltSource).toString();
			else
				this.tempPassword = new Sha1Hash(newPassword, saltSource).toString();
		}
	}

	/**
	 * @return the encryptedPassword
	 */
	@NonVisual
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	/**
	 * @param encryptedPassword
	 *            the encryptedPassword to set
	 */
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	
	@NonVisual
	@Column(length = COLUMN_LENGTH)
	public byte[] getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(byte[] passwordSalt) {
		this.passwordSalt = (byte[])passwordSalt.clone();
	}

	public boolean isAccountLocked() {
		return this.accountLocked;
	}

	public void setAccountLocked(final boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean isCredentialsExpired() {
		return this.credentialsExpired;
	}

	public void setCredentialsExpired(final boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setRoles(final List<Roles> roles) {
		this.roles = roles;
	}

	@Enumerated(EnumType.STRING)
	@ElementCollection
	public List<Roles> getRoles() {
		return this.roles;
	}

	public void setMyCourses(final List<Course> myCourses) {
		this.myCourses = myCourses;
	}

	@NonVisual
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "user_course")
	public List<Course> getMyCourses() {
		return this.myCourses;
	}

	public void setFavoriteCourses(final List<Course> favoriteCourses) {
		this.favoriteCourses = favoriteCourses;
	}

	@NonVisual
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "user_favcourse")
	public List<Course> getFavoriteCourses() {
		return this.favoriteCourses;
	}

	/**
	 * @return the myWidgets
	 */
	@NonVisual
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	@IndexColumn(name = "index_col")
	public List<Widget> getMyWidgets() {
		return myWidgets;
	}

	/**
	 * @param myWidgets
	 *            the myWidgets to set
	 */
	public void setMyWidgets(List<Widget> myWidgets) {
		this.myWidgets = myWidgets;
	}

	@Transient
	@NonVisual
	public List<Long> getMyCourseIds() {
		List<Long> courseIds = new ArrayList<Long>();
		Iterator<Course> it = this.myCourses.iterator();
		while (it.hasNext()) {
			courseIds.add(it.next().getCourseId());
		}
		return courseIds;
	}

	/**
	 * @return the widget1
	 */
	@NonVisual
	public Long getWidget1() {
		return this.widget1;
	}

	/**
	 * @param widget1
	 *            the widget1 to set
	 */
	public void setWidget1(final Long widget1) {
		this.widget1 = widget1;
	}

	/**
	 * @return the widget2
	 */
	@NonVisual
	public Long getWidget2() {
		return this.widget2;
	}

	/**
	 * @param widget2
	 *            the widget2 to set
	 */
	public void setWidget2(final Long widget2) {
		this.widget2 = widget2;
	}

	/**
	 * @return the widget3
	 */
	@NonVisual
	public Long getWidget3() {
		return this.widget3;
	}

	/**
	 * @param widget3
	 *            the widget3 to set
	 */
	public void setWidget3(final Long widget3) {
		this.widget3 = widget3;
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
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@Transient
	public Boolean checkPassword(String password) {
		ByteSource saltSource = ByteSource.Util.bytes(this.getPasswordSalt());
		String givenPassword = new Sha1Hash(password, saltSource).toString();
		if(givenPassword != null && givenPassword.equals(this.encryptedPassword)) {
			return true;
		}
		return false;
	}

	@NonVisual
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@NonVisual
	public Date getCurrentLogin() {
		return currentLogin;
	}

	public void setCurrentLogin(Date currentLogin) {
		this.currentLogin = currentLogin;
	}

}