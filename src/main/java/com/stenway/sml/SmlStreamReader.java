package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.wsv.WsvStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class SmlStreamReader implements AutoCloseable {
	public final ReliableTxtEncoding Encoding;
	WsvStreamReader reader;
	final String endKeyword;
	public final SmlElement Root;
	WsvStreamLineIterator iterator;
	
	public final ArrayList<SmlEmptyNode> EmptyNodesBefore = new ArrayList<>();
	
	public SmlStreamReader(String filePath) throws IOException {
		this(filePath, null);
	}
	
	public SmlStreamReader(String filePath, String endKeyword) throws IOException {
		reader = new WsvStreamReader(filePath);
		Encoding = reader.Encoding;
		if (endKeyword == null) {
			endKeyword = "End";
		}
		this.endKeyword = endKeyword;
		
		iterator = new WsvStreamLineIterator(reader, endKeyword);
		
		Root = SmlParser.readRootElement(iterator, EmptyNodesBefore);
	}
	
	public SmlNode readNode() throws IOException {
		return SmlParser.readNode(iterator, Root);
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}
}