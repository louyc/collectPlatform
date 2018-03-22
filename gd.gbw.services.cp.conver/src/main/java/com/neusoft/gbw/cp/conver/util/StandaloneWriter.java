package com.neusoft.gbw.cp.conver.util;

import java.io.IOException;
import java.io.Writer;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class StandaloneWriter extends XMLWriter {

	public StandaloneWriter(Writer writer, OutputFormat format) {
		super(writer, format);
	}

	protected void writeDeclaration() throws IOException {
		OutputFormat format = getOutputFormat();
		String encoding = format.getEncoding();
		if (!format.isSuppressDeclaration()) {
			writer.write("<?xml version=\"1.0\"");
			if (!format.isOmitEncoding()) {
				if (encoding.equals("UTF8"))
					writer.write(" encoding=\"UTF-8\"");
				else
					writer.write(" encoding=\"" + encoding + "\"");
			}
			writer.write(" standalone=\"yes\"");
			writer.write("?>");
		}
	}
}