package com.stenway.sml;

public class SmlParserException extends RuntimeException {
	public final int LineIndex;
	
	public SmlParserException(int lineIndex, String message) {
		super(String.format("%s (%d)", message, lineIndex + 1));
		LineIndex = lineIndex;
	}
}
