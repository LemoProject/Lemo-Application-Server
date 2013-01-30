package de.lemo.apps.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "user")
public class User extends AbstractEntity {

	private static final long serialVersionUID = -2284587022138077470L;

	public static enum Role {
		enduser(1),
		admin(5);

		private int weight;

		Role(final int weight) {
			this.weight = weight;
		}

		public int weight() {
			return this.weight;
		}
	}

	@NaturalId
	@Column(nullable = false, unique = true)
	// @NotNull
	// @Size(min = 3, max = 15)
	private String username;

	@Column(nullable = false)
	// @NotNull
	// @Size(min = 3, max = 50)
	private String fullname;

	@Column(nullable = false)
	// @NotNull
	// @Email
	private String email;

	@Column(nullable = false)
	// @Size(min = 3, max = 12)
	// @NotNull
	private String password;

	private boolean accountLocked;

	private boolean credentialsExpired;

	private Set<Role> roles = new HashSet<Role>();

	private byte[] passwordSalt;

	private List<Long> myCourses;

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

	public User(final String fullname, final String username, final String email,
			final String password) {
		this(fullname, username, email);
		this.password = password;
	}

	public User(final Long id, final String username, final String fullname, final String email, final String password) {
		super();
		this.id = id;
		this.username = username;
		this.fullname = fullname;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
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

	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
	}

	@CollectionOfElements(targetElement = Role.class)
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setMyCourses(final List<Long> myCourses) {
		this.myCourses = myCourses;
	}

	@CollectionOfElements(targetElement = Long.class)
	public List<Long> getMyCourses() {
		return this.myCourses;
	}

	/**
	 * @return the widget1
	 */
	public Long getWidget1() {
		return this.widget1;
	}

	/**
	 * @param widget1
	 *            the widget1 to set
	 */
	@NonVisual
	public void setWidget1(final Long widget1) {
		this.widget1 = widget1;
	}

	/**
	 * @return the widget2
	 */
	public Long getWidget2() {
		return this.widget2;
	}

	/**
	 * @param widget2
	 *            the widget2 to set
	 */
	@NonVisual
	public void setWidget2(final Long widget2) {
		this.widget2 = widget2;
	}

	/**
	 * @return the widget3
	 */
	public Long getWidget3() {
		return this.widget3;
	}

	/**
	 * @param widget3
	 *            the widget3 to set
	 */
	@NonVisual
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

}