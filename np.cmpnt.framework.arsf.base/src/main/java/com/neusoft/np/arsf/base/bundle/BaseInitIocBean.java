package com.neusoft.np.arsf.base.bundle;

import java.util.concurrent.TimeUnit;

public abstract class BaseInitIocBean {

	private static final int SLEEPTIME = 1500;

	private Thread thread;

	public void init() {
		thread = new Thread(new BaseInitIocBeanTask());
		thread.setName("com.neusoft.np.arsf.base.bundle.BaseInitIocBeanTask");
		thread.start();
	}

	public abstract void start();

	public void destroy() {
		thread.interrupt();
	}

	public abstract void stop();

	public class BaseInitIocBeanTask implements Runnable {
		@Override
		public void run() {
			while (!BaseInitInfo.getInstance().getFinish()) {
				System.out.println("ARSF init out : BaseInitIocBean wait -- ");
				try {
					TimeUnit.NANOSECONDS.sleep(SLEEPTIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			System.out.println("-- BaseInitIocBean begin -- ");
			start();
		}
	}

}

class BaseInitInfo {
	
	protected static class BaseInitInfoHolder {
		private static final BaseInitInfo INSTANCE = new BaseInitInfo();
	}
	
	private BaseInitInfo() {
	}

	protected static BaseInitInfo getInstance() {
		return BaseInitInfoHolder.INSTANCE;
	}

	public boolean getFinish() {
		return finish;
	}

	private volatile boolean finish = false;

	public void finish() {
		finish = true;
	}
}
