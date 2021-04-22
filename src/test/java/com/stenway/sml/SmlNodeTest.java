package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import org.junit.Test;

public class SmlNodeTest {
	@Test
	public void getWhitespaces() {
		SmlElement element = new SmlElement("Name");
		Assert.array_equals(element.getWhitespaces(), null);
		
		element.setWhitespaces("  ", "   ");
		Assert.array_equals(element.getWhitespaces(), stringArray("  ", "   "));
	}
	
	@Test
	public void getComment() {
		SmlElement element = new SmlElement("Name");
		Assert.equals(element.getComment(), null);
		
		element.setComment("Comment");
		Assert.equals(element.getComment(), "Comment");
	}
	
	//static 
	
	@Test
	public void toWsvLines() {
		SmlElement element = new SmlElement("Name");
		WsvDocument wsvDocument = new WsvDocument();
		element.toWsvLines(wsvDocument, 0, null, null);
	}
	
	/*@Test
	public void setWhitespaces() {
		SmlElement element = new SmlElement("Name");
		element.setWhitespaces("  ", "   ");
		Assert.array_equals(element.getWhitespaces(), null);
	}*/
	
	public static String[] stringArray(String... values) {
		return values;
	}
}