package cn.com.pattek.octopus;
/**
 * Created by IntelliJ IDEA.
 * User: Gao Peng
 * Date: 2008-4-3
 * Time: 23:14:41
 * To change this template use File | Settings | File Templates.
 */
public class OctopusFile implements OctoPusFileFace{
    static {
    	System.loadLibrary("OctoFile4J");
    }
    
    private static class Holder {
    	private static final OctopusFile INSTANCE = new OctopusFile();
    }
    
    public static OctopusFile getInstance() {
    	return Holder.INSTANCE;
    }
    
//    private static OctopusFile instance = null;
    
    native public int init();
    native public void setParam(int type, int value);
    native public int exit();
    //mode = wb  ����0ʧ��
    native public int fopen(String fileName, String mode);
    native public byte[] fread(int size, int handle);
    native public int fwrite(byte[] buf, int handle);

    native public int ftell(int handle);
    native public int fseek(int handle, int offset, int origin);
    native public boolean feof(int handle);

    native public int fclose(int handle);

    native public int remove(String fileName);
    native public int rename(String oldName, String newName);

    native public int mkdir(String dirName);
    native public int rmdir(String dirName);
//	/**
//	 * @return the instance
//	 */
//	public static OctopusFile getInstance() {
//		if(instance==null)
//			instance = new OctopusFile();
//		return instance;
//	}
}
