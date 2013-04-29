package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SCConnectorManagerState {

	private EConnectorManagerState state;
	private SCConnector updatingConnector;

	public SCConnectorManagerState(EConnectorManagerState state, SCConnector updatingConnector) {
		this.state = state;
		this.updatingConnector = updatingConnector;
	}

	/**
	 * @return the connectorState
	 */
	public EConnectorManagerState getConnectorState() {
		return state;
	}

	/**
	 * @param connectorState
	 *            the connectorState to set
	 */
	public void setConnectorState(EConnectorManagerState connectorState) {
		this.state = connectorState;
	}

	/**
	 * @return the updatingConnector
	 */
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
