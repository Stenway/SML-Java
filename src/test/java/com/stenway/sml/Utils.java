package com.stenway.sml;

public class Utils {
	public static byte[] byteArray(int... values) {
		byte[] bytes = new byte[values.length];
		for (int i=0; i<values.length; i++) {
			bytes[i] = (byte)values[i];
		}
		return bytes;
	}
	
	public static String[] stringArray(String... values) {
		return values;
	}
	
	public static int[] intArray(int... values) {
		return values;
	}
}
