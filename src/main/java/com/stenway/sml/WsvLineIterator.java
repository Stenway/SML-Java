package com.stenway.sml;

import com.stenway.wsv.WsvLine;
import java.io.IOException;

interface WsvLineIterator {	
	boolean hasLine();
	boolean isEmptyLine();
	WsvLine getLine() throws IOException;
	String[] getLineAsArray() throws IOException;
	String getEndKeyword();
	int getLineIndex();
}
