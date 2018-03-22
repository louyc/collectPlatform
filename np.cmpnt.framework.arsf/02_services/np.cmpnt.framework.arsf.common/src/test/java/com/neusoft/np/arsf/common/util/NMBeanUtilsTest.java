package com.neusoft.np.arsf.common.util;

public class NMBeanUtilsTest {

	public static void testPaddingNNAttrs() throws NMBeanUtilsException {
		NMBeanUtilsTestBean a = new NMBeanUtilsTestBean("default_apple", "default_banana");
		NMBeanUtilsTestBean b = new NMBeanUtilsTestBean("custom_apple", null);
		NMBeanUtils.paddingNNAttrs(a, b);
		System.out.println("target:" + a);
		System.out.println("source:" + b);
	}
}
