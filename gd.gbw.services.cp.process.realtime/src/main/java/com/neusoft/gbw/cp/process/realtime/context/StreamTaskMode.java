package com.neusoft.gbw.cp.process.realtime.context;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.process.realtime.service.stream.RecordStreamHandler;
import com.neusoft.gbw.cp.process.realtime.vo.TaskTimeOutInfo;


public class StreamTaskMode {
	private static class RealSteamTaskModeHolder {
		private static final StreamTaskMode INSTANCE = new StreamTaskMode();
	}
	
	private StreamTaskMode(){
		transferTimeOutMap = new HashMap<String, TaskTimeOutInfo>();
		streamTimeoutMap = new HashMap<String, TaskTimeOutInfo>();
		recordThreadMap = new HashMap<String, RecordStreamHandler>();
	}
	
	public static StreamTaskMode getInstance() {
		return RealSteamTaskModeHolder.INSTANCE;
	}
	//key msgId
	private HashMap<String, TaskTimeOutInfo> transferTimeOutMap = null;
	// key url
	private HashMap<String, TaskTimeOutInfo> streamTimeoutMap = null;
	// key freq+url
	private HashMap<String, RecordStreamHandler> recordThreadMap = null;
	
	private Lock lock = new ReentrantLock();
	
	public void putTransfer(String key,TaskTimeOutInfo info) {
		lock.lock();
		try {
			transferTimeOutMap.put(key, info);
		}finally{
			lock.unlock();
		}
	}
	
	public void putStream(String key,TaskTimeOutInfo info) {
		lock.lock();
		try {
			streamTimeoutMap.put(key, info);
		}finally{
			lock.unlock();
		}
	}
	
	public void putProcess(String key,RecordStreamHandler handler) {
		lock.lock();
		try {
			recordThreadMap.put(key, handler);
		}finally{
			lock.unlock();
		}
	}
	
	public TaskTimeOutInfo getTransferTimeOut(String key) {
		lock.lock();
		TaskTimeOutInfo info = null;
		try {
			info = transferTimeOutMap.get(key);
		}finally{
			lock.unlock();
		}
		return info;
	}
	
	public TaskTimeOutInfo getStreamTimeOut(String key) {
		lock.lock();
		TaskTimeOutInfo info = null;
		try {
			info = streamTimeoutMap.get(key);
		}finally{
			lock.unlock();
		}
		return info;
	}
	
	public void removeTranTimeOut(String key) {
		lock.lock();
		try {
			transferTimeOutMap.remove(key);
		}finally{
			lock.unlock();
		}
	}
	
	public void cancelStreamOut(String key) {
		lock.lock();
		try {
			if(streamTimeoutMap.containsKey(key)) 
			streamTimeoutMap.get(key).getTimer().cancel();
		}finally{
			lock.unlock();
		}
	}
	
	public void cancelTransferOut(String key) {
		lock.lock();
		try {
			if(transferTimeOutMap.containsKey(key)) 
				transferTimeOutMap.get(key).getTimer().cancel();
		}finally{
			lock.unlock();
		}
	}
	
	public void removeStreamTask(String key) {
		lock.lock();
		try {
			if(streamTimeoutMap.containsKey(key)) {
				streamTimeoutMap.remove(key);
			}
		}finally{
			lock.unlock();
		}
	}
	
	public void removeRecord(String key) {
		lock.lock();
		try {
			if(recordThreadMap.containsKey(key)) {
				recordThreadMap.remove(key);
			}
		}finally{
			lock.unlock();
		}
	}
	
	public boolean isTransferTimeOut(String id) {
		lock.lock();
		boolean isRuning = false;
		try {
			if(!transferTimeOutMap.containsKey(id)) 
				return true;
			
			TaskTimeOutInfo info = transferTimeOutMap.get(id);
			isRuning = info.isTimeOut();
		}finally{
			lock.unlock();
		}
		return isRuning;
	}
	
	public boolean isStreamRuning(String id) {
		lock.lock();
		boolean isRuning = false;
		try {
			if(!streamTimeoutMap.containsKey(id)) 
				return isRuning;
			
			TaskTimeOutInfo info = streamTimeoutMap.get(id);
			isRuning = info.isTimeOut();
		}finally{
			lock.unlock();
		}
		return isRuning;
	}
	
	public void stopRecord(String id) {
		lock.lock();
		try {
			if(recordThreadMap.containsKey(id)) 
				recordThreadMap.get(id).setRecordStatus(true);
		}finally{
			lock.unlock();
		}
	}
	
	public boolean isRecordRuning(String id) {
		lock.lock();
		try {
			if(recordThreadMap.containsKey(id)) 
				return true;
		}finally{
			lock.unlock();
		}
		return false;
	}
}
