/**
	 * File LongValueEncoder.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * 
	 * adding custom MySQL Dialect with UTF-8 as default charset and InnoDB table engine
	 */
package de.lemo.apps.services.internal;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * Static String with SQL dialect 
 *
 */
public class MysqlDialectUtf8 extends MySQL5InnoDBDialect {

	@Override
	public String getTableTypeString() {
		return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}
}
