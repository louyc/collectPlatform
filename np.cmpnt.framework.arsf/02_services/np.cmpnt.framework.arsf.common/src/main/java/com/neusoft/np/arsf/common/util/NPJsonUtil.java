package com.neusoft.np.arsf.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JavaTypeMapper;
import org.json.JSONObject;

import com.google.gson.Gson;

public class NPJsonUtil {

	public static String jsonValueString(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return "";
		}
		JSONObject jsonObject = new JSONObject();
		Iterator<Entry<String, String>> mapIter = map.entrySet().iterator();
		while (mapIter.hasNext()) {
			Entry<String, String> entry = mapIter.next();
			jsonObject.put(entry.getKey(), entry.getValue());
		}
		return jsonObject.toString();
	}

	public static Map<String, String> jsonValueToMap(String str) throws NMFormateException {
		try {
			Map<String, String> map = new HashMap<String, String>();
			JSONObject jsonObject = new JSONObject(str);
			Iterator<?> jIter = jsonObject.keys();
			while (jIter.hasNext()) {
				String key = (String) jIter.next();
				map.put(key, jsonObject.getString(key));
			}
			return map;
		} catch (Exception e) {
			throw new NMFormateException("JSonToMap ERROR。" + str);
		}
	}

	public static <T> String mapToJson(Map<String, T> map) {
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Map<String, T> jsonToMap(String str) throws NMFormateException {
		try {
			Map<String, T> values = new HashMap<String, T>();
			JsonFactory jfactory = new JsonFactory();
			JsonParser jParser = jfactory.createJsonParser(str);
			JavaTypeMapper mapper = new JavaTypeMapper();
			values = (Map) mapper.read(jParser);
			return values;
		} catch (Exception e) {
			throw new NMFormateException("JSonToMap ERROR。" + str);
		}
	}

	public static <T> T jsonToObject(String str, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(str, clazz);
	}

	public static <T> String objectToJson(T t) {
		Gson gson = new Gson();
		return gson.toJson(t);
	}
}
