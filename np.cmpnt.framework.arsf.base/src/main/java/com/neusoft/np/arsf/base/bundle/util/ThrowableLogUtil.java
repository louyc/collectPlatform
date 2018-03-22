package com.neusoft.np.arsf.base.bundle.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class ThrowableLogUtil {

	public static String getThrowableLog(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.flush();
		LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
		StringBuffer buffer = new StringBuffer();
		try {
			String line = reader.readLine();
			while (line != null) {
				buffer.append(line);
				line = reader.readLine();
			}
		} catch (IOException ex) {
			buffer.append(ex.toString());
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
		try {
			throw new NullPointerException();
		} catch (Exception e) {
			System.out.println(getThrowableLog(e));
		}
	}

}
