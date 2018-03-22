package com.neusoft.gbw.cp.conver.util;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class TimeConverter implements SingleValueConverter{

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		// TODO Auto-generated method stub
		return type.equals(Time.class);
	}

	@Override
	public Object fromString(String arg0) {
		Time t = new Time();
		t.setTime(arg0);
		return t;
	}

	@Override
	public String toString(Object arg0) {
		// TODO Auto-generated method stub
		return ((Time)arg0).toString();
	}

}
