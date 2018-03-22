package com.neusoft.gbw.cp.collect.service.control;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class TaskPriorityControlHandler implements Comparator<CollectTask>{
	
	private int type;

	public TaskPriorityControlHandler(int type) {
		this.type = type;
	}

	@Override
	public int compare(CollectTask o1, CollectTask o2) {
		long priority1, priority2;
		if(type == 1) {
			priority1 = o1.getTaskPriority().getMeasurePriority();
			priority2 = o2.getTaskPriority().getMeasurePriority();
		}else {
			priority1 = o1.getTaskPriority().getCollectPriority();
			priority2 = o2.getTaskPriority().getCollectPriority();
		}
		
		if (priority1 > priority2) 
			return -1;		
		else if (priority1 == priority2) 
			return 0;		
		else 
			return 1;	
	}
	
	
	public static void main(String[] args) {
		PriorityBlockingQueue<Integer> d = new PriorityBlockingQueue<>(1, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o2>o1)
					return 1;
				else if(o2<o1)
					return -1;
				else
					return 0;
			}
		});
		d.add(1);
		d.add(3);
		d.add(2);
		for(int i=1;i<=3;i++)
		System.out.println(d.poll());
	}
}
