package com.stenway.sml;

import java.io.IOException;
import org.junit.Test;

public class SmlSerializerTest {
	@Test
	public void serializeDocumentNonPreserving() {
		serializeDocumentNonPreserving("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", true, "MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50 60\n-\nMyThirdAttribute \"Hello world\"\n-");
		serializeDocumentNonPreserving("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", false, "MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50 60\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
	}
	
	private void serializeDocumentNonPreserving(String text, boolean minify, String expected) {
		try {
			Assert.equals(SmlSerializer.serializeDocumentNonPreserving(SmlParser.parseDocument(text), minify), expected);
		} catch (IOException ex) {
			throw new RuntimeException("Parsing failed");
		}
	}
	
	@Test
	public void serializeDocumentNonPreserving_DefaultIndentation() throws IOException {
		SmlDocument doc = SmlParser.parseDocument("Root\nEnd");
		doc.setDefaultIndentation(" ");
		Assert.equals(SmlSerializer.serializeDocumentNonPreserving(doc, false), "Root\nEnd");
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		SmlSerializer smlSerializer = new SmlSerializer();
	}
	
	@Test
	public void serializeElement() {
		SmlElement element = new SmlElement("Name");
		Assert.equals(SmlSerializer.serializeElement(element), "Name\nEnd");
	}
	
	@Test
	public void serializeElement_EndKeywordNameGiven_ShouldThrowException() {
		try {
			SmlElement element = new SmlElement("End");
			SmlSerializer.serializeElement(element);
		} catch (SmlException exception) {
			Assert.equals(exception.getMessage(), "Element name matches the end keyword 'End'");
			return;
		}
		throw new IllegalStateException("Should throw SmlException");
	}
	
	@Test
	public void serializeAttribute() {
		SmlAttribute attribute = new SmlAttribute("Name", "Value1", "Value2");
		Assert.equals(SmlSerializer.serializeAttribute(attribute), "Name Value1 Value2");
	}
	
	@Test
	public void serializeDocument_EndKeywordNameGiven_ShouldThrowException() {
		try {
			SmlDocument document = new SmlDocument("ENDE");
			document.setEndKeyword("Ende");
			SmlSerializer.serializeDocument(document);
		} catch (SmlException exception) {
			Assert.equals(exception.getMessage(), "Element name matches the end keyword 'Ende'");
			return;
		}
		throw new IllegalStateException("Should throw SmlException");
	}
}
