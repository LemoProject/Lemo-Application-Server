/**
 * 
 */
package de.lemo.apps.services.internal;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * @author A. Pursian
 *         adding custom MySQL Dialect with UTF-8 as default charset and InnoDB table engine
 */
public class MysqlDialectUtf8 extends MySQL5InnoDBDialect {

	@Override
	public String getTableTypeString() {
		return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}
}
