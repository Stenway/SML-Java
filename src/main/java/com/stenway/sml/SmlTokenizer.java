package com.stenway.sml;

import com.stenway.wsv.WsvTokenizer;
import java.util.ArrayList;

public class SmlTokenizer {
	private ArrayList<Integer> tokens = new ArrayList<>();
	
	public static final int TokenTypeLineBreak = 0;
	public static final int TokenTypeWhitespace = 1;
	public static final int TokenTypeComment = 2;
	public static final int TokenTypeNull = 3;
	public static final int TokenTypeValue = 4;
	public static final int TokenTypeStringStart = 5;
	public static final int TokenTypeStringEnd = 6;
	public static final int TokenTypeStringText = 7;
	public static final int TokenTypeStringEscapedDoubleQuote = 8;
	public static final int TokenTypeStringLineBreak = 9;
	public static final int TokenTypeEndOfText = 10;
	public static final int TokenTypeError = 11;
	public static final int TokenTypeElement = 12;
	public static final int TokenTypeAttribute = 13;
	
	private ArrayList<int[]> lines = new ArrayList<>();
	
	private int[] toArray(ArrayList<Integer> list) {
		return list.stream().mapToInt(i -> i).toArray();
	} 
	
	private void splitIntoLines(int[] wsvTokens) {
		ArrayList<Integer> curLineTokens = new ArrayList<>();
		
		for (int i=0; i<wsvTokens.length/2; i++) {
			int tokenType = wsvTokens[i*2];
			int tokenLength = wsvTokens[i*2+1];
			curLineTokens.add(tokenType);
			curLineTokens.add(tokenLength);
			if (tokenType == WsvTokenizer.TokenTypeLineBreak) {
				int[] line = toArray(curLineTokens);
				lines.add(line);
				curLineTokens.clear();
			}
		}
		
		int[] lastLine = toArray(curLineTokens);
		lines.add(lastLine);
	}
	
	private int getValueCount(int[] line) {
		int count = 0;
		for (int i=0; i<line.length/2; i++) {
			int tokenType = line[i*2];
			if (tokenType == WsvTokenizer.TokenTypeNull ||
					tokenType == WsvTokenizer.TokenTypeValue ||
					tokenType == WsvTokenizer.TokenTypeStringStart ) {
				count++;
			} else if (tokenType == WsvTokenizer.TokenTypeStringLineBreak) {
				count--;
			}
		}
		return count;
	}
	
	private void addToken(int tokenType, int length) {
		tokens.add(tokenType);
		tokens.add(length);
	}
	
	private int tokenizeFirstValue(int[] line, int type) {
		int startIndex = 0;
		if (line[0] == WsvTokenizer.TokenTypeWhitespace) {
			startIndex += 1;
			addToken(line[0], line[1]);
		}
		int firstValueTokenType = line[startIndex*2];
		if (firstValueTokenType == WsvTokenizer.TokenTypeNull || firstValueTokenType == WsvTokenizer.TokenTypeValue) {
			addToken(type, line[startIndex*2+1]);
			startIndex++;
		} else {
			for (int i=startIndex; i<line.length/2; i++) {
				int tokenType = line[i*2];
				int tokenLength = line[i*2+1];
				if (tokenType != WsvTokenizer.TokenTypeStringStart && 
						tokenType != WsvTokenizer.TokenTypeStringEnd &&
						tokenType != WsvTokenizer.TokenTypeStringText &&
						tokenType != WsvTokenizer.TokenTypeStringEscapedDoubleQuote &&
						tokenType != WsvTokenizer.TokenTypeStringLineBreak) {
					break;
				}
				addToken(type, tokenLength);
				startIndex++;
			}
		}
		return startIndex;
	}
	
	private void tokenizeLine(int[] line) {
		int count = getValueCount(line);
		int startIndex = 0;
		if (count == 1) {
			startIndex = tokenizeFirstValue(line, TokenTypeElement);
		} else if (count > 1) {
			startIndex = tokenizeFirstValue(line, TokenTypeAttribute);
		}
		for (int i=startIndex; i<line.length/2; i++) {
			int tokenType = line[i*2];
			int tokenLength = line[i*2+1];
			addToken(tokenType, tokenLength);
		}
	}
	
	public int[] tokenizeDocument(String text) {
		int[] wsvTokens = WsvTokenizer.tokenize(text);
		splitIntoLines(wsvTokens);
		for (int[] line : lines) {
			tokenizeLine(line);
		}
		return toArray(tokens);
	}
	
	public static int[] tokenize(String text) {
		return new SmlTokenizer().tokenizeDocument(text);	
	}
}
