package de.lemo.apps.services.security;

import java.io.IOException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.ldap.client.api.exception.InvalidConnectionException;
import org.apache.directory.api.ldap.model.exception.LdapException;

import de.lemo.apps.application.config.ServerConfiguration;

public class LdapAuthorizingRealm extends AuthorizingRealm {

	@Inject
	private Logger logger;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		return new SimpleAuthorizationInfo();
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		final UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		String userName = userToken.getUsername();
		String password = new String(userToken.getPassword());

		String connectionBind = ServerConfiguration.getInstance().getUserPraefix()+"="+userName+","+ServerConfiguration.getInstance().getUserRoot();

		LdapNetworkConnection ldapConnection = null;
		try {
			LdapConnectionConfig connectionConfig = new LdapConnectionConfig();
			connectionConfig.setLdapHost(ServerConfiguration.getInstance().getLdapHost());
			connectionConfig.setLdapPort(ServerConfiguration.getInstance().getLdapPort());
			connectionConfig.setUseTls(ServerConfiguration.getInstance().getLdapStartTls());
			connectionConfig.setCredentials(password);
			logger.info(password);
			connectionConfig.setName(connectionBind);
			ldapConnection = new LdapNetworkConnection(connectionConfig);
			ldapConnection.bind();
		} catch (InvalidConnectionException e) {
			logger.info("LdapAuthorizingRealm: Connection could not be established.");
			throw new AuthenticationException("LdapAuthorizingRealm: Connection could not be established.");	
		} catch (LdapException e) {
			logger.info("LdapAuthorizingRealm: wrong credentials");
			throw new AuthenticationException("LdapAuthorizingRealm: wrong credentials");	
		}finally{
		    if (ldapConnection != null) { 
		    	logger.info("LdapAuthorizingRealm: Closing ldap connection.");
		        try {
					ldapConnection.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.info("LdapAuthorizingRealm: Ldap connection can't be closed.");
				}
		    } else { 
		    	logger.info("LdapAuthorizingRealm: ldap connection not open");
		    } 
		}
		// login to LDAP successful
		return new SimpleAuthenticationInfo(userToken.getUsername(), userToken.getPassword(), "basic");
	}
}
