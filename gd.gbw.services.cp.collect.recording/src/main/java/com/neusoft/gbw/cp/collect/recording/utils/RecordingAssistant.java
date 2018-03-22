package com.neusoft.gbw.cp.collect.recording.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

import com.neusoft.gbw.cp.collect.recording.constant.RecordConstants;
import com.neusoft.gbw.cp.collect.recording.vo.StreamRecord;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecordingAssistant {
	
	public static boolean saveRecordToDisk(String filePath,StreamRecord stream,String fileName, String srcCode) {
		int totalLength = stream.getRecordLength();
		List<byte[]> cutFileByte = stream.getStream();
		OutputStream fos = null;
		OutputStream writer = null;
		File f = new File(filePath + fileName);
		if (f.exists()) 
			f.delete();
		try {
			f.createNewFile();
			fos = new FileOutputStream(f);
			writer = new BufferedOutputStream(fos);
//			if(srcCode.equals(OperationType.measure)) {//如果为收测任务，则增加wav头
			if(srcCode.equalsIgnoreCase(RecordConstants.OPER_TYPE_MEASURE)) {//如果为收测任务，则增加wav头
				ByteBuffer bb = ByteBuffer.allocate(44);
				initWaveHeader(bb, totalLength);
				writer.write(bb.array());
			}
			for(int i=0; i< cutFileByte.size();i++) {
				writer.write(cutFileByte.get(i));
			}
			writer.flush();
		} catch (IOException e) {
			Log.error("写入文件出现异常：", e);
			return false;
		} finally {
			try{
				if(fos != null) {
				   fos.close();
				   fos = null;
				}
				if(writer != null){
				   writer.close();
				   writer = null;
				} 
				if(cutFileByte != null) {
					cutFileByte.clear();
					cutFileByte = null;
				}
			}catch(Exception e) {
			}
		}
		return true;
	}
	/**
	 * 针对TTCP  新加生成文件 mp3  --》  wav
	 * @param filePath
	 * @param stream
	 * @param fileName
	 * @param srcCode
	 * @return
	 */
	public static boolean saveRecordToTTCPDisk(String filePath,StreamRecord stream,String fileName, String srcCode) {
		List<byte[]> cutFileByte = stream.getStream();
		OutputStream fos = null;
		OutputStream writer = null;
		String mp3FileName = fileName.replace(".wav", ".mp3");
		File mf = new File(filePath+mp3FileName);
		if (mf.exists()) 
			mf.delete();
		try {
			mf.createNewFile();
			fos = new FileOutputStream(mf);
			writer = new BufferedOutputStream(fos);
			for(int i=0; i< cutFileByte.size();i++) {
				writer.write(cutFileByte.get(i));
			}
			writer.flush();
		} catch (IOException e) {
			Log.error("写入文件出现异常：", e);
			return false;
		} finally {
			try{
				if(fos != null) {
				   fos.close();
				   fos = null;
				}
				if(writer != null){
				   writer.close();
				   writer = null;
				} 
				if(cutFileByte != null) {
					cutFileByte.clear();
					cutFileByte = null;
				}
			}catch(Exception e) {
			}
		}
		Converter converter = new Converter();
		try {
			//mp3格式 转换成 wav格式
			converter.convert(filePath+mp3FileName,filePath+fileName);
		} catch (Exception e) {
			Log.error("写入文件出现异常：", e);
			return false;
		}
		File f = new File(filePath+fileName);
		stream.setRecordLength(Integer.parseInt(String.valueOf(f.length())));
		if (mf.exists()) 
			mf.delete();
		return true;
	}

	public static boolean createFilePath(String filePath) {
		boolean flag = true;
		File f = new File(filePath);
		if (!f.exists()) {
			flag = f.mkdirs();
		}
		return flag;
	}
	
	 private static void initWaveHeader(ByteBuffer bb,int length) {
		  	bb.order(ByteOrder.LITTLE_ENDIAN); 
	    	bb.put((byte)'R');
	    	bb.put((byte)'I');
	    	bb.put((byte)'F');
	    	bb.put((byte)'F');
	    	bb.putInt(length+32);			// chank length
	    	bb.put((byte)'W');
	    	bb.put((byte)'A');
	    	bb.put((byte)'V');
	    	bb.put((byte)'E');
	    	bb.put((byte)'f');
	    	bb.put((byte)'m');
	    	bb.put((byte)'t');
	    	bb.put((byte)' ');
	    	bb.putInt(16);
	    	bb.putShort((short)1);
	    	bb.putShort((short)1);	// channels
	    	bb.putInt(11025);		// sample rate
	    	bb.putInt(11025*2);		// avg bytes per second
	    	bb.putShort((short)2);	// block align
	    	bb.putShort((short)16);	// bit per sample
	    	bb.put((byte)'d');
	    	bb.put((byte)'a');
	    	bb.put((byte)'t');
	    	bb.put((byte)'a');
	    	bb.putInt(length);			// chank2 length
	    }
}
