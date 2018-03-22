package cn.com.pattek.octopus;

public class OctopusFileTS implements OctoPusFileFace{
    static {
    	System.loadLibrary("JAudioCapTS");
	}
    
    private static class Holder {
    	private static final OctopusFileTS INSTANCE = new OctopusFileTS();
    }
    
    public static OctopusFileTS getInstance() {
    	return Holder.INSTANCE;
    }
    
    native public int fopen(String fileName, String mode);
    native public byte[] fread(int size, int handle);
    native public int fclose(int handle);
    native public String GetErrorInfo();
}
