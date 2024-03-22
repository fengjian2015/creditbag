package com.lucksoft.luckvoice.lib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Time：2023/2/18
 * Author：feng
 * Description：
 */
public class StringUtil {
    private final static String TAG = "StringUtil";

    public static boolean isLetter(String txt) {
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(txt);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
