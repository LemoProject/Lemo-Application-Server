/**
 * File ./de/lemo/apps/services/security/BasicSecurityRealm.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.services.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.ioc.annotations.Inject;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;

public class BasicSecurityRealm extends AuthorizingRealm
{

	@Inject
	private UserDAO userDAO;

	@Override
	@Log
	protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
		return new SimpleAuthorizationInfo();
	}

	@Override
	@Log
	protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
			throws AuthenticationException
	{

		final UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		final String username = userToken.getUsername();
		final User loginUser = this.userDAO.login(userToken.getUsername(), String.copyValueOf(userToken.getPassword()));

		AuthenticationInfo authInfo = null;

		if (loginUser == null)
		{
			throw new AuthenticationException("The user " + username + " doesn't exist.");
		}
		else
		{

			if (loginUser.isAccountLocked()) {
				throw new LockedAccountException("Account for user: " + username + " is locked.");
			}
			if (loginUser.isCredentialsExpired()) {
				throw new ExpiredCredentialsException("The credentials for user: " + username + " are expired.");
			}
			authInfo = new SimpleAuthenticationInfo(userToken.getUsername(), userToken.getPassword(), "basic");
		}

		return authInfo;
	}

}