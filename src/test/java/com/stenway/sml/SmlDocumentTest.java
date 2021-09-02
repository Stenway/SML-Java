package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.wsv.WsvParserException;
import java.io.IOException;
import org.junit.Test;

public class SmlDocumentTest {
	
	@Test
	public void toString_minify() {
		toString_minify("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", "MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50 60\n-\nMyThirdAttribute \"Hello world\"\n-");
	}
	
	private void toString_minify(String text, String expected) {
		SmlDocument doc = SmlDocument.parse(text);
		Assert.equals(doc.toStringMinified(), expected);
	}
	
	@Test
	public void minify() {
		minify("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", "MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50 60\n-\nMyThirdAttribute \"Hello world\"\n-");
	}
	
	private void minify(String text, String expected) {
		SmlDocument doc = SmlDocument.parse(text);
		doc.minify();
		Assert.equals(doc.toString(), expected);
	}
	
	@Test
	public void parse() {
		parse("MyRootElement\nEnd");
		parse("myrootelement\nend");
		parse("MYROOTELEMENT\nEND");
		
		parse("MyRootElement\n\tMyFirstAttribute 123\nEnd");
		parse("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parse("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parse("MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parse("MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50\nEnd\nMyThirdAttribute \"Hello world\"\nEnd");
		parse("# My first SML document\nMyRootElement\n\t#Group1\n\t#\tMyFirstAttribute 123\n\t#\tMySecondAttribute 10 20 30 40 50\n\t#End\n\tMyThirdAttribute \"Hello world\" # Comment\nEnd");
		parse("MyRootElement\n\tMyFirstAttribute \"Hello \"\"world\"\"!\"\n\tMySecondAttribute c:\\Temp\\Readme.txt\nEnd");
		parse("MyRootElement\n\tMyFirstAttribute \"# This is not a comment\"\nEnd");
		parse("MyRootElement\n\tMyFirstAttribute \"-\"\n\tMySecondAttribute -\n\tMyThirdAttribute \"\"\n\tMyFourthAttribute My-Value-123\nEnd");
		parse("MyRootElement\n\tMyFirstAttribute \"Line1\"/\"Line2\"/\"Line3\"\nEnd");
		parse("MyRootElement\n\tMyFirstAttribute 123\n\tMyFirstAttribute 3456\n\tMyFirstAttribute 67\n\tElement1\n\tEnd\n\tElement1\n\tEnd\nEnd");
		parse("RecentFiles\n\tFile c:\\Temp\\Readme.txt\n\tFile \"c:\\My Files\\Todo.txt\"\n\tFile c:\\Games\\Racer\\Config.sml\n\tFile d:\\Untitled.txt\nEnd");
		
		parse("Root\n-");
		parse("Root\n  End 12 13\nEnd");
		
		parse("契約\n\t個人情報\n\t\t名字　田中\n\t\t名前　蓮\n\tエンド\n\t日付　２０２１－０１－０２\nエンド");
		parse("Vertragsdaten\n\tPersonendaten\n\t\tNachname Meier\n\t\tVorname Hans\n\tEnde\n\tDatum 2021-01-02\nEnde");
		
		parse("\"My Root Element\"\n\t\"My First Attribute\" 123\nEnd");
		
		parse("Actors\n\tName Age PlaceOfBirth FavoriteColor JobTitle\n\t\"John Smith\" 33 Vancouver -\n\t\"Mary Smith\" 27 Toronto Green Lawyer\nEnd");
		
		parse("MyRootElement\n\tMyFirstAttribute \"123\"\n\tMySecondAttribute \"10\" \"20\" \"30\" \"40\" \"50\"\nEnd", "MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
	}
	
	private void parse(String inputAndExpected) {
		parse(inputAndExpected, inputAndExpected);
	}
	
	private void parse(String text, String Expected) {
		Assert.equals(SmlDocument.parse(text).toString(), Expected);
	}
	
	@Test
	public void parse_InvalidDocumentGiven_ShouldThrowException() {
		parse_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute \"hello world\nEnd",			"String not closed (2, 30)");
		parse_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute ab\"c\nEnd",					"Invalid double quote after value (2, 20)");
		parse_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute \"hello world\"a b c\nEnd",	"Invalid character after string (2, 31)");
		parse_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute \"Line1\"/ \"Line2\"\nEnd",	"Invalid string line break (2, 26)");
		
		parse_InvalidDocumentGiven_ShouldThrowSmlException("# Only\n# Comments",		"End keyword could not be detected (2)");
		parse_InvalidDocumentGiven_ShouldThrowSmlException("Root abc\nEnd",				"Invalid root element start (1)");
		parse_InvalidDocumentGiven_ShouldThrowSmlException("-\nEnd",					"Null value as element name is not allowed (1)");
		parse_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  -\n  End\nEnd",		"Null value as element name is not allowed (2)");
		parse_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  - 123\nEnd",		"Null value as attribute name is not allowed (2)");
		parse_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  Element\n  End",	"Element \"Root\" not closed (3)");
		parse_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  Element\n  End\n",	"Element \"Root\" not closed (4)");
		parse_InvalidDocumentGiven_ShouldThrowSmlException("Root\nEnd\nRoot2\nEnd",		"Only one root element allowed (3)");
	}
	
	private void parse_InvalidDocumentGiven_ShouldThrowSmlException(String text, String expectedExceptionMessage) {
		try {
			SmlDocument.parse(text);
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Text is valid");
	}
	
	private void parse_InvalidDocumentGiven_ShouldThrowWsvException(String text, String expectedExceptionMessage) {
		try {
			SmlDocument.parse(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;	
		}
		throw new RuntimeException("Text is valid");
	}
	
	@Test
	public void getEncoding() {
		getEncoding(new SmlDocument(), ReliableTxtEncoding.UTF_8);
		
		getEncoding(new SmlDocument(ReliableTxtEncoding.UTF_8), ReliableTxtEncoding.UTF_8);
		getEncoding(new SmlDocument(ReliableTxtEncoding.UTF_16), ReliableTxtEncoding.UTF_16);
		getEncoding(new SmlDocument(ReliableTxtEncoding.UTF_16_REVERSE), ReliableTxtEncoding.UTF_16_REVERSE);
		getEncoding(new SmlDocument(ReliableTxtEncoding.UTF_32), ReliableTxtEncoding.UTF_32);
		
		getEncoding(new SmlDocument("Root", ReliableTxtEncoding.UTF_8), ReliableTxtEncoding.UTF_8);
		getEncoding(new SmlDocument("Root", ReliableTxtEncoding.UTF_16), ReliableTxtEncoding.UTF_16);
		getEncoding(new SmlDocument("Root", ReliableTxtEncoding.UTF_16_REVERSE), ReliableTxtEncoding.UTF_16_REVERSE);
		getEncoding(new SmlDocument("Root", ReliableTxtEncoding.UTF_32), ReliableTxtEncoding.UTF_32);
		
		getEncoding(new SmlDocument(new SmlElement("Root"), ReliableTxtEncoding.UTF_8), ReliableTxtEncoding.UTF_8);
		getEncoding(new SmlDocument(new SmlElement("Root"), ReliableTxtEncoding.UTF_16), ReliableTxtEncoding.UTF_16);
		getEncoding(new SmlDocument(new SmlElement("Root"), ReliableTxtEncoding.UTF_16_REVERSE), ReliableTxtEncoding.UTF_16_REVERSE);
		getEncoding(new SmlDocument(new SmlElement("Root"), ReliableTxtEncoding.UTF_32), ReliableTxtEncoding.UTF_32);
	}
	
	private void getEncoding(SmlDocument document, ReliableTxtEncoding expectedEncoding) {
		Assert.equals(document.getEncoding(), expectedEncoding);
	}
	
	@Test
	public void setDefaultIndentation_NonWhitespaceGiven_ShouldThrowException() {
		setDefaultIndentation_NonWhitespaceGiven_ShouldThrowException("\n");
		setDefaultIndentation_NonWhitespaceGiven_ShouldThrowException("a");
	}
	
	private void setDefaultIndentation_NonWhitespaceGiven_ShouldThrowException(String defaultIndentation) {
		try {
			SmlDocument document = new SmlDocument();
			document.setDefaultIndentation(defaultIndentation);
		} catch (IllegalArgumentException e) {
			Assert.equals(e.getMessage(), "Indentation value contains non whitespace character or line feed");
			return;
		}
		throw new RuntimeException();
	}
	
	@Test
	public void toString_NonPreserving() throws IOException {
		Assert.equals(SmlDocument.parse("  Root  \n  End  ").toString(false), "Root\nEnd");
		Assert.equals(SmlDocument.parse("  Root  \n  End  ", false).toString(true), "Root\nEnd");
	}
	
	@Test
	public void save() throws IOException {
		save(ReliableTxtEncoding.UTF_8);
		save(ReliableTxtEncoding.UTF_16);
		save(ReliableTxtEncoding.UTF_16_REVERSE);
		save(ReliableTxtEncoding.UTF_32);
	}
	
	private void save(ReliableTxtEncoding encoding) throws IOException {
		SmlDocument document = new SmlDocument(encoding);
		String filePath = "File.sml";
		document.save(filePath);
		load(filePath, "Root\nEnd", encoding);
	}
	
	private void load(String filePath, String text, ReliableTxtEncoding encoding) {
		try {
			ReliableTxtDocument loaded = ReliableTxtDocument.load(filePath);
			Assert.equals(loaded.getEncoding(), encoding);
			Assert.equals(loaded.getText(), text);
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
	
	@Test
	public void load() throws IOException {
		load(ReliableTxtEncoding.UTF_8);
		load(ReliableTxtEncoding.UTF_16);
		load(ReliableTxtEncoding.UTF_16_REVERSE);
		load(ReliableTxtEncoding.UTF_32);
	}
	
	private void load(ReliableTxtEncoding encoding) throws IOException {
		String filePath = "File.sml";
		ReliableTxtDocument.save("Root\nEnd", encoding, filePath);
		SmlDocument document = SmlDocument.load(filePath);
		Assert.equals(document.getEncoding(), encoding);
		Assert.equals(document.toString(), "Root\nEnd");
	}
}
