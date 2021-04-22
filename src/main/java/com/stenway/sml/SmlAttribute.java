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
	
	public SmlAttribute(String name, byte[]... values) {
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
	
	public final void setValues(float... values) {
		String[] strValues = new String[values.length];
		for (int i=0; i<values.length; i++) {
			strValues[i] = String.valueOf(values[i]);
		}
		setValues(strValues);
	}
	
	public final void setValues(double... values) {
		String[] strValues = Arrays.stream(values)
				.mapToObj(String::valueOf)
				.toArray(String[]::new);
		setValues(strValues);
	}
	
	public final void setValues(boolean... values) {
		String[] strValues = new String[values.length];
		for (int i=0; i<values.length; i++) {
			strValues[i] = String.valueOf(values[i]);
		}
		setValues(strValues);
	}
	
	public final void setValues(byte[]... values) {
		String[] strValues = new String[values.length];
		for (int i=0; i<values.length; i++) {
			strValues[i] = Base64.getEncoder().encodeToString(values[i]);
		}
		setValues(strValues);
	}
	
	public String[] getValues() {
		return values;
	}
	
	public String[] getValues(int offset) {
		return Arrays.stream(values)
				.skip(offset)
				.toArray(String[]::new);
	}
	
	public int[] getIntValues() {
		return Arrays.stream(values)
				.mapToInt(Integer::parseInt)
				.toArray();
	}
	
	public int[] getIntValues(int offset) {
		return Arrays.stream(values)
				.skip(offset)
				.mapToInt(Integer::parseInt)
				.toArray();
	}
	
	public float[] getFloatValues() {
		return getFloatValues(0);
	}
	
	public float[] getFloatValues(int offset) {
		double[] values = getDoubleValues(offset);
		float[] result = new float[values.length];
		for (int i=0; i<values.length; i++) {
			result[i] = (float)values[i];
		}
		return result;
	}
	
	public double[] getDoubleValues() {
		return Arrays.stream(values)
				.mapToDouble(Double::parseDouble)
				.toArray();
	}
	
	public double[] getDoubleValues(int offset) {
		return Arrays.stream(values)
				.skip(offset)
				.mapToDouble(Double::parseDouble)
				.toArray();
	}
	
	public boolean[] getBooleanValues() {
		return getBooleanValues(0);
	}
	
	public boolean[] getBooleanValues(int offset) {
		String[] values = getValues(offset);
		boolean[] result = new boolean[values.length];
		for (int i=0; i<values.length; i++) {
			result[i] = Boolean.parseBoolean(values[i]);
		}
		return result;
	}
	
	public byte[][] getBytesValues() {
		return getBytesValues(0);
	}
	
	public byte[][] getBytesValues(int offset) {
		String[] values = getValues(offset);
		byte[][] result = new byte[values.length][];
		for (int i=0; i<values.length; i++) {
			result[i] = Base64.getDecoder().decode(values[i]);
		}
		return result;
	}
	
	public String getString() {
		return values[0];
	}
	
	public String getString(int index) {
		return values[index];
	}
	
	public int getInt() {
		return getInt(0);
	}
	
	public int getInt(int index) {
		return Integer.parseInt(values[index]);
	}
	
	public float getFloat() {
		return getFloat(0);
	}
	
	public float getFloat(int index) {
		return Float.parseFloat(values[index]);
	}
	
	public double getDouble() {
		return getDouble(0);
	}
	
	public double getDouble(int index) {
		return Double.parseDouble(values[index]);
	}
	
	public boolean getBoolean() {
		return getBoolean(0);
	}
	
	public boolean getBoolean(int index) {
		return Boolean.parseBoolean(values[index]);
	}
	
	public byte[] getBytes() {
		return getBytes(0);
	}
	
	public byte[] getBytes(int index) {
		return Base64.getDecoder().decode(values[index]);
	}
	
	public void setValue(String value) {
		setValues(value);
	}
	
	public void setValue(int value) {
		setValues(value);
	}
	
	public void setValue(float value) {
		setValues(value);
	}
	
	public void setValue(double value) {
		setValues(value);
	}
	
	public void setValue(boolean value) {
		setValues(value);
	}
	
	public void setValue(byte[] value) {
		setValues(value);
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