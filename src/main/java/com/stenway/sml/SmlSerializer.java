package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvSerializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SmlSerializer {
	public static String serializeDocument(SmlDocument document) {
		WsvDocument wsvDocument = new WsvDocument();
		
		serialzeEmptyNodes(document.EmptyNodesBefore, wsvDocument);
		document.getRoot().toWsvLines(wsvDocument, 0, document.defaultIndentation, document.endKeyword);
		serialzeEmptyNodes(document.EmptyNodesAfter, wsvDocument);
		
		return wsvDocument.toString();
	}
	
	public static String serializeElement(SmlElement element) {
		WsvDocument wsvDocument = new WsvDocument();
		element.toWsvLines(wsvDocument, 0, null, "End");
		return wsvDocument.toString();
	}

	public static String serializeAttribute(SmlAttribute attribute) {
		WsvDocument wsvDocument = new WsvDocument();
		attribute.toWsvLines(wsvDocument, 0, null, null);
		return wsvDocument.toString();
	}
	
	public static String serializeEmptyNode(SmlEmptyNode emptyNode) {
		WsvDocument wsvDocument = new WsvDocument();
		emptyNode.toWsvLines(wsvDocument, 0, null, null);
		return wsvDocument.toString();
	}
	
	private static void serialzeEmptyNodes(ArrayList<SmlEmptyNode> emptyNodes, WsvDocument wsvDocument) {
		for (SmlEmptyNode emptyNode : emptyNodes) {
			emptyNode.toWsvLines(wsvDocument, 0, null, null);
		}
	}

	public static void serializeElement(SmlElement element, WsvDocument wsvDocument,
			int level, String defaultIndentation, String endKeyword) {
		int childLevel = level + 1;
		
		String[] whitespaces = getWhitespaces(element.whitespaces, level, defaultIndentation);
		wsvDocument.addLine(new String[]{element.getName()}, whitespaces, element.comment);
		
		for (SmlNode child : element.Nodes) {
			child.toWsvLines(wsvDocument, childLevel, defaultIndentation, endKeyword);
		}
		
		String[] endWhitespaces = getWhitespaces(element.endWhitespaces, level, defaultIndentation);
		wsvDocument.addLine(new String[]{endKeyword}, endWhitespaces, element.endComment);
	}
	
	private static String[] getWhitespaces(String[] whitespaces, int level, 
			String defaultIndentation) {
		if (whitespaces != null && whitespaces.length > 0) {
			return whitespaces;
		}
		if (defaultIndentation == null) {
			char[] indentChars = new char[level];
			Arrays.fill(indentChars, '\t');
			return new String[] { new String(indentChars) };
		} else {
			String indentStr = getIndentationString(defaultIndentation, level);
			return new String[] {indentStr};
		}
	}
	
	private static String getIndentationString(String defaultIndentation, int level) {
		//String indentStr = defaultIndentation.repeat(level);
		return String.join("", Collections.nCopies(level, defaultIndentation));
	}
	
	public static void serializeAttribute(SmlAttribute attribute, WsvDocument wsvDocument,
			int level, String defaultIndentation) {
		String[] whitespaces = getWhitespaces(attribute.whitespaces, level, defaultIndentation);
		String[] combined = combine(attribute.getName(), attribute.values);
		wsvDocument.addLine(combined, whitespaces, attribute.comment);
	}
	
	private static String[] combine(String name, String[] values) {
		String[] result = new String[values.length + 1];
		result[0] = name;
		System.arraycopy(values, 0, result, 1, values.length);
		return result;
	}
	
	public static void serializeEmptyNode(SmlEmptyNode emptyNode, WsvDocument wsvDocument, 
			int level, String defaultIndentation) {
		String[] whitespaces = getWhitespaces(emptyNode.whitespaces, level, defaultIndentation);
		wsvDocument.addLine(null, whitespaces, emptyNode.comment);
	}
	
	public static String serializeDocumentNonPreserving(SmlDocument document) {
		return serializeDocumentNonPreserving(document, false);
	}
	
	public static String serializeDocumentNonPreserving(SmlDocument document, boolean minified) {
		StringBuilder sb = new StringBuilder();
		String defaultIndentation = document.getDefaultIndentation();
		if (defaultIndentation == null) {
			defaultIndentation = "\t";
		}
		String endKeyword = document.getEndKeyword();
		if (minified) {
			defaultIndentation = "";
			endKeyword = null;
		}
		serializeElementNonPreserving(sb, document.getRoot(), 0, defaultIndentation, endKeyword);
		sb.setLength(sb.length()-1);
		return sb.toString();
	}

	private static void serializeElementNonPreserving(StringBuilder sb, SmlElement element,
			int level, String defaultIndentation, String endKeyword) {
		serializeIndentation(sb, level, defaultIndentation);
		WsvSerializer.serializeValue(sb, element.getName());
		sb.append('\n'); 

		int childLevel = level + 1;
		for (SmlNode child : element.Nodes) {
			if (child instanceof SmlElement) {
				serializeElementNonPreserving(sb, (SmlElement)child, childLevel, defaultIndentation, endKeyword);
			} else if (child instanceof SmlAttribute) {
				serializeAttributeNonPreserving(sb, (SmlAttribute)child, childLevel, defaultIndentation);
			}
		}
		
		serializeIndentation(sb, level, defaultIndentation);
		WsvSerializer.serializeValue(sb, endKeyword);
		sb.append('\n'); 
	}
	
	private static void serializeAttributeNonPreserving(StringBuilder sb, SmlAttribute attribute,
			int level, String defaultIndentation) {
		serializeIndentation(sb, level, defaultIndentation);
		WsvSerializer.serializeValue(sb, attribute.getName());
		sb.append(' '); 
		WsvSerializer.serializeLine(sb, attribute.getValues());
		sb.append('\n'); 
	}
	
	private static void serializeIndentation(StringBuilder sb, int level, String defaultIndentation) {
		String indentStr = getIndentationString(defaultIndentation, level);
		sb.append(indentStr);
	}
}