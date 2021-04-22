package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class SmlStreamWriterTest {
	@Test
	public void constructor() throws Exception {
		String filePath = "Test.sml";
		SmlDocument template = new SmlDocument("MyRootElement");
		try (SmlStreamWriter writer = new SmlStreamWriter(template, filePath)) {

		}
		try (SmlStreamWriter writer = new SmlStreamWriter(template, filePath, true)) {

		}
		try (SmlStreamWriter writer = new SmlStreamWriter(template, filePath, ReliableTxtEncoding.UTF_8)) {

		}
	}
	
	@Test
	public void constructor_append() throws IOException, Exception {
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_32);
		
	}
	
	private void constructor_append(ReliableTxtEncoding firstEncoding, ReliableTxtEncoding secondEncoding) throws IOException, Exception {
		String filePath = "Append.sml";
		deleteAppendFile(filePath);
		SmlDocument template = new SmlDocument("MyRootElement");
		try (SmlStreamWriter writer = new SmlStreamWriter(template, filePath, firstEncoding, true)) {
			writer.writeNode(new SmlAttribute("Attribute1", "Value"));
			Assert.equals(writer.Encoding, firstEncoding);
		}
		load(filePath, "MyRootElement\n\tAttribute1 Value\nEnd", firstEncoding);
		try (SmlStreamWriter writer = new SmlStreamWriter(template, filePath, secondEncoding, true)) {
			writer.writeNode(new SmlAttribute("Attribute2", "Value"));
			Assert.equals(writer.Encoding, firstEncoding);
		}
		load(filePath, "MyRootElement\n\tAttribute1 Value\n\tAttribute2 Value\nEnd", firstEncoding);
	}
	
	private void deleteAppendFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
	
	@Test
	public void constructor_ZeroByteFileGiven() throws IOException, Exception {
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_8);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_32);
	}
	
	private void constructor_ZeroByteFileGiven(ReliableTxtEncoding encoding) throws IOException, Exception {
		String filePath = "Append.sml";
		deleteAppendFile(filePath);
		Files.write(Paths.get(filePath), new byte[] {});
		SmlDocument template = new SmlDocument("MyRootElement");
		try (SmlStreamWriter writer = new SmlStreamWriter(template, filePath, encoding, true)) {
			writer.writeNode(new SmlAttribute("Attribute1", "Value"));
		}
		load(filePath, "MyRootElement\n\tAttribute1 Value\nEnd", encoding);
	}
	@Test
	public void constructor_InvalidFileGiven_ShouldThrowException() throws IOException, Exception {
		constructor_InvalidFileGiven_ShouldThrowException("MyRootElement", "End line expected (0)", ReliableTxtEncoding.UTF_8);
		constructor_InvalidFileGiven_ShouldThrowException("MyRootElement", "End line expected (0)", ReliableTxtEncoding.UTF_16);
		constructor_InvalidFileGiven_ShouldThrowException("MyRootElement", "End line expected (0)", ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_InvalidFileGiven_ShouldThrowException("MyRootElement", "End line expected (0)", ReliableTxtEncoding.UTF_32);
	}
	
	private void constructor_InvalidFileGiven_ShouldThrowException(String content, String expectedMessage, ReliableTxtEncoding encoding) throws IOException, Exception {
		try {
			String filePath = "Append.sml";
			deleteAppendFile(filePath);
			ReliableTxtDocument.save(content, encoding, filePath);
			SmlDocument template = new SmlDocument("MyRootElement");
			try (SmlStreamWriter writer = new SmlStreamWriter(template, filePath, ReliableTxtEncoding.UTF_16, true)) {

			}
		} catch (SmlParserException e) {
			Assert.equals(e.getMessage(), expectedMessage);
			return;
		}
		throw new RuntimeException();
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
}
