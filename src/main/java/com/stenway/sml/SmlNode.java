package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvLine;

public abstract class SmlNode {
	String[] whitespaces;
	String comment;
	
	public final void setWhitespaces(String... whitespaces) {
		WsvLine.validateWhitespaces(whitespaces);
		this.whitespaces = whitespaces;
	}

	public String[] getWhitespaces() {
		if (whitespaces == null) {
			return null;
		}
		return whitespaces.clone();
	}
	
	public final void setComment(String comment) {
		WsvLine.validateComment(comment);
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}
	
	void setWhitespacesAndComment(String[] whitespaces, String comment) {
		this.whitespaces = whitespaces;
		this.comment = comment;
	}
	
	abstract void toWsvLines(WsvDocument document, int level, String defaultIndentation, String endKeyword);
		
	public void minify() {
		whitespaces = null;
		comment = null;
	}
}