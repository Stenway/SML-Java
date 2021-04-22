package com.stenway.sml;

import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.wsv.WsvLine;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

class SmlFileAppend {
	static class ReverseLineIterator implements AutoCloseable {
		RandomAccessFile file;
		long index;
		Charset charset;
		ReliableTxtEncoding encoding;
				
		public ReverseLineIterator(String filePath, ReliableTxtEncoding encoding) throws FileNotFoundException, IOException {
			file = new RandomAccessFile(filePath, "rw");
			index = file.length();
			charset = encoding.getCharset();
			this.encoding = encoding;
		}
		
		private void readLeadingZeros() throws IOException {
			int expectedCount = 0;
			if (encoding == ReliableTxtEncoding.UTF_16) {
				expectedCount = 1;
			} else if (encoding == ReliableTxtEncoding.UTF_32) {
				expectedCount = 3;
			}
			for (int i=0; i<expectedCount; i++) {
				index--;
				file.seek(index);
				byte b = file.readByte();
				if (b != 0) {
					throw new SmlParserException(-1, "New line character detection failed");
				}
			}
		}
		
		public String getLine() throws IOException {
			int numBytes = 0;
			while (index > 0) {
				index--;
				file.seek(index);
				byte b = file.readByte();
				if (b == '\n') {
					long lineStart = index+1;
					readLeadingZeros();
					if (encoding == ReliableTxtEncoding.UTF_16_REVERSE) {
						lineStart++;
						numBytes--;
					}
					return getLine(lineStart, numBytes);
				}
				numBytes++;
			}
			throw new SmlParserException(-1, "End line expected");
		}
		
		private String getLine(long lineStart, int numBytes) throws IOException {
			if (numBytes == 0) {
				return "";
			}
			file.seek(lineStart);
			byte[] bytes = new byte[numBytes];
			file.read(bytes);
			return new String(bytes, charset);
		}
		
		public void truncate() throws IOException {
			file.setLength(index);
		}

		@Override
		public void close() throws Exception {
			file.close();
		}
	}
	
	public static String removeEnd(String filePath, ReliableTxtEncoding encoding) throws FileNotFoundException, IOException, Exception {
		
		String endKeyword;
		try (ReverseLineIterator iterator = new ReverseLineIterator(filePath, encoding)) {
			while (true) {
				String lineStr = iterator.getLine();
				WsvLine line = WsvLine.parse(lineStr);
				if (line.hasValues()) {
					if (line.Values.length > 1) {
						throw new SmlParserException(-1, "Invalid end line");
					}
					endKeyword = line.Values[0];
					break;
				}
			}
			iterator.truncate();
		}
		return endKeyword;
	}
}
