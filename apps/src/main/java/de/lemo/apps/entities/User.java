package de.lemo.apps.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "user")
public class User extends AbstractEntity{


	private static final long serialVersionUID = -2284587022138077470L;
	
	public static enum Role {
		enduser(1), admin(5);
		private int weight;

		Role(int weight) {
			this.weight = weight;
		}

		public int weight() {
			return weight;
		}
	}

    @NaturalId
    @Column(nullable = false, unique = true)
//    @NotNull
//    @Size(min = 3, max = 15)
    private String username;

    @Column(nullable = false)
//    @NotNull
//    @Size(min = 3, max = 50)
    private String fullname;

    @Column(nullable = false)
//    @NotNull
//    @Email
    private String email;

    @Column(nullable = false)
//    @Size(min = 3, max = 12)
//    @NotNull
    private String password;
    
    private boolean accountLocked;

	private boolean credentialsExpired;

	private Set<Role> roles = new HashSet<Role>();

	private byte[] passwordSalt;

    public User()
    {
    }

    public User(final String fullname, final String username, final String email)
    {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
    }

    public User(final String fullname, final String username, final String email,
            final String password)
    {
        this(fullname, username, email);
        this.password = password;
    }

    public User(Long id, String username, String fullname, String email, String password)
    {
        super();
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("id ");
        builder.append(id);
        builder.append(",");
        builder.append("username ");
        builder.append(username);
        return builder.toString();
    }

    @Validate("required,regexp=^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z]+[\\.]{1}[0-9a-zA-Z]+[\\.]?[0-9a-zA-Z]+$")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @NaturalId
	@Column(unique = true)
	@Index(name = "User_username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getFullname()
    {
        return fullname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@CollectionOfElements(targetElement = Role.class)
	public Set<Role> getRoles() {
		return roles;
	}

    
    @Override
	public boolean equals(Object obj) {
		try {
			return (obj instanceof User && ((User) obj).getUsername().equals(username));
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return username == null ? 0 : username.hashCode();
	}

    
}