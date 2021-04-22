package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvParserException;
import java.io.IOException;
import org.junit.Test;

public class SmlParserTest {
	@Test
	public void parseDocumentNonPreserving() {
		parseDocumentNonPreserving("MyRootElement\nEnd");
		parseDocumentNonPreserving("myrootelement\nend");
		parseDocumentNonPreserving("MYROOTELEMENT\nEND");
		
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute 123\nEnd");
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parseDocumentNonPreserving("MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute \"Hello \"\"world\"\"!\"\n\tMySecondAttribute c:\\Temp\\Readme.txt\nEnd");
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute \"# This is not a comment\"\nEnd");
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute \"-\"\n\tMySecondAttribute -\n\tMyThirdAttribute \"\"\n\tMyFourthAttribute My-Value-123\nEnd");
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute \"Line1\"/\"Line2\"/\"Line3\"\nEnd");
		
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute 123\n\tMyFirstAttribute 3456\n\tMyFirstAttribute 67\n\tElement1\n\tEnd\n\tElement1\n\tEnd\nEnd");
		parseDocumentNonPreserving("RecentFiles\n\tFile c:\\Temp\\Readme.txt\n\tFile \"c:\\My Files\\Todo.txt\"\n\tFile c:\\Games\\Racer\\Config.sml\n\tFile d:\\Untitled.txt\nEnd");
		
		parseDocumentNonPreserving("Root\n-");
		parseDocumentNonPreserving("Root\n\tEnd 12 13\nEnd");
		
		parseDocumentNonPreserving("契約\n\t個人情報\n\t\t名字 田中\n\t\t名前 蓮\n\tエンド\n\t日付 ２０２１－０１－０２\nエンド");
		parseDocumentNonPreserving("Vertragsdaten\n\tPersonendaten\n\t\tNachname Meier\n\t\tVorname Hans\n\tEnde\n\tDatum 2021-01-02\nEnde");
		
		parseDocumentNonPreserving("\"My Root Element\"\n\t\"My First Attribute\" 123\nEnd");
		
		parseDocumentNonPreserving("Actors\n\tName Age PlaceOfBirth FavoriteColor JobTitle\n\t\"John Smith\" 33 Vancouver -\n\t\"Mary Smith\" 27 Toronto Green Lawyer\nEnd");
		
		parseDocumentNonPreserving("MyRootElement\n\tMyFirstAttribute \"123\"\n\tMySecondAttribute \"10\" \"20\" \"30\" \"40\" \"50\"\nEnd", "MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parseDocumentNonPreserving("MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50\nEnd\nMyThirdAttribute \"Hello world\"\nEnd", "MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parseDocumentNonPreserving("# My first SML document\nMyRootElement\n\t#Group1\n\t#\tMyFirstAttribute 123\n\t#\tMySecondAttribute 10 20 30 40 50\n\t#End\n\tMyThirdAttribute \"Hello world\" # Comment\nEnd", "MyRootElement\n\tMyThirdAttribute \"Hello world\"\nEnd");
	}
	
	private void parseDocumentNonPreserving(String textAndExpected) {
		parseDocumentNonPreserving(textAndExpected, textAndExpected);
	}
	
	private void parseDocumentNonPreserving(String text, String expected) {
		try {
			Assert.equals(SmlSerializer.serializeDocumentNonPreserving(SmlParser.parseDocumentNonPreserving(text)), expected);
		} catch (IOException ex) {
			throw new RuntimeException("Parsing failed");
		}
	}
	
	@Test
	public void parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowException() {
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute \"hello world\nEnd",		"String not closed (2, 30)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute ab\"c\nEnd",				"Invalid double quote in value (2, 20)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute \"hello world\"a b c\nEnd",	"Invalid character after string (2, 31)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowWsvException("Root\n  FirstAttribute \"Line1\"/ \"Line2\"\nEnd",	"Invalid string line break (2, 26)");
		
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("# Only\n# Comments",		"End keyword could not be detected (2)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("Root abc\nEnd",			"Invalid root element start (1)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("-\nEnd",					"Null value as element name is not allowed (1)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  -\n  End\nEnd",	"Null value as element name is not allowed (2)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  - 123\nEnd",		"Null value as attribute name is not allowed (2)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  Element\n  End",	"Element \"Root\" not closed (3)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("Root\n  Element\n  End\n",	"Element \"Root\" not closed (4)");
		parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException("Root\nEnd\nRoot2\nEnd",	"Only one root element allowed (3)");
		
	}
	
	private void parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowSmlException(String text, String expectedExceptionMessage) {
		try {
			SmlParser.parseDocumentNonPreserving(text);
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) {
			
		}
		throw new RuntimeException("Text is valid");
	}
	
	private void parseDocumentNonPreserving_InvalidDocumentGiven_ShouldThrowWsvException(String text, String expectedExceptionMessage) {
		try {
			SmlParser.parseDocumentNonPreserving(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) {
			
		}
		throw new RuntimeException("Text is valid");
	}
	
	@Test
	public void parseDocument_InvalidDocumentGiven_ShouldThrowException() {
		parseDocument_InvalidDocumentGiven_ShouldThrowException("Root\nEnd End",		"End keyword could not be detected (2)");
		parseDocument_InvalidJaggedArrayGiven_ShouldThrowException("Root\nEnd End",		"End keyword could not be detected (2)");
		
		parseDocument_InvalidDocumentGiven_ShouldThrowException("\nEnd",		"Invalid root element start (2)");
		parseDocument_InvalidJaggedArrayGiven_ShouldThrowException("\nEnd",		"Invalid root element start (2)");
	}
	
	private void parseDocument_InvalidDocumentGiven_ShouldThrowException(String text, String expectedExceptionMessage) {
		try {
			SmlParser.parseDocument(text);
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) {
			
		}
		throw new RuntimeException("Text is valid");
	}
	
	private void parseDocument_InvalidJaggedArrayGiven_ShouldThrowException(String text, String expectedExceptionMessage) {
		try {
			SmlParser.parseDocument(WsvDocument.parseAsJaggedArray(text));
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) {
			
		}
		throw new RuntimeException("Text is valid");
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		SmlParser smlParser = new SmlParser();
	}
}
