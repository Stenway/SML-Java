package com.stenway.sml;

import com.stenway.wsv.WsvLine;
import com.stenway.wsv.WsvStreamReader;
import java.io.IOException;
import java.util.Objects;

class WsvStreamLineIterator implements WsvLineIterator {
	WsvStreamReader reader;
	String endKeyword;
	WsvLine currentLine;

	int index;

	public WsvStreamLineIterator(WsvStreamReader reader, String endKeyword) throws IOException {
		Objects.requireNonNull(endKeyword);
		this.reader = reader;
		this.endKeyword = endKeyword;

		currentLine = reader.readLine();
	}

	@Override
	public boolean hasLine() {
		return currentLine != null;
	}

	@Override
	public boolean isEmptyLine() {
		return hasLine() && !currentLine.hasValues();
	}

	@Override
	public WsvLine getLine() throws IOException {
		WsvLine result = currentLine;
		currentLine = reader.readLine();
		index++;
		return result;
	}

	@Override
	public String[] getLineAsArray() throws IOException {
		return getLine().Values;
	}

	@Override
	public String getEndKeyword() {
		return endKeyword;
	}

	@Override
	public String toString() {
		String result = "(" + index + "): ";
		if (hasLine()) {
			result += currentLine.toString();
		}
		return result;
	}

	@Override
	public int getLineIndex() {
		return index;
	}
}
