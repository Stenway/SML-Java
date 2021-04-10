package com.stenway.sml;

import com.stenway.wsv.WsvParserException;
import java.io.IOException;
import org.junit.Test;

public class SmlDocumentTest {
	
	@Test
	public void test_toString_minify() {
		toString_minify_equals("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", "MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50 60\n-\nMyThirdAttribute \"Hello world\"\n-");
	}
	
	private void toString_minify_equals(String text, String expected) {
		try {
			SmlDocument doc = SmlDocument.parse(text);
			Assert.equals(doc.toStringMinified(), expected);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	@Test
	public void test_minify() {
		minify_equals("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", "MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50 60\n-\nMyThirdAttribute \"Hello world\"\n-");
	}
	
	private void minify_equals(String text, String expected) {
		try {
			SmlDocument doc = SmlDocument.parse(text);
			doc.minify();
			Assert.equals(doc.toString(), expected);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	@Test
	public void test_parse() {
		parse_equals("MyRootElement\nEnd");
		parse_equals("myrootelement\nend");
		parse_equals("MYROOTELEMENT\nEND");
		
		parse_equals("MyRootElement\n\tMyFirstAttribute 123\nEnd");
		parse_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parse_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parse_equals("MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parse_equals("MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50\nEnd\nMyThirdAttribute \"Hello world\"\nEnd");
		parse_equals("# My first SML document\nMyRootElement\n\t#Group1\n\t#\tMyFirstAttribute 123\n\t#\tMySecondAttribute 10 20 30 40 50\n\t#End\n\tMyThirdAttribute \"Hello world\" # Comment\nEnd");
		parse_equals("MyRootElement\n\tMyFirstAttribute \"Hello \"\"world\"\"!\"\n\tMySecondAttribute c:\\Temp\\Readme.txt\nEnd");
		parse_equals("MyRootElement\n\tMyFirstAttribute \"# This is not a comment\"\nEnd");
		parse_equals("MyRootElement\n\tMyFirstAttribute \"-\"\n\tMySecondAttribute -\n\tMyThirdAttribute \"\"\n\tMyFourthAttribute My-Value-123\nEnd");
		parse_equals("MyRootElement\n\tMyFirstAttribute \"Line1\"/\"Line2\"/\"Line3\"\nEnd");
		parse_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMyFirstAttribute 3456\n\tMyFirstAttribute 67\n\tElement1\n\tEnd\n\tElement1\n\tEnd\nEnd");
		parse_equals("RecentFiles\n\tFile c:\\Temp\\Readme.txt\n\tFile \"c:\\My Files\\Todo.txt\"\n\tFile c:\\Games\\Racer\\Config.sml\n\tFile d:\\Untitled.txt\nEnd");
		
		parse_equals("Root\n-");
		parse_equals("Root\n  End 12 13\nEnd");
		
		parse_equals("契約\n\t個人情報\n\t\t名字　田中\n\t\t名前　蓮\n\tエンド\n\t日付　２０２１－０１－０２\nエンド");
		parse_equals("Vertragsdaten\n\tPersonendaten\n\t\tNachname Meier\n\t\tVorname Hans\n\tEnde\n\tDatum 2021-01-02\nEnde");
		
		parse_equals("\"My Root Element\"\n\t\"My First Attribute\" 123\nEnd");
		
		parse_equals("Actors\n\tName Age PlaceOfBirth FavoriteColor JobTitle\n\t\"John Smith\" 33 Vancouver -\n\t\"Mary Smith\" 27 Toronto Green Lawyer\nEnd");
		
		parse_equals("MyRootElement\n\tMyFirstAttribute \"123\"\n\tMySecondAttribute \"10\" \"20\" \"30\" \"40\" \"50\"\nEnd", "MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
	}
	
	private void parse_equals(String inputAndExpected) {
		parse_equals(inputAndExpected, inputAndExpected);
	}
	
	private void parse_equals(String text, String Expected) {
		try {
			Assert.equals(SmlDocument.parse(text).toString(), Expected);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	@Test
	public void test_parse_exceptions() {
		parse_throws_wsvException("Root\n  FirstAttribute \"hello world\nEnd",			"String not closed (2, 30)");
		parse_throws_wsvException("Root\n  FirstAttribute ab\"c\nEnd",					"Invalid double quote in value (2, 20)");
		parse_throws_wsvException("Root\n  FirstAttribute \"hello world\"a b c\nEnd",	"Invalid character after string (2, 31)");
		parse_throws_wsvException("Root\n  FirstAttribute \"Line1\"/ \"Line2\"\nEnd",	"Invalid string line break (2, 26)");
		
		parse_throws_smlException("# Only\n# Comments",			"End keyword could not be detected (2)");
		parse_throws_smlException("Root abc\nEnd",				"Invalid root element start (1)");
		parse_throws_smlException("-\nEnd",						"Null value as element name is not allowed (1)");
		parse_throws_smlException("Root\n  -\n  End\nEnd",		"Null value as element name is not allowed (2)");
		parse_throws_smlException("Root\n  - 123\nEnd",			"Null value as attribute name is not allowed (2)");
		parse_throws_smlException("Root\n  Element\n  End",		"Element \"Root\" not closed (3)");
		parse_throws_smlException("Root\n  Element\n  End\n",	"Element \"Root\" not closed (4)");
		parse_throws_smlException("Root\nEnd\nRoot2\nEnd",		"Only one root element allowed (3)");
	}
	
	private void parse_throws_smlException(String text, String expectedExceptionMessage) {
		try {
			SmlDocument.parse(text);
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) { 
			
		}
		throw new RuntimeException("Text is valid");
	}
	
	private void parse_throws_wsvException(String text, String expectedExceptionMessage) {
		try {
			SmlDocument.parse(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		} catch (IOException ex) { 
			
		}
		throw new RuntimeException("Text is valid");
	}
}
