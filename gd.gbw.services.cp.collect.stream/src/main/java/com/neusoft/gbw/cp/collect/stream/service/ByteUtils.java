package com.neusoft.gbw.cp.collect.stream.service;

public class ByteUtils {

	public static int getLength(byte[] bytes) {
		byte[] lengthArray = getBytes(bytes, 4, 9);
		return bytesToInt(lengthArray);
	}
	
//	public static String getId(byte[] bytes) {
////		byte[] idArray = getBytes(bytes, 4, 0);
//		byte[] idArray = getBytes(bytes, 8, 0);
//		return new String(idArray);
//	}

	public static long getId(byte[] bytes) {
		byte[] idArray = getBytes(bytes, 8, 0);
		return bytes2Long(idArray);
	}
	
	public static int getStatus(byte[] bytes) {
		byte[] statusArray = getBytes(bytes, 1, 8);
		return byte2int(statusArray[0]);
	}
	
	private static int bytesToInt(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
	}
	
	private static int byte2int(byte b) {
		int mask = 0xff;
		int temp = 0;
		int res = 0;
		res <<= 8;
		temp = b & mask;
		res |= temp;
		return res;
	}
	
	private static byte[] getBytes(byte[] head, int length, int src) {
		byte[] bytes = new byte[length];
		System.arraycopy(head, src, bytes, 0, length);
		return bytes;
	}
	
	private static long bytes2Long(byte[] b) {
		byte[] a = new byte[8];
		int i = a.length-1,j = b.length-1;
		for(; i >= 0; i-- ,j--) {
			if(j >= 0) 
				a[i] = b[j];
			else
				a[i] = 0;
		}
		long v0 = (long)(a[0] & 0xff) << 56;
		long v1 = (long)(a[1] & 0xff) << 48;
		long v2 = (long)(a[2] & 0xff) << 40;
		long v3 = (long)(a[3] & 0xff) << 32;
		long v4 = (long)(a[4] & 0xff) << 24;
		long v5 = (long)(a[5] & 0xff) << 16;
		long v6 = (long)(a[6] & 0xff) << 8;
		long v7 = (long)(a[7] & 0xff) ;
		return v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7;
	}
}
