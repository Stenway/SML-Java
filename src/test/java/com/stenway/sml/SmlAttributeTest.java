package com.stenway.sml;

import org.junit.Test;

public class SmlAttributeTest {
	
	@Test
	public void setValues_InvalidValuesGiven_ShouldThrowException() {
		setValues_InvalidValuesGiven_ShouldThrowException(null);
		setValues_InvalidValuesGiven_ShouldThrowException(new String[]{});
	}
	
	private void setValues_InvalidValuesGiven_ShouldThrowException(String[] values) {
		try {
			SmlAttribute attribute = new SmlAttribute("Attribute", "Value");
			attribute.setValues(values);
		} catch (IllegalArgumentException e) {
			Assert.equals(e.getMessage(), "Values must contain at least one value");
			return;
		}
		throw new RuntimeException();
	}
	
	@Test
	public void strings() {
		SmlAttribute attribute = new SmlAttribute("Attribute", "Value1", "Value2", "Value3");
		Assert.equals(attribute.toString(), "Attribute Value1 Value2 Value3");
		
		Assert.array_equals(attribute.getValues(0), stringArray("Value1", "Value2", "Value3"));
		Assert.array_equals(attribute.getValues(1), stringArray("Value2", "Value3"));
		Assert.array_equals(attribute.getValues(2), stringArray("Value3"));
		Assert.array_equals(attribute.getValues(3), stringArray());
		
		Assert.equals(attribute.getString(), "Value1");
		Assert.equals(attribute.getString(0), "Value1");
		Assert.equals(attribute.getString(1), "Value2");
		Assert.equals(attribute.getString(2), "Value3");
		
		attribute.setValue("ValueX");
		Assert.equals(attribute.toString(), "Attribute ValueX");
	}
	
	@Test
	public void ints() {
		SmlAttribute attribute = new SmlAttribute("Attribute", 10, 20, 30);
		Assert.equals(attribute.toString(), "Attribute 10 20 30");
		
		Assert.array_equals(attribute.getIntValues(), intArray(10, 20, 30));
		Assert.array_equals(attribute.getIntValues(0), intArray(10, 20, 30));
		Assert.array_equals(attribute.getIntValues(1), intArray(20, 30));
		Assert.array_equals(attribute.getIntValues(2), intArray(30));
		Assert.array_equals(attribute.getIntValues(3), intArray());
		
		Assert.equals(attribute.getInt(), 10);
		Assert.equals(attribute.getInt(0), 10);
		Assert.equals(attribute.getInt(1), 20);
		Assert.equals(attribute.getInt(2), 30);
		
		attribute.setValue(100);
		Assert.equals(attribute.toString(), "Attribute 100");
	}
	
	@Test
	public void floats() {
		SmlAttribute attribute = new SmlAttribute("Attribute", 10.5f, 20.5f, 30.5f);
		Assert.equals(attribute.toString(), "Attribute 10.5 20.5 30.5");
		
		Assert.array_equals(attribute.getFloatValues(), floatArray(10.5f, 20.5f, 30.5f));
		Assert.array_equals(attribute.getFloatValues(0), floatArray(10.5f, 20.5f, 30.5f));
		Assert.array_equals(attribute.getFloatValues(1), floatArray(20.5f, 30.5f));
		Assert.array_equals(attribute.getFloatValues(2), floatArray(30.5f));
		Assert.array_equals(attribute.getFloatValues(3), floatArray());
		
		Assert.equals(attribute.getFloat(), 10.5f);
		Assert.equals(attribute.getFloat(0), 10.5f);
		Assert.equals(attribute.getFloat(1), 20.5f);
		Assert.equals(attribute.getFloat(2), 30.5f);
		
		attribute.setValue(100.5f);
		Assert.equals(attribute.toString(), "Attribute 100.5");
	}
	
	@Test
	public void doubles() {
		SmlAttribute attribute = new SmlAttribute("Attribute", 10.5, 20.5, 30.5);
		Assert.equals(attribute.toString(), "Attribute 10.5 20.5 30.5");
		
		Assert.array_equals(attribute.getDoubleValues(), doubleArray(10.5, 20.5, 30.5));
		Assert.array_equals(attribute.getDoubleValues(0), doubleArray(10.5, 20.5, 30.5));
		Assert.array_equals(attribute.getDoubleValues(1), doubleArray(20.5, 30.5));
		Assert.array_equals(attribute.getDoubleValues(2), doubleArray(30.5));
		Assert.array_equals(attribute.getDoubleValues(3), doubleArray());
		
		Assert.equals(attribute.getDouble(), 10.5);
		Assert.equals(attribute.getDouble(0), 10.5);
		Assert.equals(attribute.getDouble(1), 20.5);
		Assert.equals(attribute.getDouble(2), 30.5);
		
		attribute.setValue(100.5);
		Assert.equals(attribute.toString(), "Attribute 100.5");
	}
	
	@Test
	public void booleans() {
		SmlAttribute attribute = new SmlAttribute("Attribute", true, false, true);
		Assert.equals(attribute.toString(), "Attribute true false true");
		
		Assert.array_equals(attribute.getBooleanValues(), booleanArray(true, false, true));
		Assert.array_equals(attribute.getBooleanValues(0), booleanArray(true, false, true));
		Assert.array_equals(attribute.getBooleanValues(1), booleanArray(false, true));
		Assert.array_equals(attribute.getBooleanValues(2), booleanArray(true));
		Assert.array_equals(attribute.getBooleanValues(3), booleanArray());
		
		Assert.equals(attribute.getBoolean(), true);
		Assert.equals(attribute.getBoolean(0), true);
		Assert.equals(attribute.getBoolean(1), false);
		Assert.equals(attribute.getBoolean(2), true);
		
		attribute.setValue(false);
		Assert.equals(attribute.toString(), "Attribute false");
	}
	
	@Test
	public void base64() {
		byte[] data1 = new byte[]{0x41,0x42,0x43,0x61,0x62,0x63};
		byte[] data2 = new byte[]{0x42,0x43,0x44,0x62,0x63,0x64};
		
		SmlAttribute attribute = new SmlAttribute("Attribute", data1, data2);
		Assert.equals(attribute.toString(), "Attribute QUJDYWJj QkNEYmNk");
		
		Assert.array_equals(attribute.getBytesValues(), bytesArray(data1, data2));
		Assert.array_equals(attribute.getBytesValues(0), bytesArray(data1, data2));
		Assert.array_equals(attribute.getBytesValues(1), bytesArray(data2));
		Assert.array_equals(attribute.getBytesValues(2), bytesArray());
		
		Assert.array_equals(attribute.getBytes(), data1);
		Assert.array_equals(attribute.getBytes(0), data1);
		Assert.array_equals(attribute.getBytes(1), data2);
		
		attribute.setValue(data2);
		Assert.equals(attribute.toString(), "Attribute QkNEYmNk");
	}
	
	public static String[] stringArray(String... values) {
		return values;
	}
	
	public static int[] intArray(int... values) {
		return values;
	}
	
	public static float[] floatArray(float... values) {
		return values;
	}
	
	public static double[] doubleArray(double... values) {
		return values;
	}
	
	public static boolean[] booleanArray(boolean... values) {
		return values;
	}
	
	public static byte[][] bytesArray(byte[]... values) {
		return values;
	}
}
