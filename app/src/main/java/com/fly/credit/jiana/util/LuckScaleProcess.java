package com.fly.credit.jiana.util;

import android.content.Context;
import android.os.Build;


import java.lang.reflect.Method;

public class LuckScaleProcess {
    public static int selectedScaleType = 0;
    public static int selectedScaleConnectType = 0;

    public static boolean isInitSerialPortServiceSuccess = false;

    public static boolean startScaleService(int dataObserverType, Context context,boolean isTest ,int selectedTestScaleConnectType,int selectedScaleType) {


        switch (dataObserverType) {
            case 0:
            case 2: {
                LuckScaleProcess.selectedScaleType = selectedScaleType;
                //dataObserverType=1表示是设置界面接收数据
                return true;
            }
        }
        return false;
    }

}
