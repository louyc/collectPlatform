package cn.com.pattek.octopus;

public interface OctoPusFileFace {
	
	  public int fopen(String paramString1, String paramString2);

	  public byte[] fread(int paramInt1, int paramInt2);

	  public int fclose(int paramInt);
}
