package com.neusoft.np.arsf.common.util;

public class NMBeanUtilsTestBean {

	private String apple;

	private String banana;

	public NMBeanUtilsTestBean() {
	}

	public NMBeanUtilsTestBean(String apple, String banana) {
		this.apple = apple;
		this.banana = banana;
	}

	public String getBanana() {
		return banana;
	}

	public void setBanana(String banana) {
		this.banana = banana;
	}

	public String getApple() {
		return apple;
	}

	public void setApple(String apple) {
		this.apple = apple;
	}

	@Override
	public String toString() {
		return "NMBeanUtilsTestBean [apple=" + apple + ", banana=" + banana + "]";
	}
}
