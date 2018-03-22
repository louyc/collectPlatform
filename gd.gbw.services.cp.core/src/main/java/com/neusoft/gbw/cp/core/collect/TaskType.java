package com.neusoft.gbw.cp.core.collect;

public enum TaskType {

	system, //系统任务
	
	temporary,//日常任务点击任务
	
	burst,	//突发收测任务
	
	leakage,  //补采任务
	
	qualityDel //自动三满任务删除
}
