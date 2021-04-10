package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.wsv.WsvString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SmlDocument {
	SmlElement root;
	ReliableTxtEncoding encoding;
	String endKeyword = "End";
	String defaultIndentation;
	
	public final ArrayList<SmlEmptyNode> EmptyNodesBefore = new ArrayList<>();
	public final ArrayList<SmlEmptyNode> EmptyNodesAfter = new ArrayList<>();
	
	SmlDocument() {
		
	}
	
	public SmlDocument(String rootName) {
		this(new SmlElement(rootName));
	}
	
	public SmlDocument(SmlElement root) {
		this(root, ReliableTxtEncoding.UTF_8);
	}
	
	public SmlDocument(SmlElement root, ReliableTxtEncoding encoding) {
		setRoot(root);
		setEncoding(encoding);
	}
	
	public final void setRoot(SmlElement root) {
		Objects.requireNonNull(root);
		this.root = root;
	}
	
	public SmlElement getRoot() {
		return root;
	}
	
	public final void setEncoding(ReliableTxtEncoding encoding) {
		Objects.requireNonNull(encoding);
		this.encoding = encoding;
	}
	
	public ReliableTxtEncoding getEncoding() {
		return encoding;
	}
	
	public void setDefaultIndentation(String defaultIndentation) {
		if (defaultIndentation != null && defaultIndentation.length() > 0 &&
				!WsvString.isWhitespace(defaultIndentation)) {
			throw new IllegalArgumentException(
					"Indentation value contains non whitespace character or line feed");
		}
		this.defaultIndentation = defaultIndentation;
	}
	
	public String getDefaultIndentation() {
		return defaultIndentation;
	}
	
	public void setEndKeyword(String endKeyword) {
		this.endKeyword = endKeyword;
	}
	
	public String getEndKeyword() {
		return endKeyword;
	}
	
	public void minify() {
		EmptyNodesBefore.clear();
		EmptyNodesAfter.clear();
		setDefaultIndentation("");
		setEndKeyword(null);
		root.minify();
	}
	
	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean preserveWhitespaceAndComments) {
		if (preserveWhitespaceAndComments) {
			return SmlSerializer.serializeDocument(this);
		} else {
			return SmlSerializer.serializeDocumentNonPreserving(this, false);
		}
	}
	
	public String toStringMinified() {
		return SmlSerializer.serializeDocumentNonPreserving(this, true);
	}
	
	public void save(String filePath) throws IOException {
		String content = toString();
		ReliableTxtDocument.save(content, encoding, filePath);
	}
	
	public static SmlDocument load(String filePath) throws IOException {
		ReliableTxtDocument txt = ReliableTxtDocument.load(filePath);
		SmlDocument document = parse(txt.getText());
		document.encoding = txt.getEncoding();
		return document;
	}

	public static SmlDocument parse(String content) throws IOException {
		return parse(content, true);
	}
	
	public static SmlDocument parse(String content, boolean preserveWhitespaceAndComments) throws IOException {
		if (preserveWhitespaceAndComments) {
			return SmlParser.parseDocument(content);
		} else {
			return SmlParser.parseDocumentNonPreserving(content);
		}
	}
}