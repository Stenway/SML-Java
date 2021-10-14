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
		if (endWhitespaces == null) {
			return null;
		}
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
	
	public SmlAttribute addAttribute(String name, byte[]... values) {
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
			throw new IllegalArgumentException("Element \""+getName()+"\" does not contain a \""+name+"\" attribute");
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
			throw new IllegalArgumentException("Element \""+getName()+"\" does not contain a \""+name+"\" element");
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
	
	public SmlNamedNode[] nodes(String name) {
		return Nodes.stream()
				.filter(node -> node instanceof SmlNamedNode)
				.map(node -> (SmlNamedNode)node)
				.filter(attribute -> attribute.hasName(name))
				.toArray(SmlNamedNode[]::new);
	}
	
	public String getString(String attributeName) {
		return attribute(attributeName).getString();
	}
	
	public int getInt(String attributeName) {
		return attribute(attributeName).getInt();
	}
	
	public float getFloat(String attributeName) {
		return attribute(attributeName).getFloat();
	}
	
	public double getDouble(String attributeName) {
		return attribute(attributeName).getDouble();
	}
	
	public boolean getBoolean(String attributeName) {
		return attribute(attributeName).getBoolean();
	}
	
	public byte[] getBytes(String attributeName) {
		return attribute(attributeName).getBytes();
	}
	
	public String getString(String attributeName, String defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getString();
		} else {
			return defaultValue;
		}
	}
	
	public int getInt(String attributeName, int defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getInt();
		} else {
			return defaultValue;
		}
	}
	
	public float getFloat(String attributeName, float defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getFloat();
		} else {
			return defaultValue;
		}
	}
	
	public double getDouble(String attributeName, double defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getDouble();
		} else {
			return defaultValue;
		}
	}
	
	public boolean getBoolean(String attributeName, boolean defaultValue) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getBoolean();
		} else {
			return defaultValue;
		}
	}
	
	public byte[] getBytes(String attributeName, byte[] defaultValues) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getBytes();
		} else {
			return defaultValues;
		}
	}
	
	public String getStringOrNull(String attributeName) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getString();
		} else {
			return null;
		}
	}
	
	public Integer getIntOrNull(String attributeName) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getInt();
		} else {
			return null;
		}
	}
	
	public Float getFloatOrNull(String attributeName) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getFloat();
		} else {
			return null;
		}
	}
	
	public Double getDoubleOrNull(String attributeName) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getDouble();
		} else {
			return null;
		}
	}
	
	public Boolean getBooleanOrNull(String attributeName) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getBoolean();
		} else {
			return null;
		}
	}
	
	public byte[] getBytesOrNull(String attributeName) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getBytes();
		} else {
			return null;
		}
	}
	
	public String[] getValues(String attributeName) {
		return attribute(attributeName).getValues();
	}
	
	public int[] getIntValues(String attributeName) {
		return attribute(attributeName).getIntValues();
	}
	
	public float[] getFloatValues(String attributeName) {
		return attribute(attributeName).getFloatValues();
	}
	
	public double[] getDoubleValues(String attributeName) {
		return attribute(attributeName).getDoubleValues();
	}
	
	public boolean[] getBooleanValues(String attributeName) {
		return attribute(attributeName).getBooleanValues();
	}
	
	public byte[][] getBytesValues(String attributeName) {
		return attribute(attributeName).getBytesValues();
	}
		
	public String[] getValues(String attributeName, String[] defaultValues) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getValues();
		} else {
			return defaultValues;
		}
	}
	
	public int[] getIntValues(String attributeName, int[] defaultValues) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getIntValues();
		} else {
			return defaultValues;
		}
	}
	
	public float[] getFloatValues(String attributeName, float[] defaultValues) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getFloatValues();
		} else {
			return defaultValues;
		}
	}
	
	public double[] getDoubleValues(String attributeName, double[] defaultValues) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getDoubleValues();
		} else {
			return defaultValues;
		}
	}
	
	public boolean[] getBooleanValues(String attributeName, boolean[] defaultValues) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getBooleanValues();
		} else {
			return defaultValues;
		}
	}
	
	public byte[][] getBytesValues(String attributeName, byte[][] defaultValues) {
		if (hasAttribute(attributeName)) {
			return attribute(attributeName).getBytesValues();
		} else {
			return defaultValues;
		}
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