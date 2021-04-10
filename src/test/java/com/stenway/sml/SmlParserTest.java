package com.stenway.sml;

import com.stenway.wsv.WsvParserException;
import java.io.IOException;
import org.junit.Test;

public class SmlParserTest {
	@Test
	public void test_parseDocumentNP() {
		parseDocumentNP_equals("MyRootElement\nEnd");
		parseDocumentNP_equals("myrootelement\nend");
		parseDocumentNP_equals("MYROOTELEMENT\nEND");
		
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute 123\nEnd");
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parseDocumentNP_equals("MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute \"Hello \"\"world\"\"!\"\n\tMySecondAttribute c:\\Temp\\Readme.txt\nEnd");
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute \"# This is not a comment\"\nEnd");
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute \"-\"\n\tMySecondAttribute -\n\tMyThirdAttribute \"\"\n\tMyFourthAttribute My-Value-123\nEnd");
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute \"Line1\"/\"Line2\"/\"Line3\"\nEnd");
		
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMyFirstAttribute 3456\n\tMyFirstAttribute 67\n\tElement1\n\tEnd\n\tElement1\n\tEnd\nEnd");
		parseDocumentNP_equals("RecentFiles\n\tFile c:\\Temp\\Readme.txt\n\tFile \"c:\\My Files\\Todo.txt\"\n\tFile c:\\Games\\Racer\\Config.sml\n\tFile d:\\Untitled.txt\nEnd");
		
		parseDocumentNP_equals("Root\n-");
		parseDocumentNP_equals("Root\n\tEnd 12 13\nEnd");
		
		parseDocumentNP_equals("契約\n\t個人情報\n\t\t名字 田中\n\t\t名前 蓮\n\tエンド\n\t日付 ２０２１－０１－０２\nエンド");
		parseDocumentNP_equals("Vertragsdaten\n\tPersonendaten\n\t\tNachname Meier\n\t\tVorname Hans\n\tEnde\n\tDatum 2021-01-02\nEnde");
		
		parseDocumentNP_equals("\"My Root Element\"\n\t\"My First Attribute\" 123\nEnd");
		
		parseDocumentNP_equals("Actors\n\tName Age PlaceOfBirth FavoriteColor JobTitle\n\t\"John Smith\" 33 Vancouver -\n\t\"Mary Smith\" 27 Toronto Green Lawyer\nEnd");
		
		parseDocumentNP_equals("MyRootElement\n\tMyFirstAttribute \"123\"\n\tMySecondAttribute \"10\" \"20\" \"30\" \"40\" \"50\"\nEnd", "MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parseDocumentNP_equals("MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50\nEnd\nMyThirdAttribute \"Hello world\"\nEnd", "MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parseDocumentNP_equals("# My first SML document\nMyRootElement\n\t#Group1\n\t#\tMyFirstAttribute 123\n\t#\tMySecondAttribute 10 20 30 40 50\n\t#End\n\tMyThirdAttribute \"Hello world\" # Comment\nEnd", "MyRootElement\n\tMyThirdAttribute \"Hello world\"\nEnd");
	}
	
	private void parseDocumentNP_equals(String textAndExpected) {
		parseDocumentNP_equals(textAndExpected, textAndExpected);
	}
	
	private void parseDocumentNP_equals(String text, String expected) {
		try {
			Assert.equals(SmlSerializer.serializeDocumentNonPreserving(SmlParser.parseDocumentNonPreserving(text)), expected);
		} catch (IOException ex) {
			throw new RuntimeException("Parsing failed");
		}
	}
	
	@Test
	public void test_parseDocumentNP_exceptions() {
		parseDocumentNP_throws_wsvException("Root\n  FirstAttribute \"hello world\nEnd",			"String not closed (2, 30)");
		parseDocumentNP_throws_wsvException("Root\n  FirstAttribute ab\"c\nEnd",					"Invalid double quote in value (2, 20)");
		parseDocumentNP_throws_wsvException("Root\n  FirstAttribute \"hello world\"a b c\nEnd",	"Invalid character after string (2, 31)");
		parseDocumentNP_throws_wsvException("Root\n  FirstAttribute \"Line1\"/ \"Line2\"\nEnd",	"Invalid string line break (2, 26)");
		
		parseDocumentNP_throws_smlException("# Only\n# Comments",		"End keyword could not be detected (2)");
		parseDocumentNP_throws_smlException("Root abc\nEnd",			"Invalid root element start (1)");
		parseDocumentNP_throws_smlException("-\nEnd",					"Null value as element name is not allowed (1)");
		parseDocumentNP_throws_smlException("Root\n  -\n  End\nEnd",	"Null value as element name is not allowed (2)");
		parseDocumentNP_throws_smlException("Root\n  - 123\nEnd",		"Null value as attribute name is not allowed (2)");
		parseDocumentNP_throws_smlException("Root\n  Element\n  End",	"Element \"Root\" not closed (3)");
		parseDocumentNP_throws_smlException("Root\n  Element\n  End\n",	"Element \"Root\" not closed (4)");
		parseDocumentNP_throws_smlException("Root\nEnd\nRoot2\nEnd",	"Only one root element allowed (3)");
		
	}
	
	private void parseDocumentNP_throws_smlException(String text, String expectedExceptionMessage) {
		try {
			SmlParser.parseDocumentNonPreserving(text);
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) {
			
		}
		throw new RuntimeException("Text is valid");
	}
	
	private void parseDocumentNP_throws_wsvException(String text, String expectedExceptionMessage) {
		try {
			SmlParser.parseDocumentNonPreserving(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) {
			
		}
		throw new RuntimeException("Text is valid");
	}
}
