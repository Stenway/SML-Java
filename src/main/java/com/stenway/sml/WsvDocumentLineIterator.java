package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvLine;
import java.io.IOException;

class WsvDocumentLineIterator implements WsvLineIterator {
	WsvDocument wsvDocument;
	String endKeyword;

	int index;

	public WsvDocumentLineIterator(WsvDocument wsvDocument, String endKeyword) {
		this.wsvDocument = wsvDocument;
		this.endKeyword = endKeyword;
	}

	@Override
	public String getEndKeyword() {
		return endKeyword;
	}

	@Override
	public boolean hasLine() {
		return index < wsvDocument.Lines.size();
	}

	@Override
	public boolean isEmptyLine() {
		return hasLine() && !wsvDocument.Lines.get(index).hasValues();
	}

	@Override
	public WsvLine getLine() {
		WsvLine line = wsvDocument.Lines.get(index);
		index++;
		return line;
	}

	@Override
	public String[] getLineAsArray() throws IOException {
		return getLine().Values;
	}

	@Override
	public String toString() {
		String result = "(" + index + "): ";
		if (hasLine()) {
			result += wsvDocument.Lines.get(index).toString();
		}
		return result;
	}

	@Override
	public int getLineIndex() {
		return index;
	}
}
