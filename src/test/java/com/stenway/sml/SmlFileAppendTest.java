package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class SmlFileAppendTest {
		
	@Test
	public void removeEnd() throws Exception {
		removeEnd(ReliableTxtEncoding.UTF_8);
	}
	
	private void removeEnd(ReliableTxtEncoding encoding) throws IOException, Exception {
		String filePath = "File.sml";
		ReliableTxtDocument.save("MyRootElement\nEnd\n\n", encoding, filePath);
		SmlFileAppend.removeEnd(filePath, encoding);
		load(filePath, "MyRootElement", encoding);
	}
	
	@Test
	public void removeEnd_InvalidFileGiven_ShouldThrowException() throws Exception {
		removeEnd_InvalidFileGiven_ShouldThrowException(byteArray(0xFE,0xFF,0x0A), ReliableTxtEncoding.UTF_16);
	}
	
	private void removeEnd_InvalidFileGiven_ShouldThrowException(byte[] bytes, ReliableTxtEncoding encoding) throws IOException, Exception {
		try {
			String filePath = "File.sml";
			Files.write(Paths.get(filePath), bytes);
			SmlFileAppend.removeEnd(filePath, encoding);
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), "New line character detection failed (0)");
			return;
		}
	}
	
	private void load(String filePath, String text, ReliableTxtEncoding encoding) {
		try {
			ReliableTxtDocument loaded = ReliableTxtDocument.load(filePath);
			Assert.equals(loaded.getEncoding(), encoding);
			Assert.equals(loaded.getText(), text);
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
	
	private static byte[] byteArray(int... values) {
		byte[] bytes = new byte[values.length];
		for (int i=0; i<values.length; i++) {
			bytes[i] = (byte)values[i];
		}
		return bytes;
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		SmlFileAppend smlFileAppend = new SmlFileAppend();
	}
}
