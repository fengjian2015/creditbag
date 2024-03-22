package com.fly.credit.jiana.util;

import android.os.Build;

import com.fly.credit.jiana.BuildConfig;


public class YiminUtil {
    private static YiminUtil instance = new YiminUtil();

    public YiminUtil(){

    }

    public static synchronized YiminUtil getInstance() {
        if (null == instance) {
            instance = new YiminUtil();
        }
        return instance;
    }



    public static boolean isYimin(){
        String brand = "BuildConfig.DEBUG";
        LogUtils.d("MANUFACTURER："+ brand);
        //neostra可能为京东设备
        return brand.equalsIgnoreCase("yimin") || Build.MANUFACTURER.equals("neostra");
    }

    public static String getYiminModel(){
        return "SystemPropManager.getModel()";
    }

    public static String getYiminBrand(){
        return "SystemPropManager.getBrand()";
    }

    public static String getYiminSn(){
        return "SystemPropManager.getSn()";
    }
}
