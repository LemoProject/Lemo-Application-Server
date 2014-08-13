package de.lemo.apps.entities;

public class LearningObject {

	private String name;
	private Long combinedId;
	
	
	public LearningObject(String name, Long combinedId){
		this.name = name;
		this.combinedId = combinedId;
	}
	
	public LearningObject(Long combinedId){
		this.combinedId = combinedId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * @return the combinedId
	 */
	public Long getCombinedId() {
		return combinedId;
	}
	
	/**
	 * @param combinedId the combinedId to set
	 */
	public void setCombinedId(Long combinedId) {
		this.combinedId = combinedId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((combinedId == null) ? 0 : combinedId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LearningObject other = (LearningObject) obj;
		if (combinedId == null) {
			if (other.combinedId != null)
				return false;
		} else if (!combinedId.equals(other.combinedId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
