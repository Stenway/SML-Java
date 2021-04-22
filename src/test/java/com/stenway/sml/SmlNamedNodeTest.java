package com.stenway.sml;

import org.junit.Test;

public class SmlNamedNodeTest {
	@Test
	public void setName_NullGiven_ShouldThrowException() {
		try {
			SmlElement element = new SmlElement("Name");
			element.setName(null);
		} catch (IllegalArgumentException e) {
			Assert.equals(e.getMessage(), "Name cannot be null");
			return;
		}
		throw new RuntimeException();
	}
	
	@Test
	public void hasName() {
		hasName("Name", "Name", true);
		hasName("Name", "name", true);
		hasName("Name", "NAME", true);
		hasName("Name", "Nam", false);
		hasName("Name", "", false);
		hasName("Name", null, false);
	}
	
	private void hasName(String name, String hasName, boolean expectedResult) {
		SmlElement element = new SmlElement(name);
		Assert.equals(element.hasName(hasName), expectedResult);
	}
}
