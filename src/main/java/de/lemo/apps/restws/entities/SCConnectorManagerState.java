package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SCConnectorManagerState {

	private String state;
	private SCConnector updatingConnector;

	public SCConnectorManagerState(){}
	
	public SCConnectorManagerState(String state, SCConnector updatingConnector) {
		this.state = state;
		this.updatingConnector = updatingConnector;
	}

	/**
	 * @return the connectorState
	 */
	@XmlElement(name = "state")
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the updatingConnector
	 */
	@XmlElement(name = "updatingConnector")
	public SCConnector getUpdatingConnector() {
		return updatingConnector;
	}

	/**
	 * @param updatingConnector
	 *            the updatingConnector to set
	 */
	public void setUpdatingConnector(SCConnector updatingConnector) {
		this.updatingConnector = updatingConnector;
	}

}
