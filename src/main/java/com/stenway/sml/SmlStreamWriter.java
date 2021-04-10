package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.wsv.WsvDocument;
import com.stenway.wsv.WsvStreamWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class SmlStreamWriter implements AutoCloseable {
	WsvStreamWriter writer;
	WsvDocument wsvDocument;
	String endKeyword = "End";
	String defaultIndentation;
	
	public final ReliableTxtEncoding Encoding;
	public final boolean AppendMode;
	
	public SmlStreamWriter(SmlDocument template, String filePath, ReliableTxtEncoding encoding,
			boolean append) throws IOException, Exception {
		Objects.requireNonNull(template);
		
		if (append) {
			Path path = Paths.get(filePath);
			if (!Files.exists(path) || Files.size(path) == 0) {
				append = false;
			}
		}
		writer = new WsvStreamWriter(filePath, encoding, append);
		Encoding = writer.Encoding;
		AppendMode = writer.AppendMode;
		
		if (append) {
			template.endKeyword = SmlFileAppend.removeEnd(filePath, Encoding);
		}
		
		wsvDocument = new WsvDocument();
		endKeyword = template.endKeyword;
		defaultIndentation = template.defaultIndentation;
		
		if (!append) {
			String rootElementName = template.getRoot().getName();
			writer.writeLine(rootElementName);
		}
	}
	
	public void writeNode(SmlNode node) throws IOException {
		wsvDocument.Lines.clear();
		node.toWsvLines(wsvDocument, 1, defaultIndentation, endKeyword);
		writer.writeLines(wsvDocument);
	}

	@Override
	public void close() throws Exception {
		writer.writeLine(endKeyword);
		writer.close();
	}
}