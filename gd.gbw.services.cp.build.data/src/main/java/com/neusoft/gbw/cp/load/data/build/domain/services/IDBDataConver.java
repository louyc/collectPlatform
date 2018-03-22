package com.neusoft.gbw.cp.load.data.build.domain.services;

import java.util.List;

import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public interface IDBDataConver {

	List<Task> getTaskList(String taskId);
	
	List<Task> getMoniTaskList(String monitorId);
}
