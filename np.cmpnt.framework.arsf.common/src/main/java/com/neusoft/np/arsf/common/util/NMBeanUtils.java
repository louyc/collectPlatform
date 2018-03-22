package com.neusoft.np.arsf.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 
 * 项目名称: Netpatrol产品<br>
 * 模块名称: 处理平台服务开发<br>
 * 功能描述: 配置文件内容到类对象转换工具<br>
 * 创建日期: 2013-6-20 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-19       刘勃宏        创建
 *    2    2013-06-20       黄守凯        重构
 *    3    2013-11-01       黄守凯        重构
 *    4    2014-05-04       黄守凯        重构
 * </pre>
 */
public final class NMBeanUtils {

	private NMBeanUtils() {
	}

	/*
	 *  new APIs 
	 *  （1）引入泛型，接口更加通用
	 *  （2）修改方法名称
	 */
	public static <S, T> S fillObjectAttrs(S object, Map<String, T> dataMap) throws NMBeanUtilsException {
		try {
			fillObjectFields(dataMap, object);
			return object;
		} catch (Exception e) {
			throw new NMBeanUtilsException("Class is " + object.getClass().getName() + ": ", e);
		}
	}

	/**
	 * 填充传入对象的属性值，map的key为待填充的属性名称，value为属性值
	 */
	public static <S> S fillObjectAttrsS(S object, Map<String, String> dataMap) throws NMBeanUtilsException {
		return fillObjectAttrs(object, dataMap);
	}

	/**
	 * 填充传入对象的属性值，map的key为待填充的属性名称，value为属性值
	 */
	public static <S, T> S fillObjectAttrsO(S object, Map<String, Object> dataMap) throws NMBeanUtilsException {
		return fillObjectAttrs(object, dataMap);
	}

	/*
	 *  former APIs
	 */
	/**
	 * 根据传入的class类，创建对象。
	 * 
	 * 创建对象后，根据dataMap赋值object的属性
	 * 
	 * @param clazz
	 * @param dataMap
	 * @return
	 */
	public static <T> Object createObject(Class<?> clazz, Map<String, T> dataMap) throws NMBeanUtilsException {
		try {
			Object obj = clazz.newInstance();
			fillObjectFields(dataMap, obj);
			return obj;
		} catch (Exception e) {
			throw new NMBeanUtilsException("Class is " + clazz.getName() + ": ", e);
		}
	}

	/**
	 * 通过className，查找class类，创建对象。
	 * 
	 * 创建对象后，根据dataMap赋值object的属性
	 * 
	 * @param className 需要赋值的类名称
	 * @param dataMap 赋值数据
	 * @return 需要赋值对象
	 * @throws NMBeanUtilsException 
	 */
	public static <T> Object createObject(String className, Map<String, T> dataMap) throws NMBeanUtilsException {
		try {
			Object obj = Class.forName(className).newInstance();
			fillObjectFields(dataMap, obj);
			return obj;
		} catch (Exception e) {
			throw new NMBeanUtilsException("Class is " + className + ": ", e);
		}
	}

	/**
	 * 填充传入对象的属性值，map的key为待填充的属性名称，value为属性值
	 * 
	 * @param object
	 * @param dataMap
	 * @return
	 * @throws NMBeanUtilsException 
	 */
	public static <T> Object createObject(Object object, Map<String, T> dataMap) throws NMBeanUtilsException {
		try {
			fillObjectFields(dataMap, object);
			return object;
		} catch (Exception e) {
			throw new NMBeanUtilsException("Class is " + object.getClass().getName() + ": ", e);
		}
	}

	private static <T> void fillObjectFields(Map<String, T> dataMap, Object obj) throws Exception {
		Iterator<Map.Entry<String, T>> iter = dataMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, T> elements = iter.next();
			String key = elements.getKey();
			if (elements.getValue() == null) {
				continue;
			}
			T value = elements.getValue();
			BeanUtils.setProperty(obj, key, value);
		}
	}

	/*
	 * flex
	 */
	/**
	 * 根据传入类的类型进行强转，适用于Flex→Java情况；
	 */
	public static <T> Object createObjectChaos(String className, Map<String, T> dataMap) throws NMBeanUtilsException {
		try {
			Object obj = Class.forName(className).newInstance();
			fillObjectFieldsChaos(dataMap, obj);
			return obj;
		} catch (Exception e) {
			throw new NMBeanUtilsException("Class is " + className + ": ", e);
		}
	}

	private static <T> void fillObjectFieldsChaos(Map<String, T> dataMap, Object obj) throws Exception {
		Map<String, Class<?>> types = getPropertiesType(obj.getClass());
		Iterator<Map.Entry<String, T>> iter = dataMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, T> elements = iter.next();
			String key = elements.getKey();
			Class<?> type = types.get(key);
			if (elements.getValue() == null) {
				continue;
			}
			T value = elements.getValue();
			if (List.class.isAssignableFrom(value.getClass())) {
				processListType(obj, key, value);
			} else if (Number.class.isAssignableFrom(type)) {
				BeanUtils.setProperty(obj, key, value);
			} else if (String.class.equals(type)) {
				BeanUtils.setProperty(obj, key, value);
			} else {
				Object newObj = type.newInstance();
				fillObjectFieldsChaos(getObjectField(value), newObj);
				BeanUtils.setProperty(obj, key, newObj);
			}
		}
	}

	private static <T> void processListType(Object obj, String key, T value) throws Exception {
		Type listType = getListDeclaredField(obj, key);
		if (listType == null) {
			return;
		}
		List<?> vList = (List<?>) value;
		List<Object> rsList = new ArrayList<Object>();
		for (Object v : vList) {
			Object newObj = Class.forName(getClassNameByType(listType)).newInstance();
			fillObjectFieldsChaos(getObjectField(v), newObj);
			rsList.add(newObj);
		}
		BeanUtils.setProperty(obj, key, rsList);
	}

	/**
	 * 获取class的属性类型。
	 * 
	 * private String name
	 * 
	 * 返回：key:name;value:java.lang.String
	 */
	private static Map<String, Class<?>> getPropertiesType(Class<?> clazz) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] prs = beanInfo.getPropertyDescriptors();
		Map<String, Class<?>> rs = new HashMap<String, Class<?>>();
		for (int i = 0; i < prs.length; i++) {
			rs.put(prs[i].getName(), prs[i].getPropertyType());
		}
		return rs;
	}

	/**
	 * 返回List的泛型
	 * 参考：http://www.linuxidc.com/Linux/2012-06/62371.htm
	 */
	private static Type getListDeclaredField(Object obj, String key) throws SecurityException, NoSuchFieldException {
		Field f = obj.getClass().getDeclaredField(key);
		Type mapMainType = f.getGenericType();
		if (mapMainType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) mapMainType;
			Type[] types = parameterizedType.getActualTypeArguments();
			if (types != null) {
				return types[0];
			}
		}
		return null;
	}

	private static String getClassNameByType(Type t) {
		String[] s = t.toString().split(" ");
		if (s == null || s.length != 2) {
			return null;
		}
		return s[1];
	}

	/*
	 * 静态属性处理
	 */

	/**
	 * 根据传入的class类，填充类的静态属性。
	 * 
	 * @param clazz
	 * @param dataMap
	 * @throws NMBeanUtilsException 
	 */
	public static void fillClassStaticFields(Class<?> clazz, Map<String, String> dataMap) throws NMBeanUtilsException {
		Field[] fields = clazz.getDeclaredFields();
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		for (Field field : fields) {
			fieldMap.put(field.getName(), field);
		}
		Iterator<Map.Entry<String, String>> iter = dataMap.entrySet().iterator();
		try {
			while (iter.hasNext()) {
				Map.Entry<String, String> elements = iter.next();
				processStaticFields(clazz.getName(), fieldMap, elements);
			}
		} catch (IllegalAccessException e) {
			throw new NMBeanUtilsException("Class is " + clazz.getName() + ": ", e);
		}
	}

	private static void processStaticFields(String className, Map<String, Field> fieldMap,
			Map.Entry<String, String> elements) throws IllegalAccessException, NMBeanUtilsException {
		String key = elements.getKey();
		String value = elements.getValue();
		if (fieldMap.containsKey(key)) {
			Field field = fieldMap.get(key);
			if (field.getType().equals(Integer.TYPE)) {
				field.set(field.getName(), Integer.valueOf(value));
			} else if (field.getType().equals(java.lang.String.class)) {
				field.set(field.getName(), value);
			} else if (field.getType().equals(Boolean.TYPE)) {
				field.set(field.getName(), Boolean.valueOf(value));
			} else {
				throw new NMBeanUtilsException("UNSUPPORT TYPE OF " + field.getType() + " ,Class is " + className);
			}
		}
	}

	/**
	 * 将传入类的静态属性填充到map中，map的key为类的属性名称，value为类的静态属性值
	 */
	public static Map<String, Object> getClassStaticFieldNames(Class<?> clazz) throws NMBeanUtilsException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				map.put(name, field.get(name));
			}
			return map;
		} catch (IllegalArgumentException e) {
			throw new NMBeanUtilsException("Class is " + clazz.getName() + ": ", e);
		} catch (IllegalAccessException e) {
			throw new NMBeanUtilsException("Class is " + clazz.getName() + ": ", e);
		}
	}

	public static Map<String, Object> getObjectField(Object object) throws NMBeanUtilsException {
		if (object == null) {
			return null;
		}
		try {
			Map<String, Object> propertiesMap = new HashMap<String, Object>();
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (!key.equals("class")) {
					Method getter = property.getReadMethod();
					Object value = getter.invoke(object);
					propertiesMap.put(key, value);
				}
			}
			return propertiesMap;
		} catch (Exception e) {
			throw new NMBeanUtilsException("Object is " + object + ": ", e);
		}
	}

	public static Map<String, String> getObjectFieldStr(Object object) throws NMBeanUtilsException {
		Map<String, Object> map = getObjectField(object);
		return NMCollectionUtil.fillingMapStr(map);
	}

	/**
	 * 复制source中的非空属性到target中，覆盖target中原有的属性；
	 * 
	 * @param target
	 * @param source
	 * @return
	 * @throws NMBeanUtilsException 
	 */
	public static Object paddingNNAttrs(Object target, Object source) throws NMBeanUtilsException {
		Map<String, Object> sourceAttrs = getObjectField(source);
		Iterator<Map.Entry<String, Object>> iter = sourceAttrs.entrySet().iterator();
		try {
			while (iter.hasNext()) {
				Map.Entry<String, Object> elements = iter.next();
				String key = elements.getKey();
				Object value = elements.getValue();
				if (value == null) {
					continue;
				}
				BeanUtils.setProperty(target, key, value);
			}
			return target;
		} catch (Exception e) {
			throw new NMBeanUtilsException("Object is " + target + " and " + source + " : ", e);
		}
	}
}
