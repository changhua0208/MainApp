package com.jch.plugin.clazz;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射相关的工具类
 *
 * @author changhua.jiang
 * @since 2018/1/23 上午11:22
 */

public class ReflectUtils {

    /**
     * 获得一个对象的成员变量
     *
     * @param clazz     成员变量所在的类
     * @param ref       对象引用
     * @param fieldName 成员变量名称
     * @return 成员变量值
     */
    public static Object getFieldValue(Class clazz, Object ref, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(ref);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(String.format("get field value failed class : %s field : %s", clazz.getName(), fieldName));
        }
    }

    public static Object getFieldValue(String clazzName, Object ref, String fieldName) {
        try {
            Class clazz = Class.forName(clazzName);
            return getFieldValue(clazz,ref,fieldName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("get field value failed class : %s field : %s", clazzName, fieldName));
        }
    }

    /**
     * 调用一个对象的方法
     *
     * @param ref        对象引用
     * @param methodName 方法名称
     * @param params     参数
     * @return 返回值
     */
    public static Object invokeMethod(Object ref, String methodName, Object... params) {
        Class[] classes = null;
        if (params != null && params.length > 0) {
            classes = new Class[params.length];
            int index = 0;
            for (Object param : params) {
                classes[index++] = param.getClass();
            }
        }
        return invokeMethod(ref, methodName, classes, params);
    }

    public static Object invokeMethod(Object ref, String methodName, Class[] classes, Object[] params) {
        return invokeMethod(ref.getClass(),ref,methodName,classes,params);
    }

    public static Object invokeMethod(Class clazz,Object ref,String methodName,Class[] classes,Object[] params){
        Object ret = null;
        try {
            Method method = clazz.getMethod(methodName, classes);
            method.setAccessible(true);
            ret = method.invoke(ref, params);
        } catch (Exception e) {
            throw new RuntimeException(String.format("invoke method failed class : %s method : %s", ref.getClass(), methodName));
        }
        return ret;
    }

    public static Object invokeMethod(String clazzName,Object ref,String methodName,Class[] classes,Object[] params){
        try {
            Class clazz = Class.forName(clazzName);
            return invokeMethod(clazz,ref,methodName,classes,params);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("invoke method failed class : %s method : %s", ref.getClass(), methodName));
        }
    }

    /**
     * 反射需要设置字段的类并设置新字段
     * @param obj
     * @param cl
     * @param field
     * @param value
     */
    public static void setFieldValue(Class<?> cl, Object obj,String field,
                                 Object value)  {
        Field localField = null;
        try {
            localField = cl.getDeclaredField(field);
            localField.setAccessible(true);
            localField.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(String.format("set Class %s,Field %s error", obj.getClass(), field));
        }

    }

    public static void setFieldValue(Object obj,String field,
                                     Object value)  {
        setFieldValue(obj.getClass(),obj,field,value);

    }

    public static void setFieldValue(String clazzName, Object obj, String field,
                                     Object value)  {
        try {
            Class clazz = Class.forName(clazzName);
            setFieldValue(clazz,obj,field,value);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("set Class %s,Field %s error", obj.getClass(), field));
        }

    }
}
