/**
 * File User.java
 * Date Feb 14, 2013
 * Copyright TODO (INSERT COPYRIGHT)
 */
package de.lemo.apps.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "User")
public class User extends AbstractEntity {

	private static final long serialVersionUID = -432098998274596203L;

	private List<Course> myCourses = new ArrayList<Course>();

	private List<Course> favoriteCourses = new ArrayList<Course>();

	private List<Widget> myWidgets = new ArrayList<Widget>();

	// @NaturalId
	// @Column(nullable = false, unique = true)
	// @NotNull
	// @Size(min = 3, max = 15)
	private String username;

	// @Column(nullable = false)
	// @NotNull
	// @Size(min = 3, max = 50)
	private String fullname;

	// @Column(nullable = false)
	// @NotNull
	// @Email
	private String email;

	private String encryptedPassword;

	private boolean accountLocked;

	private boolean credentialsExpired;

	private Set<Roles> roles = new HashSet<Roles>();

	private byte[] passwordSalt;

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

	public User(final Long id, final String username, final String fullname, final String email, final String password) {
		super();
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

	@Validate("required,regexp=^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z]+[\\.]{1}[0-9a-zA-Z]+[\\.]?[0-9a-zA-Z]+$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NaturalId
	@Column(unique = true)
	@Index(name = "User_username")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setFullname(final String fullname) {
		this.fullname = fullname;
	}

	public String getFullname() {
		return this.fullname;
	}

	@Transient
	public String getPassword() {

		return "";
	}

	public void setPassword(final String newPassword) {
		if (newPassword != null && !newPassword.equals(this.encryptedPassword) && !"".equals(newPassword)) {
			ByteSource saltSource = new SecureRandomNumberGenerator().nextBytes();
			this.passwordSalt = saltSource.getBytes();
			this.encryptedPassword = new Sha1Hash(newPassword, saltSource).toString();
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
	@Column(length = 128)
	public byte[] getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(byte[] passwordSalt) {
		this.passwordSalt = passwordSalt;
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

	public void setRoles(final Set<Roles> roles) {
		this.roles = roles;
	}

	// @Enumerated(EnumType.STRING)
	@CollectionOfElements(targetElement = Roles.class)
	public Set<Roles> getRoles() {
		return this.roles;
	}

	public void setMyCourses(final List<Course> myCourses) {
		this.myCourses = myCourses;
	}

	@NonVisual
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "User_Course")
	public List<Course> getMyCourses() {
		return this.myCourses;
	}

	public void setFavoriteCourses(final List<Course> favoriteCourses) {
		this.favoriteCourses = favoriteCourses;
	}

	@NonVisual
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "User_FavCourse")
	public List<Course> getFavoriteCourses() {
		return this.favoriteCourses;
	}

	/**
	 * @return the myWidgets
	 */
	@NonVisual
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@Cascade({ org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
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

	@Override
	public boolean equals(final Object obj) {
		try {
			return ((obj instanceof User) && ((User) obj).getUsername().equals(this.username));
		} catch (final NullPointerException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.username == null ? 0 : this.username.hashCode();
	}

	@Transient
	public Boolean checkPassword(String password) {
		ByteSource saltSource = ByteSource.Util.bytes(this.getPasswordSalt());
		String givenPassword = new Sha1Hash(password, saltSource).toString();
		if (givenPassword != null && givenPassword.equals(this.encryptedPassword))
			return true;
		return false;
	}

}