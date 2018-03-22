package com.neusoft.gbw.cp.build.domain.services;

public class TaskProcessCentre {
	
//	private JMSTaskProcesser jmsProcess = null;
//	private RestTaskProcesser restProcess = null;
//	private ManualAndAutoTaskProcesser manAutoProcess = null;

	private static class Holder {
		private static final TaskProcessCentre INSTANCE = new TaskProcessCentre();
	}
	
	private TaskProcessCentre(){
	}
	
	public static TaskProcessCentre getInstance() {
		return Holder.INSTANCE;
	}
	
	//每接收一个任务实例化一个对象，已防任务正在执行中，其他任务需要等待执行，不过对于内存是否释放需要考虑观察  modify by jiahao
	public ITaskProcess newJMSTaskProcess() {
//		if (jmsProcess == null) {
//		jmsProcess = new JMSTaskProcesser();
//		}
		JMSTaskProcesser jmsProcess = new JMSTaskProcesser();
		return jmsProcess;
	}
	
	public ITaskProcess newRestTskProcess() {
//		if (restProcess == null) {
//		restProcess = new RestTaskProcesser();
//		}
		RestTaskProcesser restProcess = new RestTaskProcesser();
		return restProcess;
	}
	//效果类（在线监听，效果任务）和设置类任务（指标，频偏，录音，频谱）
	public ITaskProcess newManualAndAutoTskProcess() {
//		if (manAutoProcess == null)
//		manAutoProcess = new ManualAndAutoTaskProcesser();
		 ManualAndAutoTaskProcesser manAutoProcess = new ManualAndAutoTaskProcesser();
		return manAutoProcess;
	} 
	
	
	//增加清空process动作
	
}
