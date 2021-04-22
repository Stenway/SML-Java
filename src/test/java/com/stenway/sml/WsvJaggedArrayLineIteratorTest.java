package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import java.io.IOException;
import org.junit.Test;

public class WsvJaggedArrayLineIteratorTest {
	@Test
	public void toStringTest() {
		Assert.equals(getIterator().toString(), "(0): Root");
	}
	
	@Test
	public void getLineAsArray() throws IOException {
		Assert.array_equals(getIterator().getLineAsArray(), new String[] {"Root"});
	}
	
	@Test
	public void getLine() throws IOException {
		Assert.equals(getIterator().getLine().toString(), "Root");
	}
	
	@Test
	public void test() throws IOException {
		WsvJaggedArrayLineIterator iterator = getIterator();
		iterator.getLine();
		
		Assert.isTrue(iterator.isEmptyLine());
		iterator.getLine();
		
		Assert.isFalse(iterator.isEmptyLine());
		
		iterator.getLine();
		Assert.equals(iterator.toString(), "(3): ");
	}
	
	private WsvJaggedArrayLineIterator getIterator() {
		return new WsvJaggedArrayLineIterator(WsvDocument.parseAsJaggedArray("Root\n\nEnd"), "End");
	}
}
