package com.neusoft.np.arsf.core.transfer.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class NMSocketException extends Exception {
	
	private static final long serialVersionUID = -8653299855469124169L;

	public NMSocketException(String message) {
        super(message);
    }

    public NMSocketException(String message,Throwable e) {
        super(message,e);
    }
   
    public NMSocketException(Exception e) {
        super(e);
    }

    public NMSocketException(Throwable e) {
        super(e);
    }

    /**
     * 获得堆栈信息串。
     * 
     * @return 堆栈信息串
     */
    public String getStackTraceString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        this.printStackTrace(pw);
        return sw.toString();
    }
}
