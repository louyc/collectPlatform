package com.neusoft.gbw.cp.conver;

import com.neusoft.gbw.cp.conver.v5.domain.ProtocolConverServiceV5;
import com.neusoft.gbw.cp.conver.v6.domain.ProtocolConverServiceV6;
import com.neusoft.gbw.cp.conver.v7.domain.ProtocolConverServiceV7;
import com.neusoft.gbw.cp.conver.v8.domain.ProtocolConverServiceV8;

public class ProtocolConverFactory {

	private static class ProtocolConverFactoryHolder {
		private static final ProtocolConverFactory INSTANCE = new ProtocolConverFactory();
	}

	private ProtocolConverFactory() {
	}

	public static ProtocolConverFactory getInstance() {
		return ProtocolConverFactoryHolder.INSTANCE;
	}

	public IProtocolConver newProtocolConverServiceV7() {
		return new ProtocolConverServiceV7();
	}
	
	public IProtocolConver newProtocolConverServiceV8() {
		return new ProtocolConverServiceV8();
	}
	
	public IProtocolConver newProtocolConverServiceV6() {
		return new ProtocolConverServiceV6();
	}
	
	public IProtocolConver newProtocolConverServiceV5() {
		return new ProtocolConverServiceV5();
	}
}
