package com.stenway.sml;

import com.stenway.wsv.WsvDocument;
import java.util.Arrays;
import java.util.Base64;

public class SmlAttribute extends SmlNamedNode {
	String[] values;
	
	public SmlAttribute(String name, String... values) {
		super(name);
		setValues(values);
	}
	
	public SmlAttribute(String name, int... values) {
		super(name);
		setValues(values);
	}
	
	public SmlAttribute(String name, float... values) {
		super(name);
		setValues(values);
	}
	
	public SmlAttribute(String name, double... values) {
		super(name);
		setValues(values);
	}
	
	public SmlAttribute(String name, boolean... values) {
		super(name);
		setValues(values);
	}
	
	public SmlAttribute(String name, byte[] values) {
		super(name);
		setValues(values);
	}
	
	public final void setValues(String... values) {
		if (values == null || values.length == 0) {
			throw new IllegalArgumentException("Values must contain at least one value");
		}
		this.values = values;
	}
	
	public final void setValues(int... values) {
		String[] strValues = Arrays.stream(values)
				.mapToObj(String::valueOf)
				.toArray(String[]::new);
		setValues(strValues);
	}
	
	public final void setValues(double... values) {
		String[] strValues = Arrays.stream(values)
				.mapToObj(String::valueOf)
				.toArray(String[]::new);
		setValues(strValues);
	}
	
	public final void setValues(float... values) {
		String[] strValues = new String[values.length];
		for (int i=0; i<values.length; i++) {
			strValues[i] = String.valueOf(values[i]);
		}
		setValues(strValues);
	}
	
	public final void setValues(boolean... values) {
		String[] strValues = new String[values.length];
		for (int i=0; i<values.length; i++) {
			strValues[i] = String.valueOf(values[i]);
		}
		setValues(strValues);
	}
	
	public final void setValues(byte[] values) {
		String strBase64 = Base64.getEncoder().encodeToString(values);
		String[] strValues = new String[] {strBase64};
		setValues(strValues);
	}
	
	public String[] getValues() {
		return values;
	}
	
	public String[] getValues(int offset) {
		return (String[]) Arrays.stream(values).skip(offset).toArray();
	}
	
	public double[] getDoubleValues() {
		return Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
	}
	
	public double[] getDoubleValues(int offset) {
		return Arrays.stream(values).skip(offset).mapToDouble(Double::parseDouble).toArray();
	}
	
	public int[] getIntValues() {
		return Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
	}
	
	public String getString() {
		return values[0];
	}
	
	public double getDouble() {
		return Double.parseDouble(values[0]);
	}
	
	public String getString(int index) {
		return values[index];
	}
	
	public void setString(String value) {
		values = new String[] {value};
	}
	
	@Override
	public String toString() {
		return SmlSerializer.serializeAttribute(this);
	}
	
	@Override
	void toWsvLines(WsvDocument document, int level, String defaultIndentation, String endKeyword) {
		SmlSerializer.serializeAttribute(this, document, level, defaultIndentation);
	}
}