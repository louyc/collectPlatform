package com.neusoft.gbw.cp.conver.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class DateDayConverter implements SingleValueConverter {

	public DateDayConverter() {
	}

	public String toString(Object obj) {
		try {
			//时间格式化器
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			//指定日期/时间解析是否不严格 lenient - 为 true 时，解析过程是不严格的
			dateFormat.setLenient(true);
			return dateFormat.format(((Date) obj));
		} catch (Exception e) {
			return null;
		}
	}

	public Object fromString(String date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(true);
			return dateFormat.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return type.equals(Date.class);
	}
}
