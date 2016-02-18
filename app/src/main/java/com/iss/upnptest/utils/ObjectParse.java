package com.iss.upnptest.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.fourthline.cling.model.types.BooleanDatatype;

/**
 * @author hubing
 * @version 1.0.0 2015-5-7
 */

public class ObjectParse {

	protected class FieldType {

		public static final String BYTE = "byte";
		public static final String SHORT = "short";
		public static final String INT = "int";
		public static final String LONG = "long";
		public static final String FLOAT = "float";
		public static final String DOUBLE = "double";
		public static final String BOOLEAN = "boolean";
		public static final String CHAR = "char";

	}

	protected static final String SET = "set";

	private static String TAG = ObjectParse.class.getSimpleName();
	
	
	/**
	 * 将map解析成object
	 * @param data
	 * @param clazz
	 * @return
	 */
	public static <T> T parseMapToObject(Map<String, Object> data, Class<T> clazz) {
		T t = null;
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			t = constructor.newInstance();
			if (data == null || data.isEmpty()) {
				return t;
			}
			Set<Entry<String, Object>> set = data.entrySet();
			for (Entry<String, Object> entry : set) {
				String attrName = entry.getKey();
				try {
					Field field = clazz.getDeclaredField(firstToLowerCase(attrName));
					Class<?> fieldType = field.getType();
					Method method = clazz.getDeclaredMethod(SET + attrName, fieldType);
					method.invoke(t, parseToRealValue(fieldType, entry.getValue()));
				} catch (NoSuchFieldException e) {
					break;
				}
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return t;
	}

	/**
	 * 将字符串首字母改成小写
	 * 
	 * @param str
	 * @return
	 */
	public static String firstToLowerCase(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		if (str.length() == 1) {
			return str.toLowerCase(Locale.CHINA);
		}
		StringBuilder sb = new StringBuilder();
		String mStr = str.substring(0, 1).toLowerCase(Locale.CHINA);
		sb.append(mStr);
		sb.append(str.substring(1, str.length()));
		return sb.toString();
	}

	private static Object parseToRealValue(Class<?> classType, Object value)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException {
		String type = classType.toString();
		if (FieldType.BYTE.equals(type)) {
			return Byte.parseByte(value.toString());
		} else if (FieldType.SHORT.equals(type)) {
			return Short.parseShort(value.toString());
		} else if (FieldType.INT.equals(type)) {
			return Integer.parseInt(value.toString());
		} else if (FieldType.LONG.equals(type)) {
			return Long.parseLong(value.toString());
		} else if (FieldType.FLOAT.equals(type)) {
			return Float.parseFloat(value.toString());
		} else if (FieldType.DOUBLE.equals(type)) {
			return Double.parseDouble(value.toString());
		} else if (FieldType.BOOLEAN.equals(type)) {
			return new BooleanDatatype().valueOf(value.toString());
		} else if (FieldType.CHAR.equals(type)) {
			return (Character) value;
		} else {
			Constructor<?> constructor = classType.getDeclaredConstructor(String.class);
			return constructor.newInstance(value.toString());
		}
	}

}
