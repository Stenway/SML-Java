package com.stenway.sml;

import org.junit.Test;

public class SmlParserExceptionTest {

	@Test
	public void getMessage() {
		Assert.equals(new SmlParserException(1, "Test").getMessage(), "Test (2)");
	}
	
}