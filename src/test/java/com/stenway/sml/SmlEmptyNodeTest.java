package com.stenway.sml;

import org.junit.Test;

public class SmlEmptyNodeTest {
	@Test
	public void toStringTest() {
		Assert.equals(new SmlEmptyNode().toString(), "");
	}
}
