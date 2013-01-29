/**
 * 
 */
package de.lemo.apps.exceptions;

import org.apache.log4j.Logger;


/**
 * @author Andreas Pursian
 *
 */
public class ObjectNotDeletedException extends Exception{
	private Logger logger = Logger.getLogger(ObjectNotDeletedException.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 7075416622913301799L;

	public ObjectNotDeletedException(){
		
	}
	
	public ObjectNotDeletedException(String objectId){
		logger.error("Object with Id: "+objectId+" could not be deleted.");
	}
	

}
