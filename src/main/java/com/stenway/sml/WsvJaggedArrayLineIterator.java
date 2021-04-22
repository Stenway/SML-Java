package com.stenway.sml;

import com.stenway.wsv.WsvLine;
import com.stenway.wsv.WsvSerializer;
import java.io.IOException;

class WsvJaggedArrayLineIterator implements WsvLineIterator {
	private final String[][] lines;
	String endKeyword;

	int index;

	public WsvJaggedArrayLineIterator(String[][] lines, String endKeyword) {
		this.lines = lines;
		this.endKeyword = endKeyword;
	}

	@Override
	public String getEndKeyword() {
		return endKeyword;
	}

	@Override
	public boolean hasLine() {
		return index < lines.length;
	}

	@Override
	public boolean isEmptyLine() {
		return hasLine() && (lines[index] == null || lines[index].length == 0);
	}

	@Override
	public WsvLine getLine() throws IOException {
		return new WsvLine(getLineAsArray());
	}

	@Override
	public String[] getLineAsArray() throws IOException {
		String[] line = lines[index];
		index++;
		return line;
	}

	@Override
	public String toString() {
		String result = "(" + index + "): ";
		if (hasLine()) {
			String[] line = lines[index];
			if (line != null) {
				result += WsvSerializer.serializeLine(line);
			}
		}
		return result;
	}

	@Override
	public int getLineIndex() {
		return index;
	}
}
