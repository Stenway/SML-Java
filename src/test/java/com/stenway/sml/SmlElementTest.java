package com.stenway.sml;

import org.junit.Test;

public class SmlElementTest {
	@Test
	public void setEndWhitespaces() {
		SmlElement element = new SmlElement("Element");
		Assert.array_equals(element.getEndWhitespaces(), null);
		
		element.setEndWhitespaces("  ", "   ");
		Assert.array_equals(element.getEndWhitespaces(), stringArray("  ", "   "));
		
		Assert.equals(element.toString(), "Element\n  End   ");
	}
	
	@Test
	public void setEndComment() {
		SmlElement element = new SmlElement("Element");
		Assert.equals(element.getEndComment(), null);
		
		element.setEndComment("Comment");
		Assert.equals(element.getEndComment(), "Comment");
		
		Assert.equals(element.toString(), "Element\nEnd #Comment");
	}
	
	@Test
	public void addAttribute() {
		SmlElement element = getTestElement();
		Assert.equals(element.toString(), "Element\n\tAttribute1 Value1 Value2 Value3\n\tAttribute2 10 20 30\n\tAttribute3 10.5 20.5 30.5\n\tAttribute4 10.5 20.5 30.5\n\tAttribute5 true false true\n\tAttribute6 QUJDYWJj QkNEYmNk\nEnd");
	}
	
	private SmlElement getTestElement() {
		SmlElement element = new SmlElement("Element");
		element.addAttribute("Attribute1", "Value1", "Value2", "Value3");
		element.addAttribute("Attribute2", 10, 20, 30);
		element.addAttribute("Attribute3", 10.5f, 20.5f, 30.5f);
		element.addAttribute("Attribute4", 10.5, 20.5, 30.5);
		element.addAttribute("Attribute5", true, false, true);
		element.addAttribute("Attribute6", new byte[]{0x41,0x42,0x43,0x61,0x62,0x63}, new byte[]{0x42,0x43,0x44,0x62,0x63,0x64});
		return element;
	}
	
	@Test
	public void addElement() {
		SmlElement element = new SmlElement("Element");
		element.addElement("SubElement");
		
		Assert.equals(element.toString(), "Element\n\tSubElement\n\tEnd\nEnd");
	}
	
	@Test
	public void addEmptyNode() {
		SmlElement element = new SmlElement("Element");
		element.addEmptyNode();
		
		Assert.equals(element.toString(), "Element\n\t\nEnd");
	}
	
	@Test
	public void attributes() {
		SmlElement element = new SmlElement("Element");
		element.addAttribute("Attribute1", "Value1", "Value2", "Value3");
		element.addAttribute("Attribute1", "Value4", "Value5");
		element.addAttribute("Attribute2", "Value6", "Value7");
		
		Assert.equals(element.attributes().length, 3);
		Assert.equals(element.attributes("Attribute1").length, 2);
		Assert.equals(element.attributes("Attribute2").length, 1);
		Assert.equals(element.attributes("Attribute3").length, 0);
	}
	
	@Test
	public void attribute() {
		SmlElement element = new SmlElement("Element");
		element.addAttribute("Attribute1", "Value1", "Value2", "Value3");
		element.addAttribute("Attribute1", "Value4", "Value5");
		element.addAttribute("Attribute2", "Value6", "Value7");
		
		Assert.equals(element.attribute("Attribute1").getString(), "Value1");
	}
	
	@Test
	public void attribute_NoMatchingAttribute_ShouldThrowException() {
		try {
			SmlElement element = new SmlElement("Element");
			element.attribute("Attribute1");
		} catch (IllegalArgumentException e) {
			Assert.equals(e.getMessage(), "Element \"Element\" does not contain a \"Attribute1\" attribute");
			return;
		}
		throw new RuntimeException();
	}
	
	@Test
	public void hasAttribute() {
		SmlElement element = new SmlElement("Element");
		element.addAttribute("Attribute1", "Value1", "Value2", "Value3");
		element.addAttribute("Attribute1", "Value4", "Value5");
		element.addAttribute("Attribute2", "Value6", "Value7");
		
		Assert.isTrue(element.hasAttribute("Attribute1"));
		Assert.isTrue(element.hasAttribute("Attribute2"));
		Assert.isFalse(element.hasAttribute("Attribute3"));
	}
	
	@Test
	public void elements() {
		SmlElement element = new SmlElement("Element");
		element.addElement("Element1");
		element.addElement("Element1");
		element.addElement("Element2");
		
		Assert.equals(element.elements().length, 3);
		Assert.equals(element.elements("Element1").length, 2);
		Assert.equals(element.elements("Element2").length, 1);
		Assert.equals(element.elements("Element3").length, 0);
	}
	
	@Test
	public void element() {
		SmlElement element = new SmlElement("Element");
		element.addElement("Element1");
		element.addElement("Element1");
		element.addElement("Element2");
		
		Assert.equals(element.element("Element1").getName(), "Element1");
	}
	
	@Test
	public void element_NoMatchingElement_ShouldThrowException() {
		try {
			SmlElement element = new SmlElement("Element");
			element.element("Element1");
		} catch (IllegalArgumentException e) {
			Assert.equals(e.getMessage(), "Element \"Element\" does not contain a \"Element1\" element");
			return;
		}
		throw new RuntimeException();
	}
	
	@Test
	public void hasElement() {
		SmlElement element = new SmlElement("Element");
		element.addElement("Element1");
		element.addElement("Element1");
		element.addElement("Element2");
		
		Assert.isTrue(element.hasElement("Element1"));
		Assert.isTrue(element.hasElement("Element2"));
		Assert.isFalse(element.hasElement("Element3"));
	}
	
	@Test
	public void getX() {
		SmlElement element = getTestElement();
		Assert.equals(element.getString("Attribute1"), "Value1");
		Assert.equals(element.getInt("Attribute2"), 10);
		Assert.equals(element.getFloat("Attribute3"), 10.5f);
		Assert.equals(element.getDouble("Attribute4"), 10.5);
		Assert.equals(element.getBoolean("Attribute5"), true);
		Assert.array_equals(element.getBytes("Attribute6"), new byte[]{0x41,0x42,0x43,0x61,0x62,0x63});
	}
	
	@Test
	public void getX_DefaultValueGiven() {
		SmlElement element = new SmlElement("Element");
		Assert.equals(element.getString("Attribute1", "Value1"), "Value1");
		Assert.equals(element.getInt("Attribute2", 100), 100);
		Assert.equals(element.getFloat("Attribute3", 100.5f), 100.5f);
		Assert.equals(element.getDouble("Attribute4", 100.5), 100.5);
		Assert.equals(element.getBoolean("Attribute5", false), false);
		Assert.array_equals(element.getBytes("Attribute6", new byte[]{0x42,0x43,0x44,0x62,0x63,0x64}), new byte[]{0x42,0x43,0x44,0x62,0x63,0x64});
		
		element = getTestElement();
		Assert.equals(element.getString("Attribute1", "Value1"), "Value1");
		Assert.equals(element.getInt("Attribute2", 100), 10);
		Assert.equals(element.getFloat("Attribute3", 100.5f), 10.5f);
		Assert.equals(element.getDouble("Attribute4", 100.5), 10.5);
		Assert.equals(element.getBoolean("Attribute5", false), true);
		Assert.array_equals(element.getBytes("Attribute6", new byte[]{0x42,0x43,0x44,0x62,0x63,0x64}), new byte[]{0x41,0x42,0x43,0x61,0x62,0x63});
	}
	
	@Test
	public void getXOrNull() {
		SmlElement element = new SmlElement("Element");
		Assert.equals(element.getStringOrNull("Attribute1"), null);
		Assert.equals(element.getIntOrNull("Attribute2"), null);
		Assert.equals(element.getFloatOrNull("Attribute3"), null);
		Assert.equals(element.getDoubleOrNull("Attribute4"), null);
		Assert.equals(element.getBooleanOrNull("Attribute5"), null);
		Assert.array_equals(element.getBytesOrNull("Attribute6"), null);
		
		element = getTestElement();
		Assert.equals(element.getStringOrNull("Attribute1"), "Value1");
		Assert.equals(element.getIntOrNull("Attribute2"), (Integer)10);
		Assert.equals(element.getFloatOrNull("Attribute3"), 10.5f);
		Assert.equals(element.getDoubleOrNull("Attribute4"), 10.5);
		Assert.equals(element.getBooleanOrNull("Attribute5"), true);
		Assert.array_equals(element.getBytesOrNull("Attribute6"), new byte[]{0x41,0x42,0x43,0x61,0x62,0x63});
	}
	
	@Test
	public void getXValues() {
		SmlElement element = getTestElement();
		Assert.array_equals(element.getValues("Attribute1"), stringArray("Value1", "Value2", "Value3"));
		Assert.array_equals(element.getIntValues("Attribute2"), intArray(10, 20, 30));
		Assert.array_equals(element.getFloatValues("Attribute3"), floatArray(10.5f, 20.5f, 30.5f));
		Assert.array_equals(element.getDoubleValues("Attribute4"), doubleArray(10.5, 20.5, 30.5));
		Assert.array_equals(element.getBooleanValues("Attribute5"), booleanArray(true, false, true));
		Assert.array_equals(element.getBytesValues("Attribute6"), bytesArray(new byte[]{0x41,0x42,0x43,0x61,0x62,0x63}, new byte[]{0x42,0x43,0x44,0x62,0x63,0x64}));
	}
	
	@Test
	public void getXValues_DefaultGiven() {
		SmlElement element = new SmlElement("Element");
		Assert.array_equals(element.getValues("Attribute1", null), null);
		Assert.array_equals(element.getIntValues("Attribute2", null), null);
		Assert.array_equals(element.getFloatValues("Attribute3", null), null);
		Assert.array_equals(element.getDoubleValues("Attribute4", null), null);
		Assert.array_equals(element.getBooleanValues("Attribute5", null), null);
		Assert.array_equals(element.getBytesValues("Attribute6", null), null);
	
		 element = getTestElement();
		Assert.array_equals(element.getValues("Attribute1", null), stringArray("Value1", "Value2", "Value3"));
		Assert.array_equals(element.getIntValues("Attribute2", null), intArray(10, 20, 30));
		Assert.array_equals(element.getFloatValues("Attribute3", null), floatArray(10.5f, 20.5f, 30.5f));
		Assert.array_equals(element.getDoubleValues("Attribute4", null), doubleArray(10.5, 20.5, 30.5));
		Assert.array_equals(element.getBooleanValues("Attribute5", null), booleanArray(true, false, true));
		Assert.array_equals(element.getBytesValues("Attribute6", null), bytesArray(new byte[]{0x41,0x42,0x43,0x61,0x62,0x63}, new byte[]{0x42,0x43,0x44,0x62,0x63,0x64}));
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
