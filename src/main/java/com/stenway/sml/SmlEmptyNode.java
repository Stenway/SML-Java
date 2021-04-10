package com.stenway.sml;

import com.stenway.wsv.WsvDocument;

public class SmlEmptyNode extends SmlNode {
	@Override
	public String toString() {
		return SmlSerializer.serializeEmptyNode(this);
	}
	
	@Override
	void toWsvLines(WsvDocument document, int level, String defaultIndentation, String endKeyword) {
		SmlSerializer.serializeEmptyNode(this, document, level, defaultIndentation);
	}
}