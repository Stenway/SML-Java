package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import java.io.IOException;
import org.junit.Test;

public class WsvDocumentLineIteratorTest {
	@Test
	public void toStringTest() {
		Assert.equals(getIterator().toString(), "(0): Root");
	}
	
	@Test
	public void getLineAsArray() throws IOException {
		Assert.array_equals(getIterator().getLineAsArray(), new String[] {"Root"});
	}
	
	private WsvDocumentLineIterator getIterator() {
		return new WsvDocumentLineIterator(WsvDocument.parse("Root\nEnd"), "End");
	}
}
