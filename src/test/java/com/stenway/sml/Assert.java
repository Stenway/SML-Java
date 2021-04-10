package com.stenway.sml;

import static org.junit.Assert.*;

public class Assert {
	public static void array_equals(byte[] actual, byte[] expected) {
		assertArrayEquals(expected, actual);
	}
	
	public static void array_equals(int[] actual, int[] expected) {
		assertArrayEquals(expected, actual);
	}
	
	public static void array_equals(Object[] actual, Object[] expected) {
		assertArrayEquals(expected, actual);
	}
	
	public static void equals(Object actual, Object expected) {
		assertEquals(expected, actual);
	}
	
	public static void equals(String actual, String expected) {
		assertEquals(expected, actual);
	}
	
	public static void equals(long actual, long expected) {
		assertEquals(expected, actual);
	}
	
	public static void isTrue(boolean actual) {
		assertTrue(actual);
	}
	
	public static void isFalse(boolean actual) {
		assertFalse(actual);
	}
}
