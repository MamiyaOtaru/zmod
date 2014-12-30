package com.mamiyaotaru.zmod.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectionUtils {
	
	public static Object getPrivateFieldValueByType(Object o, Class objectClasstype, Class fieldClasstype) {   
		return getPrivateFieldValueByType(o, objectClasstype, fieldClasstype, 0);
	}
	
	public static Object getPrivateFieldValueByType(Object o, Class objectClasstype, Class fieldClasstype, int index) {   
		// Go and find the private field... 
		Class objectClass = o.getClass();
		while (!objectClass.equals(objectClasstype) && objectClass.getSuperclass() != null) {
			objectClass = objectClass.getSuperclass();
		}
		int counter = 0;
		final java.lang.reflect.Field fields[] = objectClass.getDeclaredFields();
		for (int i = 0; i < fields.length; ++i) {
			if (fieldClasstype.equals(fields[i].getType())) {
				if (counter == index) {
					try {
						fields[i].setAccessible(true);
						return fields[i].get(o);
					} 
					catch (IllegalAccessException ex) {
					}
				}
				counter++;
			}
		}
		return null;
	}
	
	// ATM used only for calling optifine's randomob method 
	public static Object getFieldValueByName(Object o, String fieldName) {   
		// Go and find the field... 
		final java.lang.reflect.Field fields[] = o.getClass().getFields();
		for (int i = 0; i < fields.length; ++i) {
			if (fieldName.equals(fields[i].getName())) {
				try {
					fields[i].setAccessible(true);
					return fields[i].get(o);
				} 
				catch (IllegalAccessException ex) {
					//Assert.fail ("IllegalAccessException accessing " + fieldName);
				}
			}
		}
		//Assert.fail ("Field '" + fieldName +"' not found");
		return null;
	}
	// why doesn't this work
	/*java.lang.reflect.Field privateField = null;
	  try {
		  privateField = o.getClass().getDeclaredField(fieldName);
	  }
	  catch (NoSuchFieldException e){}
	  privateField.setAccessible(true);
	  Object obj = null;
	  try {
		  obj = privateField.get(o);
	  }
	  catch (IllegalAccessException e){}
	  return obj;*/
	
	public static ArrayList<Field> getFieldsByType(Object o, Class objectClassBaseType, Class fieldClasstype) {   
		// Go and find the private field... 
		ArrayList<Field> matches = new ArrayList<Field>();
		Class objectClass = o.getClass();
		while (!objectClass.equals(objectClassBaseType) && objectClass.getSuperclass() != null) {
			final java.lang.reflect.Field fields[] = objectClass.getDeclaredFields();
			for (int i = 0; i < fields.length; ++i) {
				if (fieldClasstype.equals(fields[i].getType())) {
					fields[i].setAccessible(true);
					matches.add(fields[i]);
				}
			}
			objectClass = objectClass.getSuperclass();
		}
		return matches;
	}
	
	public static Field getFieldByType (Object o, Class objectClasstype, Class fieldClasstype) {   
		return getFieldByType(o, objectClasstype, fieldClasstype, 0);
	}
	
	public static Field getFieldByType (Object o, Class objectClasstype, Class fieldClasstype, int index) {   
		// Go and find the private field... 
		Class objectClass = o.getClass();
		while (!objectClass.equals(objectClasstype) && objectClass.getSuperclass() != null) {
			objectClass = objectClass.getSuperclass();
		}
		int counter = 0;
		final java.lang.reflect.Field fields[] = objectClass.getDeclaredFields();
		for (int i = 0; i < fields.length; ++i) {
			if (fieldClasstype.equals(fields[i].getType())) {
				if (counter == index) {
					fields[i].setAccessible(true);
					return fields[i];
				}
				counter++;
			}
		}
		return null;
	}
	
	public static Method getMethodByType(Class objectType, Class returnType, Class... parameterTypes) {   
		return getMethodByType(0, objectType, returnType, parameterTypes);
	}
	
	public static Method getMethodByType(int index, Class objectType, Class returnType, Class... parameterTypes) {
		Method[] methods = objectType.getDeclaredMethods();
		int counter = 0;
		for (int i = 0; i < methods.length; ++i) {
			if (returnType.equals(methods[i].getReturnType())) {
				Class[] methodParameterTypes = methods[i].getParameterTypes();
				if (parameterTypes.length == methodParameterTypes.length) {
					boolean match = true;
					for (int t = 0; t < parameterTypes.length; t++) {
						if (parameterTypes[t] != methodParameterTypes[t])
							match = false;
					}
					if (counter == index) {
						methods[i].setAccessible(true);
						return methods[i];
					}
				}
				counter++;
			}
		}
		return null;
	}
	
	public static boolean classExists(String className) {
		try {
			Class.forName (className);
			return true;
		}
		catch (ClassNotFoundException exception) {
			return false;
		}
	}
	
}
