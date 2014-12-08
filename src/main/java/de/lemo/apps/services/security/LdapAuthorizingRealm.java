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
		logger.info(connectionBind);
		logger.info(ServerConfiguration.getInstance().getUserPraefix());
		logger.info(ServerConfiguration.getInstance().getUserRoot());
		logger.info(ServerConfiguration.getInstance().getLdapHost());
		logger.info(String.valueOf(ServerConfiguration.getInstance().getLdapPort()));
		logger.info(String.valueOf(ServerConfiguration.getInstance().getLdapStartTls()));

		LdapNetworkConnection ldapConnection = null;
		// try to connect to the ldapServer
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
			e.printStackTrace();
			logger.info("ldap.noConnection");
			throw new AuthenticationException("ldap.noConnection");	
		} catch (LdapException e) {
			e.printStackTrace();
			logger.info("ldap.wrongCredentials");
			throw new AuthenticationException("ldap.wrongCredentials");	
		}finally{
		    if (ldapConnection != null) { 
		    	logger.info("Closing ldapConnection.");
		        try {
					ldapConnection.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } else { 
		    	logger.info("ldapConnection not open");
		    } 
		}
		// login to LDAP successful
		return new SimpleAuthenticationInfo(userToken.getUsername(), userToken.getPassword(), "basic");
	}
}
