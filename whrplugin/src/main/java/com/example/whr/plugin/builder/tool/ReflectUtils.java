package com.example.whr.plugin.builder.tool;

import java.lang.reflect.Field;

/**
 * Created by whrwhr446 on 18/07/2017.
 */

public class ReflectUtils {
    public static Object getField(Class parentClazz,Object obj,String fieldName){
        try {
            Field t = parentClazz.getDeclaredField(fieldName);
            t.setAccessible(true);
            return t.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void updateField(Class parentClazz,Object obj,String fieldName,Object value){
        try {
            Field t = parentClazz.getDeclaredField(fieldName);
            t.setAccessible(true);
            t.set(obj,value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
