package com.stenway.sml;

public class SmlNamedNode extends SmlNode {
	private String name;
	
	public SmlNamedNode(String name) {
		setName(name);
	}
	
	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasName(String name) {
		if (name == null) return false;
		return this.name.equalsIgnoreCase(name);
	}
}