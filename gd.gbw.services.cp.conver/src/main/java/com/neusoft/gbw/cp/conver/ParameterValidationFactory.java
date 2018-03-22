package com.neusoft.gbw.cp.conver;

import com.neusoft.gbw.cp.conver.v8.domain.ParameterValidationService;
/**
 * 静态工厂
 * @author yanghao
 *
 */
public class ParameterValidationFactory {
	private static class StaticFactoryHolder {
		private static final ParameterValidationFactory INSTANCE = new ParameterValidationFactory();
	}

	private ParameterValidationFactory() {
	}

	public static ParameterValidationFactory getInstance() {
		return StaticFactoryHolder.INSTANCE;
	}

	public ParameterValidation newParameterValidationService(){
		return new ParameterValidationService();
	}
}
