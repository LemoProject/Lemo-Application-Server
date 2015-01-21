/**
 * File ./src/main/java/de/lemo/apps/integration/UserDAO.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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

/**
	 * File UserDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.integration;

import java.util.List;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.User;

/**
 * Interface for an dao object for the user
 */
public interface UserDAO {

	boolean doExist(User user);
	
	boolean doExist(final String username);

	User getUser(String username);
	
	List<User> getAllUser();
	
	@CommitAfter
	boolean toggleFavoriteCourse(final Long courseId, final Long userId);

	@CommitAfter
	void update(User user);

	@CommitAfter
	void save(User user);

	User login(String username, String password);
	
	@CommitAfter
	void remove(User user);

}
