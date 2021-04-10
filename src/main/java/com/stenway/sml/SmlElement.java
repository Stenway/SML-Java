package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvLine;
import java.util.ArrayList;
import java.util.Optional;

public class SmlElement extends SmlNamedNode {
	public final ArrayList<SmlNode> Nodes = new ArrayList<SmlNode>();
	
	String[] endWhitespaces;
	String endComment;
	
	public SmlElement(String name) {
		super(name);
	}

	public final void setEndWhitespaces(String... whitespaces) {
		WsvLine.validateWhitespaces(whitespaces);
		this.endWhitespaces = whitespaces;
	}

	public String[] getEndWhitespaces() {
		return endWhitespaces.clone();
	}
	
	public final void setEndComment(String comment) {
		WsvLine.validateComment(comment);
		this.endComment = comment;
	}

	public String getEndComment() {
		return endComment;
	}
	
	void setEndWhitespacesAndComment(String[] whitespaces, String comment) {
		this.endWhitespaces = whitespaces;
		this.endComment = comment;
	}
	
	public SmlNode add(SmlNode node) {
		Nodes.add(node);
		return node;
	}
	
	public SmlAttribute addAttribute(String name, String... values) {
		SmlAttribute attribute = new SmlAttribute(name, values);
		add(attribute);
		return attribute;
	}
	
	public SmlAttribute addAttribute(String name, int... values) {
		SmlAttribute attribute = new SmlAttribute(name, values);
		add(attribute);
		return attribute;
	}
	
	public SmlAttribute addAttribute(String name, float... values) {
		SmlAttribute attribute = new SmlAttribute(name, values);
		add(attribute);
		return attribute;
	}
	
	public SmlAttribute addAttribute(String name, double... values) {
		SmlAttribute attribute = new SmlAttribute(name, values);
		add(attribute);
		return attribute;
	}
	
	public SmlAttribute addAttribute(String name, boolean... values) {
		SmlAttribute attribute = new SmlAttribute(name, values);
		add(attribute);
		return attribute;
	}
	
	public SmlAttribute addAttribute(String name, byte[] values) {
		SmlAttribute attribute = new SmlAttribute(name, values);
		add(attribute);
		return attribute;
	}
	
	public SmlElement addElement(String name) {
		SmlElement element = new SmlElement(name);
		add(element);
		return element;
	}
	
	public SmlEmptyNode addEmptyNode() {
		SmlEmptyNode emptyNode = new SmlEmptyNode();
		add(emptyNode);
		return emptyNode;
	}
	
	public SmlAttribute[] attributes() {
		return Nodes.stream()
				.filter(node -> node instanceof SmlAttribute)
				.map(node -> (SmlAttribute)node)
				.toArray(SmlAttribute[]::new);
	}
	
	public SmlAttribute[] attributes(String name) {
		return Nodes.stream()
				.filter(node -> node instanceof SmlAttribute)
				.map(node -> (SmlAttribute)node)
				.filter(attribute -> attribute.hasName(name))
				.toArray(SmlAttribute[]::new);
	}
	
	public SmlAttribute attribute(String name) {
		Optional<SmlAttribute> result = Nodes.stream()
				.filter(node -> node instanceof SmlAttribute)
				.map(node -> (SmlAttribute)node)
				.filter(attribute -> attribute.hasName(name))
				.findFirst();
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new RuntimeException("Element \""+getName()+"\" does not contain a \""+name+"\" attribute");
		}
	}
	
	public boolean hasAttribute(String name) {
		return Nodes.stream()
				.filter(node -> node instanceof SmlAttribute)
				.map(node -> (SmlAttribute)node)
				.filter(attribute -> attribute.hasName(name))
				.findFirst()
				.isPresent();
	}
	
	public SmlElement[] elements() {
		return Nodes.stream()
				.filter(node -> node instanceof SmlElement)
				.map(node -> (SmlElement)node)
				.toArray(SmlElement[]::new);
	}
	
	public SmlElement[] elements(String name) {
		return Nodes.stream()
				.filter(node -> node instanceof SmlElement)
				.map(node -> (SmlElement)node)
				.filter(element -> element.hasName(name))
				.toArray(SmlElement[]::new);
	}
	
	public SmlElement element(String name) {
		Optional<SmlElement> result = Nodes.stream()
				.filter(node -> node instanceof SmlElement)
				.map(node -> (SmlElement)node)
				.filter(element -> element.hasName(name))
				.findFirst();
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new RuntimeException("Element \""+getName()+"\" does not contain a \""+name+"\" element");
		}
	}
	
	public boolean hasElement(String name) {
		return Nodes.stream()
				.filter(node -> node instanceof SmlElement)
				.map(node -> (SmlElement)node)
				.filter(element -> element.hasName(name))
				.findFirst()
				.isPresent();
	}
	
	public double getDouble(String attributeName, double defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getDouble();
		} else {
			return defaultValue;
		}
	}
	
	public Double getDoubleOrNull(String attributeName) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getDouble();
		} else {
			return null;
		}
	}
	
	public double[] getDoubleValues(String attributeName, double[] defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getDoubleValues();
		} else {
			return defaultValue;
		}
	}
	
	public String getString(String attributeName, String defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getString();
		} else {
			return defaultValue;
		}
	}
	
	public String getString(String attributeName) {
		return attribute(attributeName).getString();
	}
	
	public String[] getStringValues(String attributeName) {
		return attribute(attributeName).getValues();
	}

	@Override
	public String toString() {
		return SmlSerializer.serializeElement(this);
	}
	
	@Override
	void toWsvLines(WsvDocument document, int level, String defaultIndentation, String endKeyword) {
		SmlSerializer.serializeElement(this, document, level, defaultIndentation, endKeyword);
	}
	
	@Override
	public void minify() {
		Object[] toRemoveList = Nodes.stream().filter(node -> node instanceof SmlEmptyNode).toArray();
		for (Object toRemove : toRemoveList) {
			Nodes.remove(toRemove);
		}
		whitespaces = null;
		comment = null;
		endWhitespaces = null;
		endComment = null;
		for (SmlNode node : Nodes) {
			node.minify();
		}
	}
}