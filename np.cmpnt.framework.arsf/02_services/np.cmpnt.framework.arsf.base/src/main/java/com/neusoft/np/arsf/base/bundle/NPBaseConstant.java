package com.neusoft.np.arsf.base.bundle;

public interface NPBaseConstant {

	public interface EventTopic {
		String START_EVENT_TOPIC = "ARSF_BASE-START_EVENT_TOPIC";
		String START_SERVER_TOPIC = "ARSF_BASE-START_SERVER_TOPIC";
		String QUEUE_THRESHOLD_TOPIC = "ARSF_BASE-QUEUE_THRESHOLD_TOPIC";
		String THREAD_THROWABLE_TOPIC = "ARSF_BASE-THREAD_THROWABLE_TOPIC";
	}

	public interface Control {
		String BUNDLE_LIST = "BUNDLE_LIST";
		String BUNDLE_ANSWER = "BUNDLE_ANSWER";
		String BUNDLE_SPLIT = ";";
		String VERDOR = "com.neusoft";
		String VERDOR_ARSF = "com.neusoft.np.arsf";
		String VERDOR_CORE = ".core";
		String VERDOR_COMMON = ".common";
		int ANSWER_WAIT_TIME = 3000;
	}

	public interface Util {
		String NAME = "NAME";
		String CAPACITY = "CAPACITY";
		String SIZE = "SIZE";
		String RATE = "RATE";
		String DATE = "DATE";
		String BUNDLE_NAME = "BUNDLE_NAME";
	}

}
