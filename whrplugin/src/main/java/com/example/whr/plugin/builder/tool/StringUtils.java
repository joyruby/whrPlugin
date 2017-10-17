package com.example.whr.plugin.builder.tool;

/**
 * Created by whrwhr446 on 25/09/2017.
 */

public class StringUtils {

    /**
     *
     * @param container
     * @param sub
     * @return
     */
    public static Boolean  lowercaseContains(String container, String sub){
        if(container == null || sub == null) return false;
        return container.toLowerCase().contains(sub.toLowerCase());
    }

    /**
     *
     * @param str1
     * @param str2
     * @return
     */
    public static Boolean  lowercaseEqual(String str1, String str2){
        if(str1 == null || str2 == null) return false;
        return str1.toLowerCase().equals(str2.toLowerCase());
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */

    public static String upperCase(String str){
        return  str.substring(0,1).toUpperCase()+str.substring(1);
    }

}
