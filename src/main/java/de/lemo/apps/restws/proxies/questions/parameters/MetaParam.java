/**
 * File ./src/main/java/de/lemo/apps/restws/proxies/questions/parameters/MetaParam.java
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
	 * File MetaParam.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.restws.proxies.questions.parameters;

/**
 * Interface for the metaparams of the question classes
 * @author Boris Wenzlaff
 *
 */
public interface MetaParam {

	String COURSE_IDS = "cid";
	String QUIZ_IDS = "quiz_ids";
	String DEGREE = "degree";
	String DEPARTMENT = "dep";
	String END_TIME = "end";
	String LOG_OBJECT_IDS = "oid";
	String LOGOUT_FLAG = "logout";
	String RESOLUTION = "resolution";
	String ROLE_IDS = "rid";
	String START_TIME = "start";
	String TYPES = "types";
	String USER_IDS = "uid";
	String USER_NAME = "uname";
	String MIN_SUP = "min_sup";
	String SESSION_WISE = "session_wise";
	String MIN_LENGTH = "min_length";
	String MAX_LENGTH = "max_length";
	String MIN_INTERVAL = "min_interval";
	String MAX_INTERVAL = "max_interval";
	String MAX_WHOLE_INTERVAL = "max_whole_interval";
	String MIN_WHOLE_INTERVAL = "min_whole_interval";
	String SEARCH_TEXT = "stext";
	String RESULT_AMOUNT = "res_amount";
	String OFFSET = "offset";
	String GENDER = "gender";
	String LEMO_USER_ID = "lemo_uid";
	String LEARNING_OBJ_IDS = "learning_obj_ids";	
}
