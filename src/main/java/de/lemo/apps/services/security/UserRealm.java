/**
 * File ./src/main/java/de/lemo/apps/services/security/UserRealm.java
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

package de.lemo.apps.services.security;

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
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import de.lemo.apps.entities.User;

public class UserRealm extends AuthorizingRealm {

	protected final Session session;

	public UserRealm(final Session session) {
		super(new MemoryConstrainedCacheManager());
		this.session = session;
		this.setAuthenticationTokenClass(UsernamePasswordToken.class);
		this.setCredentialsMatcher(new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME));
	}

	private User findByUsername(final String username) {

		return (User) this.session.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection was null .... ");
		}

		if (principals.isEmpty()) {
			return null;
		}

		if (principals.fromRealm(this.getName()).size() <= 0) {
			return null;
		}

		final String username = (String) principals.fromRealm(this.getName()).iterator().next();
		if (username == null) {
			return null;
		}
		final User user = this.findByUsername(username);
		if (user == null) {
			return null;
		// TODO Role logic not yet implemented
		}

		return new SimpleAuthorizationInfo();
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
		final UsernamePasswordToken upToken = (UsernamePasswordToken) token;

		final String username = upToken.getUsername();

		// Null username is invalid
		if (username == null) {
			throw new AccountException("Null usernames are not allowed by this realm.");
		}

		final User user = this.findByUsername(username);
		
		return new SimpleAuthenticationInfo(username, user.getPassword(), "basic");
	}

}