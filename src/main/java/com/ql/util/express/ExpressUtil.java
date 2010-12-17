package com.ql.util.express;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 表达式工具类
 * 
 * @author qhlhl2010@gmail.com
 * 
 */
@SuppressWarnings("unchecked")
public class ExpressUtil {
	public static final String DT_STRING = "String";
	public static final String DT_SHORT = "Short";
	public static final String DT_INTEGER = "Integer";
	public static final String DT_LONG = "Long";
	public static final String DT_DOUBLE = "Double";
	public static final String DT_FLOAT = "Float";
	public static final String DT_BYTE = "Byte";
	public static final String DT_CHAR = "Char";
	public static final String DT_BOOLEAN = "Boolean";
	public static final String DT_DATE = "Date";
	public static final String DT_TIME = "Time";
	public static final String DT_DATETIME = "DateTime";
	public static final String DT_OBJECT = "Object";

	public static final String DT_short = "short";
	public static final String DT_int = "int";
	public static final String DT_long = "long";
	public static final String DT_double = "double";
	public static final String DT_float = "float";
	public static final String DT_byte = "byte";
	public static final String DT_char = "char";
	public static final String DT_boolean = "boolean";

	public static Class<?>[][] classMatchs =new Class[][]{
			//原始数据类型
			{double.class,float.class},{double.class,long.class},{double.class,int.class}, {double.class,short.class},{double.class,byte.class},
			{float.class,long.class},  {float.class,int.class},  {float.class,short.class},{float.class,byte.class},
			{long.class,int.class},    {long.class,short.class}, {long.class,byte.class},
			{int.class,short.class},   {int.class,byte.class},
			{short.class,byte.class},
			//---------
			{byte.class,Byte.class},{Byte.class,byte.class},
			{short.class,Short.class},{Short.class,short.class},
			{int.class,Integer.class},{Integer.class,int.class},
			{long.class,Long.class},{Long.class,long.class},
			{float.class,Float.class},{Float.class,float.class},
			{double.class,Double.class},{Double.class,double.class},
			{char.class,Character.class},{Character.class,char.class},
			{boolean.class,Boolean.class},{Boolean.class,boolean.class}
	};	
	
	public static Class getSimpleDataType(Class aClass) {
		if (Integer.class.equals(aClass))
			return Integer.TYPE;
		if (Short.class.equals(aClass))
			return Short.TYPE;
		if (Long.class.equals(aClass))
			return Long.TYPE;
		if (Double.class.equals(aClass))
			return Double.TYPE;
		if (Float.class.equals(aClass))
			return Float.TYPE;
		if (Byte.class.equals(aClass))
			return Byte.TYPE;
		if (Character.class.equals(aClass))
			return Character.TYPE;
		if (Boolean.class.equals(aClass))
			return Boolean.TYPE;
		return aClass;
	}

	public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
		if (lhsType == rhsType)
			return true;

		if (lhsType == null)
			return false;
		if (rhsType == null)//null转换
			return !lhsType.isPrimitive();
		
		if (lhsType.isAssignableFrom(rhsType) == true){
			return true;
		}
		if (lhsType.isPrimitive() == false) {
			if (lhsType == Byte.class)
				lhsType = byte.class;
			else if (lhsType == Short.class)
				lhsType = short.class;
			else if (lhsType == Integer.class)
				lhsType = int.class;
			else if (lhsType == Long.class)
				lhsType = long.class;
			else if (lhsType == Float.class)
				lhsType = float.class;
			else if (lhsType == Double.class)
				lhsType = double.class;
		}
		if (rhsType.isPrimitive() == false) {
			if (rhsType == Byte.class)
				rhsType = byte.class;
			else if (rhsType == Short.class)
				rhsType = short.class;
			else if (rhsType == Integer.class)
				rhsType = int.class;
			else if (rhsType == Long.class)
				rhsType = long.class;
			else if (rhsType == Float.class)
				rhsType = float.class;
			else if (rhsType == Double.class)
				rhsType = double.class;
		}
		if (lhsType == rhsType)// 转换后需要在判断一下
			return true;

		for (int i = 0; i < classMatchs.length; i++) {
			if (lhsType == classMatchs[i][0] && rhsType == classMatchs[i][1]) {
				return true;
			}
		}
		
		return false;
		
	}
	public static boolean isAssignableOld(Class<?> lhsType, Class<?> rhsType) {
		if (lhsType == null)
			return false;
		if (rhsType == null)
			return !lhsType.isPrimitive();

		if (lhsType.isPrimitive() && rhsType.isPrimitive()) {
			if (lhsType == rhsType)
				return true;

			if ((rhsType == Byte.TYPE)
					&& (lhsType == Short.TYPE || lhsType == Integer.TYPE
							|| lhsType == Long.TYPE || lhsType == Float.TYPE || lhsType == Double.TYPE))
				return true;

			if ((rhsType == Short.TYPE)
					&& (lhsType == Integer.TYPE || lhsType == Long.TYPE
							|| lhsType == Float.TYPE || lhsType == Double.TYPE))
				return true;

			if ((rhsType == Character.TYPE)
					&& (lhsType == Integer.TYPE || lhsType == Long.TYPE
							|| lhsType == Float.TYPE || lhsType == Double.TYPE))
				return true;

			if ((rhsType == Integer.TYPE)
					&& (lhsType == Long.TYPE || lhsType == Float.TYPE || lhsType == Double.TYPE))
				return true;

			if ((rhsType == Long.TYPE)
					&& (lhsType == Float.TYPE || lhsType == Double.TYPE))
				return true;

			if ((rhsType == Float.TYPE) && (lhsType == Double.TYPE))
				return true;
		} else if (lhsType.isAssignableFrom(rhsType))
			return true;

		return false;
	}

	public static boolean isSignatureAssignable(Class[] from, Class[] to) {
		for (int i = 0; i < from.length; i++)
			if (!isAssignable(to[i], from[i]))
				return false;
		return true;
	}

	public static int findMostSpecificSignature(Class[] idealMatch,
			Class[][] candidates) {
		Class[] bestMatch = null;
		int bestMatchIndex = -1;

		for (int i = candidates.length - 1; i >= 0; i--) {// 先从基类开始查找 墙辉
			Class[] targetMatch = candidates[i];
			if (ExpressUtil.isSignatureAssignable(idealMatch, targetMatch)
					&& ((bestMatch == null) || ExpressUtil
							.isSignatureAssignable(targetMatch, bestMatch))) {
				bestMatch = targetMatch;
				bestMatchIndex = i;
			}
		}

		if (bestMatch != null)
			return bestMatchIndex;
		else
			return -1;
	}

	public static Method findMethod(Class baseClass, String methodName,
			Class[] types, boolean publicOnly, boolean isStatic) {

		Vector candidates = gatherMethodsRecursive(baseClass, methodName,
				types.length, publicOnly, isStatic, null /* candidates */);
		Method method = findMostSpecificMethod(types, (Method[]) candidates
				.toArray(new Method[0]));

		return method;
	}

	public static Constructor findConstructor(Class baseClass, Class[] types) {
		Constructor[] constructors = baseClass.getConstructors();
		List listClass = new ArrayList();
		List<Constructor> constructorList = new ArrayList();
		for (int i = 0; i < constructors.length; i++) {
			if (constructors[i].getParameterTypes().length == types.length) {
				listClass.add(constructors[i].getParameterTypes());
				constructorList.add(constructors[i]);
			}
		}

		int match = findMostSpecificSignature(types, (Class[][]) listClass
				.toArray(new Class[0][]));
		return match == -1 ? null : constructorList.get(match);
	}

	public static Method findMostSpecificMethod(Class[] idealMatch,
			Method[] methods) {
		Class[][] candidateSigs = new Class[methods.length][];
		for (int i = 0; i < methods.length; i++)
			candidateSigs[i] = methods[i].getParameterTypes();

		int match = findMostSpecificSignature(idealMatch, candidateSigs);
		return match == -1 ? null : methods[match];

	}

	private static Vector gatherMethodsRecursive(Class baseClass,
			String methodName, int numArgs, boolean publicOnly,
			boolean isStatic, Vector candidates) {
		if (candidates == null)
			candidates = new Vector();

		addCandidates(baseClass.getDeclaredMethods(), methodName, numArgs,
				publicOnly, isStatic, candidates);

		Class[] intfs = baseClass.getInterfaces();
		for (int i = 0; i < intfs.length; i++)
			gatherMethodsRecursive(intfs[i], methodName, numArgs, publicOnly,
					isStatic, candidates);

		Class superclass = baseClass.getSuperclass();
		if (superclass != null)
			gatherMethodsRecursive(superclass, methodName, numArgs, publicOnly,
					isStatic, candidates);

		return candidates;
	}

	private static Vector addCandidates(Method[] methods, String methodName,
			int numArgs, boolean publicOnly, boolean isStatic, Vector candidates) {
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.getName().equals(methodName)
					&& (m.getParameterTypes().length == numArgs)
					&& (publicOnly == false || isPublic(m)
							&& (isStatic == false || isStatic(m))))
				candidates.add(m);
		}
		return candidates;
	}

	public static boolean isPublic(Class c) {
		return Modifier.isPublic(c.getModifiers());
	}

	public static boolean isPublic(Method m) {
		return Modifier.isPublic(m.getModifiers());
	}

	public static boolean isStatic(Method m) {
		return Modifier.isStatic(m.getModifiers());
	}

	public static Class getJavaClass(String type) {
		int index = type.indexOf("[]");
		if (index < 0)
			return getJavaClassInner(type);

		StringBuilder arrayString = new StringBuilder();
		arrayString.append("[");
		String baseType = type.substring(0, index);
		while ((index = type.indexOf("[]", index + 2)) >= 0) {
			arrayString.append("[");
		}
		Class baseClass = getJavaClassInner(baseType);

		try {
			String baseName = "";
			if (baseClass.isPrimitive() == false) {
				return loadClass(arrayString.toString() + "L"
						+ baseClass.getName() + ";");
			} else {
				if (baseClass.equals(boolean.class)) {
					baseName = "Z";
				} else if (baseClass.equals(byte.class)) {
					baseName = "B";
				} else if (baseClass.equals(char.class)) {
					baseName = "C";
				} else if (baseClass.equals(double.class)) {
					baseName = "D";
				} else if (baseClass.equals(float.class)) {
					baseName = "F";
				} else if (baseClass.equals(int.class)) {
					baseName = "I";
				} else if (baseClass.equals(long.class)) {
					baseName = "J";
				} else if (baseClass.equals(short.class)) {
					baseName = "S";
				}
				return loadClass(arrayString.toString() + baseName);
			}
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}

	}

	public static Class getJavaClassInner(String type) {

		if (type.equals(DT_STRING))
			return String.class;
		if (type.equals(DT_SHORT))
			return Short.class;
		if (type.equals(DT_INTEGER))
			return Integer.class;
		if (type.equals(DT_LONG))
			return Long.class;
		if (type.equals(DT_DOUBLE))
			return Double.class;
		if (type.equals(DT_FLOAT))
			return Float.class;
		if (type.equals(DT_BYTE))
			return Byte.class;
		if (type.equals(DT_CHAR) || type.equals("Character"))
			return Character.class;
		if (type.equals(DT_BOOLEAN))
			return Boolean.class;
		if (type.equals(DT_DATE))
			return java.sql.Date.class;
		if (type.equals(DT_TIME))
			return java.sql.Time.class;
		if (type.equals(DT_DATETIME))
			return java.sql.Timestamp.class;
		if (type.equals(DT_OBJECT))
			return Object.class;
		if (type.equals(DT_short))
			return short.class;
		if (type.equals(DT_int))
			return int.class;
		if (type.equals(DT_long))
			return long.class;
		if (type.equals(DT_double))
			return double.class;
		if (type.equals(DT_float))
			return float.class;
		if (type.equals(DT_byte))
			return byte.class;
		if (type.equals(DT_char))
			return char.class;
		if (type.equals(DT_boolean))
			return boolean.class;
		try {
			return loadClass(type);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String getClassName(Class className) {
		String name = className.getName();
		return getClassName(name);
	}

	private static String getClassName(String name) {
		String arrays = "";
		if (name.indexOf("[") >= 0) {
			int point = 0;
			while (name.charAt(point) == '[') {
				arrays = arrays + "[]";
				++point;
			}
			if (name.charAt(point) == 'L') {
				name = name.substring(point + 1, name.length() - 1);
			} else if (name.charAt(point) == 'Z') {
				name = "boolean";
			} else if (name.charAt(point) == 'B') {
				name = "byte";
			} else if (name.charAt(point) == 'C') {
				name = "char";
			} else if (name.charAt(point) == 'D') {
				name = "double";
			} else if (name.charAt(point) == 'F') {
				name = "float";
			} else if (name.charAt(point) == 'I') {
				name = "int";
			} else if (name.charAt(point) == 'J') {
				name = "long";
			} else if (name.charAt(point) == 'S') {
				name = "short";
			}
		}
		int index = name.lastIndexOf('.');
		if (index > 0 && name.substring(0, index).equals("java.lang") == true) {
			name = name.substring(index + 1);
		}
		name = name + arrays;
		return name;
	}
	public static Class loadClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	}

	/**
	 * 替换字符串中的参数 replaceString("$1强化$2实施$2",new String[]{"qq","ff"})
	 * ="qq 强化 ff 实施 ff"
	 * 
	 * @param str
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static String replaceString(String str, Object[] parameters)
			throws Exception {
		if (str == null || parameters == null || parameters.length == 0) {
			return str;
		}
		Pattern p = Pattern.compile("\\$\\d+");
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			int index = Integer.parseInt(m.group().substring(1)) - 1;
			if (index < 0 || index >= parameters.length) {
				throw new Exception("设置的参数位置$" + (index + 1) + "超过了范围 "
						+ parameters.length);
			}
			m.appendReplacement(sb, " " + parameters[index].toString() + " ");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static Object getProperty(Object bean, Object name) {
		try {
			if (bean instanceof Class) {
				Field f = ((Class) bean).getDeclaredField(name.toString());
				return f.get(null);
			}else if(bean instanceof Map ){
				return ((Map)bean).get(name);
		    }else {
				Object obj = PropertyUtils.getProperty(bean, name.toString());
				return obj;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setProperty(Object bean, Object name, Object value) {
		try {
			if (bean instanceof Class) {
				Field f = ((Class) bean).getDeclaredField(name.toString());
				f.set(null, value);
			}else if(bean instanceof Map ){
				((Map)bean).put(name, value);
		    } else {
				PropertyUtils.setProperty(bean, name.toString(), value);
			}
		} catch (Exception e) {
			throw new RuntimeException("不能访问" + bean + "的property:" + name,e);
		}
	}

	public static Class getPropertyType(Object bean, String name) {
		try {
			return PropertyUtils.getPropertyType(bean, name);
		} catch (Exception e) {
			throw new RuntimeException("不能访问" + bean + "的property:" + name, e);
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(replaceString("$1强化$2实施$2", new String[] { "qq",
				"ff" }));
	}
    
}

