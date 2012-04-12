package de.lemo.apps.services.security;

import java.util.HashSet;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.lemo.apps.entities.User;

public class UserRealm extends AuthorizingRealm {
	protected final Session session;
	
	public UserRealm(Session session) {
		super(new MemoryConstrainedCacheManager());
		this.session = session;
		//setName("localaccounts");
		setAuthenticationTokenClass(UsernamePasswordToken.class);
		setCredentialsMatcher(new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME));
	}
	
	private User findByUsername(String username) {
		
		return (User) session.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) throw new AuthorizationException("PrincipalCollection was null .... ");

		if (principals.isEmpty()) return null;

		if (principals.fromRealm(getName()).size() <= 0) return null;

		String username = (String) principals.fromRealm(getName()).iterator().next();
		if (username == null) return null;
		User user = findByUsername(username);
		if (user == null) return null;
		//TODO Role logic not yet implemented
		
		//Set<String> roles = new HashSet<String>(user.getRoles().size());
		//for (Role role : user.getRoles())
		//	roles.add(role.name());
		//return new SimpleAuthorizationInfo(roles);
		
		return new SimpleAuthorizationInfo();
	}

	

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;

		String username = upToken.getUsername();

		// Null username is invalid
		if (username == null) { throw new AccountException("Null usernames are not allowed by this realm."); }

		User user = findByUsername(username);

		if (user.isAccountLocked()) { throw new LockedAccountException("Account [" + username + "] is locked."); }
		if (user.isCredentialsExpired()) {
			String msg = "The credentials for account [" + username + "] are expired";
			throw new ExpiredCredentialsException(msg);
		}
		return new SimpleAuthenticationInfo(username, user.getPassword(), "basic");
	}

}