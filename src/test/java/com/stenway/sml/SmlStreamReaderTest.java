package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import org.junit.Test;

public class SmlStreamReaderTest {
	@Test
	public void readNode() throws Exception {
		readNode(ReliableTxtEncoding.UTF_8);
		readNode(ReliableTxtEncoding.UTF_16);
		readNode(ReliableTxtEncoding.UTF_16_REVERSE);
		readNode(ReliableTxtEncoding.UTF_32);
	}
	
	private void readNode(ReliableTxtEncoding encoding) throws Exception {
		String filePath = "Test.sml";
		ReliableTxtDocument.save("Root\nElement1\nEnd\nAttribute1 Value\nElement2\nEnd\nAttribute2 Value\nEnd", encoding, filePath);
		try (SmlStreamReader reader = 
				new SmlStreamReader(filePath)) {
			Assert.equals(reader.Encoding, encoding);
			SmlNode node = null;
			int lineCount = 0;
			while (true) {
				lineCount++;
				node = reader.readNode();
				Assert.isTrue(node instanceof SmlElement);
				SmlElement element = (SmlElement)node;
				Assert.equals(element.getName(), "Element"+lineCount);
				
				node = reader.readNode();
				Assert.isTrue(node instanceof SmlAttribute);
				SmlAttribute attribute = (SmlAttribute)node;
				Assert.equals(attribute.getName(), "Attribute"+lineCount);
				
				if (lineCount == 2) {
					break;
				}
			}
			Assert.equals(reader.readNode(), null);
		}
	}
	
	@Test
	public void constructor_Localized() throws Exception {
		String filePath = "Test.sml";
		ReliableTxtDocument.save("Wurzel\nElement1\nEnde\nEnde", ReliableTxtEncoding.UTF_8, filePath);
		try (SmlStreamReader reader = 
				new SmlStreamReader(filePath, "Ende")) {
			
		}
	}
}
