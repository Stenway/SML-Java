package com.stenway.sml;

import com.stenway.wsv.WsvBasedFormat;
import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class SmlParser {
	private static final String ONLY_ONE_ROOT_ELEMENT_ALLOWED				= "Only one root element allowed";
	private static final String ROOT_ELEMENT_EXPECTED						= "Root element expected";
	private static final String INVALID_ROOT_ELEMENT_START					= "Invalid root element start";
	private static final String NULL_VALUE_AS_ELEMENT_NAME_IS_NOT_ALLOWED	= "Null value as element name is not allowed";
	private static final String NULL_VALUE_AS_ATTRIBUTE_NAME_IS_NOT_ALLOWED	= "Null value as attribute name is not allowed";
	private static final String END_KEYWORD_COULD_NOT_BE_DETECTED			= "End keyword could not be detected";
	
	public static SmlDocument parseDocument(String content) throws IOException {
		WsvDocument wsvDocument = WsvDocument.parse(content);
		String endKeyword = determineEndKeyword(wsvDocument);
		WsvLineIterator iterator = new WsvDocumentLineIterator(wsvDocument, endKeyword);
		
		SmlDocument document = new SmlDocument();
		document.setEndKeyword(endKeyword);
		
		SmlElement rootElement = readRootElement(iterator, document.EmptyNodesBefore);
		readElementContent(iterator, rootElement);
		document.setRoot(rootElement);
		
		readEmptyNodes(document.EmptyNodesAfter, iterator);
		if (iterator.hasLine()) {
			throw getException(iterator, ONLY_ONE_ROOT_ELEMENT_ALLOWED);
		}
		return document;
	}
	
	private static boolean equalIgnoreCase(String name1, String name2) {
		if (name1 == null) {
			return name1 == name2;
		}
		return name1.equalsIgnoreCase(name2);
	}
	
	public static SmlElement readRootElement(WsvLineIterator iterator, 
			ArrayList<SmlEmptyNode> emptyNodesBefore) throws IOException {
		readEmptyNodes(emptyNodesBefore, iterator);
		
		if (!iterator.hasLine()) {
			throw getException(iterator, ROOT_ELEMENT_EXPECTED);
		}
		WsvLine rootStartLine = iterator.getLine();
		if (!rootStartLine.hasValues() || rootStartLine.Values.length != 1 
				|| equalIgnoreCase(iterator.getEndKeyword(), rootStartLine.Values[0])) {
			throw getLastLineException(iterator, INVALID_ROOT_ELEMENT_START);
		}
		String rootElementName = rootStartLine.Values[0];
		if (rootElementName == null) {
			throw getLastLineException(iterator, NULL_VALUE_AS_ELEMENT_NAME_IS_NOT_ALLOWED);
		}
		SmlElement rootElement = new SmlElement(rootElementName);
		rootElement.setWhitespacesAndComment(WsvBasedFormat.getWhitespaces(rootStartLine), rootStartLine.getComment());
		return rootElement;
	}
	
	public static SmlNode readNode(WsvLineIterator iterator, SmlElement parentElement) throws IOException {
		SmlNode node;
		WsvLine line = iterator.getLine();
		if (line.hasValues()) {
			String name = line.Values[0];
			if (line.Values.length == 1) {
				if (equalIgnoreCase(iterator.getEndKeyword(),name)) {
					parentElement.setEndWhitespacesAndComment(WsvBasedFormat.getWhitespaces(line), line.getComment());
					return null;
				}
				if (name == null) {
					throw getLastLineException(iterator, NULL_VALUE_AS_ELEMENT_NAME_IS_NOT_ALLOWED);
				}
				SmlElement childElement = new SmlElement(name);
				childElement.setWhitespacesAndComment(WsvBasedFormat.getWhitespaces(line), line.getComment());

				readElementContent(iterator, childElement);

				node = childElement;
			} else {
				if (name == null) {
					throw getLastLineException(iterator, NULL_VALUE_AS_ATTRIBUTE_NAME_IS_NOT_ALLOWED);
				}
				String[] values = Arrays.copyOfRange(line.Values, 1, line.Values.length);
				SmlAttribute childAttribute = new SmlAttribute(name, values);
				childAttribute.setWhitespacesAndComment(WsvBasedFormat.getWhitespaces(line), line.getComment());

				node = childAttribute;
			}
		} else {
			SmlEmptyNode emptyNode = new SmlEmptyNode();
			emptyNode.setWhitespacesAndComment(WsvBasedFormat.getWhitespaces(line), line.getComment());

			node = emptyNode;
		}
		return node;
	}
	
	private static void readElementContent(WsvLineIterator iterator, SmlElement element) throws IOException {
		while (true) {
			if (!iterator.hasLine()) {
				throw getLastLineException(iterator, "Element \""+element.getName()+"\" not closed");
			}
			SmlNode node = readNode(iterator, element);
			if (node == null) {
				break;
			}
			element.add(node);
		}
	}
	
	private static void readEmptyNodes(ArrayList<SmlEmptyNode> nodes, WsvLineIterator iterator) throws IOException {
		while (iterator.isEmptyLine()) {
			SmlEmptyNode emptyNode = readEmptyNode(iterator);
			nodes.add(emptyNode);
		}
	}
	
	private static SmlEmptyNode readEmptyNode(WsvLineIterator iterator) throws IOException {
		WsvLine line = iterator.getLine();
		SmlEmptyNode emptyNode = new SmlEmptyNode();
		emptyNode.setWhitespacesAndComment(WsvBasedFormat.getWhitespaces(line), line.getComment());
		return emptyNode;
	}
	
	private static String determineEndKeyword(WsvDocument wsvDocument) {
		for (int i=wsvDocument.Lines.size()-1; i>=0; i--) {
			String[] values = wsvDocument.Lines.get(i).Values;
			if (values != null) {
				if (values.length == 1) {
					return values[0];
				} else if (values.length > 1) {
					break;
				}
			}
		}
		throw new SmlParserException(wsvDocument.Lines.size()-1, END_KEYWORD_COULD_NOT_BE_DETECTED);
	}
	
	private static SmlParserException getException(WsvLineIterator iterator, String message) {
		return new SmlParserException(iterator.getLineIndex(), message);
	}

	private static SmlParserException getLastLineException(WsvLineIterator iterator, String message) {
		return new SmlParserException(iterator.getLineIndex()-1, message);
	}
	
	public static SmlDocument parseDocumentNonPreserving(String content) throws IOException {
		String[][] wsvLines = WsvDocument.parseAsJaggedArray(content);
		return parseDocument(wsvLines);
	}
		
	public static SmlDocument parseDocument(String[][] wsvLines) throws IOException {
		String endKeyword = determineEndKeyword(wsvLines);
		WsvLineIterator iterator = new WsvJaggedArrayLineIterator(wsvLines, endKeyword);
		
		SmlDocument document = new SmlDocument();
		document.setEndKeyword(endKeyword);
		
		SmlElement rootElement = parseDocumentNonPreserving(iterator);
		document.setRoot(rootElement);
		
		return document;
	}
	
	private static SmlElement parseDocumentNonPreserving(WsvLineIterator iterator) throws IOException {
		skipEmptyLines(iterator);
		if (!iterator.hasLine()) {
			throw getException(iterator, ROOT_ELEMENT_EXPECTED);
		}
		
		SmlNode node = readNodeNonPreserving(iterator);
		if (!(node instanceof SmlElement)) {
			throw getLastLineException(iterator, INVALID_ROOT_ELEMENT_START);
		}
		
		skipEmptyLines(iterator);
		if (iterator.hasLine()) {
			throw getException(iterator, ONLY_ONE_ROOT_ELEMENT_ALLOWED);
		}

		return (SmlElement)node;
	}
	
	private static void skipEmptyLines(WsvLineIterator iterator) throws IOException {
		while (iterator.isEmptyLine()) {
			iterator.getLineAsArray();
		}
	}
	
	private static SmlNode readNodeNonPreserving(WsvLineIterator iterator) throws IOException {
		String[] line = iterator.getLineAsArray();
		
		String name = line[0];
		if (line.length == 1) {
			if (equalIgnoreCase(iterator.getEndKeyword(),name)) {
				return null;
			}
			if (name == null) {
				throw getLastLineException(iterator, NULL_VALUE_AS_ELEMENT_NAME_IS_NOT_ALLOWED);
			}
			SmlElement element = new SmlElement(name);
			readElementContentNonPreserving(iterator, element);
			return element;
		} else {
			if (name == null) {
				throw getLastLineException(iterator, NULL_VALUE_AS_ATTRIBUTE_NAME_IS_NOT_ALLOWED);
			}
			String[] values = Arrays.copyOfRange(line, 1, line.length);
			SmlAttribute attribute = new SmlAttribute(name, values);
			return attribute;
		}
	}
	
	private static void readElementContentNonPreserving(WsvLineIterator iterator, SmlElement element) throws IOException {
		while (true) {
			skipEmptyLines(iterator);
			if (!iterator.hasLine()) {
				throw getLastLineException(iterator, "Element \""+element.getName()+"\" not closed");
			}
			SmlNode node = readNodeNonPreserving(iterator);
			if (node == null) {
				break;
			}
			element.add(node);
		}
	}
	
	private static String determineEndKeyword(String[][] lines) {
		int i;
		for (i=lines.length-1; i>=0; i--) {
			String[] values = lines[i];
			if (values.length == 1) {
				return values[0];
			} else if (values.length > 1) {
				break;
			}
		}
		throw new SmlParserException(lines.length-1, END_KEYWORD_COULD_NOT_BE_DETECTED);
	}
}
