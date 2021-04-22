package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvStreamReader;
import java.io.IOException;
import org.junit.Test;

public class WsvStreamLineIteratorTest {
	@Test
	public void test() throws IOException, Exception {
		String filePath = "File.sml";
		ReliableTxtDocument.save("Root\n\nEnd", filePath);
		try (WsvStreamReader reader = 
				new WsvStreamReader(filePath)) {
			WsvStreamLineIterator iterator = new WsvStreamLineIterator(reader, "End");
			Assert.equals(iterator.toString(), "(0): Root");
			
			Assert.array_equals(iterator.getLineAsArray(), new String[] {"Root"});
			
			Assert.equals(iterator.getLineIndex(), 1);
			Assert.isTrue(iterator.isEmptyLine());
			Assert.isTrue(iterator.hasLine());
			
			iterator.getLine();
			iterator.getLine();
			
			Assert.equals(iterator.getLineIndex(), 3);
			Assert.isFalse(iterator.isEmptyLine());
			Assert.isFalse(iterator.hasLine());
			Assert.equals(iterator.toString(), "(3): ");
		}
	}
}
