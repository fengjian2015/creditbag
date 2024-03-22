package com.fly.credit.jiana.util;

import android.os.Build;

public class MTUtil {
    public static boolean isMTDevice(){
        String model = Build.MODEL;
        if (model.startsWith("MT-")){
            return true;
        }
        return false;
    }

    /**
     * 区分打印服务使用哪个
     * MT-S4Sp 请参考 打印服务SDK
     * MT-L4、MT-S4_Mini 请参考 硬件服务SDK
     */
    public static boolean isMTS4Sp(){
        String model = Build.MODEL;
        if (model.equals("MT-S4Sp")){
            return true;
        }
        return false;
    }

    /**
     * 打印是否使用硬件sdk
     * @return
     */
    public static boolean isPrinterHardwareSDK(){
        String model = Build.MODEL;
        if (model.equals("MT-L4") || model.equals("MT-S4_Mini")){
            return true;
        }
        return false;
    }
}
