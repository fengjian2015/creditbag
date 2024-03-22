package com.fly.credit.jiana.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;


import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * lucksoft
 * Created by AndroidDevelop on 2019年10月15日,0015.
 * 158045632@qq.com
 */

public class DeviceManager {
    /**
     * 屏幕 类型 // 无副屏 0*0  小副屏 t1sub7  大副屏 t1sub14
     */
    /** 屏幕类型  0 单屏幕  1 大小屏幕   2 双大屏幕  3，Presentation  Android双屏异显 hdmi*/
    public final static int NO_SECONDARY = 0;
    public final static int SMALL_SECONDARY = 1;
    public final static int LARGE_SECONDARY = 2;
    public final static int PRESENT_SECONDARY = 3;

    /** 台式收银机种类 **/
    public final static int DESKTOP_CASH_UNKNOWN = -1;
    public final static int DESKTOP_CASH_T1 = 0;
    public final static int DESKTOP_CASH_T2 = 1;
    public final static int DESKTOP_CASH_T2_MINI = 2;
    public final static int DESKTOP_CASH_T2_LITE = 3;
    public final static int DESKTOP_CASH_D1 = 4;
    //D1s_d也属于D1S  ，Qbao_d  也属于D1S ,新机型  Qbao_d
    public final static int DESKTOP_CASH_D1S = 5;
    //D2_d 也属于D2
    public final static int DESKTOP_CASH_D2 = 6;
    //D2s_d也属于D2S
    public final static int DESKTOP_CASH_D2S = 7;
    public final static int DESKTOP_CASH_D2_MINI = 8;
    public final static int DESKTOP_CASH_S2 = 9;
    public final static int DESKTOP_CASH_X2 = 10;
    public final static int DESKTOP_CASH_S2CC = 11;

    //美团收银机
    public final static int DESKTOP_CASH_MT_S4 = 1000;
    public final static int DESKTOP_CASH_MT_S4S = 1001;
    public final static int DESKTOP_CASH_MT_N2P = 1002;
    //内置称重
    public final static int DESKTOP_CASH_MT_L4 = 1003;
    public final static int DESKTOP_CASH_MT_S4MINI = 1004;
    public final static int DESKTOP_CASH_MT_OTHER = 1005;
    //一敏
    public final static int DESKTOP_CASH_YM = 1006;
    //一敏一体称
    public final static int DESKTOP_CASH_YM_D1w = 1007;
    //京东收银机
    public final static int DESKTOP_CASH_JDT = 1008;
    public final static int DESKTOP_CASH_Y2 = 1009;

    public int deviceType = 0;

    private final String TAG = DeviceManager.class.getSimpleName();

    private static double mInch = 0;
    /** 屏的个数有 1 ，2 */
    private int screenCount = 1;
    /** 屏的种类，无副屏，小副屏，大副屏 **/
    private int screenType = 0;
    private String model = "";
    public static DeviceManager manager = null;

    private Display[] displays = null;//屏幕数组
    private boolean isMinScreen = false;//小屏
    private Context context = null;

    public int getDeviceType() {
        return deviceType;
    }

    public int getScreenCount() {
        LogUtils.d(" screenCount: " + screenCount);
        return screenCount;
    }

    public int getScreenType() {
        return screenType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    private DeviceManager() {
    }

    public void startInit(Application application) {
        context = application.getApplicationContext();

        String brand = Build.BRAND;
        String model = Build.MODEL;
        LogUtils.f(" brand: " + brand + "   model: " + model);
        // 无副屏 0*0  7寸副屏 t1sub7  14寸副屏 t1sub14
        String subModel = Settings.Global.getString(application.getContentResolver(), "sunmi_sub_model");
        LogUtils.v(" 子型号:  " + subModel);

        if (!TextUtils.isEmpty(brand)) {
            if (brand.equals("SUNMI")) {
                if (!TextUtils.isEmpty(model)) {
                    getDeviceParams(model, subModel);
                } else {
                    LogUtils.e("此种情况应该没有");
                }
            } else {
                if (brand.equals("JDT") || brand.equals("JDD")) {
                    if (!TextUtils.isEmpty(model)) {
                        getDeviceParams(model, subModel);
                        deviceType = DESKTOP_CASH_JDT;
                    } else {
                        LogUtils.e("此种情况应该没有");
                    }
                }else if (TextUtils.equals(model, "AURORA Y2")) {
                    getDeviceParams(model, subModel);
                    deviceType = DESKTOP_CASH_Y2;
                } else if (YiminUtil.isYimin()){
                    //一敏D3机器是rockchip
                    model = YiminUtil.getYiminModel();
                    brand = YiminUtil.getYiminBrand();
                    LogUtils.f("一敏 brand: " + brand + "   model: " + model);
                    getDeviceParams(model, subModel);
                    YiminUtil.getYiminModel();
                    if (YiminUtil.getYiminModel().equals("D1w-702")){
                        deviceType = DESKTOP_CASH_YM_D1w;
                    }else {
                        deviceType = DESKTOP_CASH_YM;
                    }
                } else if (MTUtil.isMTDevice()){
                    getDeviceParams(model, subModel);
                }
                /**  非商米的设备全部默认处理 **/
                //deviceType = DESKTOP_CASH_D1;
            }
        }
        /*****************************打印显示检测结果******************************/
        LogUtils.f(" 机型种类 " + getDeviceType());
        LogUtils.f(" 副屏幕个数: " + getScreenCount());
        LogUtils.f(" 副屏类型: " + getScreenType());
        //LogUtils.f(" 打印机型号: ");
    }

    public static DeviceManager getInstance() {
        if (null == manager) {
            synchronized (DeviceManager.class) {
                if (null == manager) {
                    manager = new DeviceManager();
                }
            }
        }
        return manager;
    }

    public void init(Context context) {
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        if (displays.length > 1) {
            Rect outSize0 = new Rect();
            displays[0].getRectSize(outSize0);

            Rect outSize1 = new Rect();
            displays[1].getRectSize(outSize1);

            if (outSize0.right - outSize1.right > 100) {
                //是小屏
                isMinScreen = true;
            }
            screenCount = 2;
            LogUtils.f(" screenCount = 2");
        }
    }

    public Display[] getDisplays() {
        return displays;
    }

    public boolean isMinScreen() {
        return isMinScreen;
    }

    public Display getPresentationDisplays() {
        for (int i = 0; i < displays.length; i++) {
            Log.e(TAG, "屏幕" + displays[i]);
            if ((displays[i].getFlags() & Display.FLAG_SECURE) != 0
                    && (displays[i].getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (displays[i].getFlags() & Display.FLAG_PRESENTATION) != 0) {
                Log.e(TAG, "第一个真实存在的副屏屏幕" + displays[i]);
                return displays[i];
            }
        }
        return null;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return
     */
    public static double getScreenInch(Activity context) {
        if (mInch != 0.0d) {
            return mInch;
        }

        try {
            int realWidth = 0, realHeight = 0;
            Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (Build.VERSION.SDK_INT < 17
                    && Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }

            mInch = formatDouble(Math.sqrt((realWidth / metrics.xdpi) * (realWidth / metrics.xdpi) + (realHeight / metrics.ydpi) * (realHeight / metrics.ydpi)), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInch;
    }

    /**
     * Double类型保留指定位数的小数，返回double类型（四舍五入）
     * newScale 为指定的位数
     */
    private static double formatDouble(double d, int newScale) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 通过设备 设置参数值
     * 1。打印 2。副屏相关 3。摄像头 ,4.媒体播放，视频 音乐  5。读卡器
     *
     * @param model
     * @param subModel
     */
    private void getDeviceParams(String model, String subModel) {
        if (TextUtils.isEmpty(subModel)) {
            subModel = "";
        }
        setModel(model);

        /*** 检测屏幕个数 **/
        init(context);

        if(model.equals("t1host") || model.equals("T1-G")){
            deviceType = DESKTOP_CASH_T1;
            if (subModel.contains("0*0")) {
                screenCount = 1;
                screenType = NO_SECONDARY;
            } else if (subModel.contains("t1sub7")) {
                screenCount = 2;
                screenType = SMALL_SECONDARY;
            } else if (subModel.contains("t1sub14")) {
                screenCount = 2;
                screenType = LARGE_SECONDARY;
            } else {
                //deviceType = DESKTOP_CASH_UNKNOWN;
            }
        } else if (model.equals("t1sub14")) {
            /** 程序运行在副屏上显示**/
            screenType = LARGE_SECONDARY;
            //screenType = Constant.T1_14INCH_SECONDARY;
            //LogUtils.v("  在副屏上运行 " + screenType);
            return;
        } else if(model.equals("T2")) {
            deviceType = DESKTOP_CASH_T2;
            if (subModel.contains("0*0")) {
                screenCount = 1;
                screenType = NO_SECONDARY;
            } else if (subModel.contains("t1sub7")) {
                screenCount = 2;
                screenType = SMALL_SECONDARY;
            } else if (subModel.contains("t1sub14")) {
                screenCount = 2;
                screenType = LARGE_SECONDARY;
            } else {
                //deviceType = DESKTOP_CASH_UNKNOWN;
            }
        } else if(model.equals("T2lite")) {
            deviceType = DESKTOP_CASH_T2_LITE;
        } else if(model.equals("D1")) {
            deviceType = DESKTOP_CASH_D1;
        } else if(model.equals("D1s") || model.startsWith("D1s_")) {
            deviceType = DESKTOP_CASH_D1S;
        } else if(model.equals("D2") || model.startsWith("D2_")) {
            deviceType = DESKTOP_CASH_D2;
        } else if(model.equals("D2s") || model.startsWith("D2s_")) {
            deviceType = DESKTOP_CASH_D2S;
        }  // S2_CC-Ant  DESKTOP_CASH_S2CC
        else if (model.startsWith("S2_CC")) {
            deviceType = DESKTOP_CASH_S2CC;
        }
        else if (model.startsWith("S2")) {
            deviceType = DESKTOP_CASH_S2;
        } else if(model.equals("X2")){
            deviceType = DESKTOP_CASH_X2;
        } else if (model.equals("Qbao_d")) {
            deviceType = DESKTOP_CASH_D1S;
        } else {
            LogUtils.f("其它情况 机型 " + model);
        }
        if (model.startsWith("MT-")){
            if (model.equals("MT-S4Sp")) {
                deviceType = DESKTOP_CASH_MT_S4S;
            }  else if (model.equals("MT-S4_Mini")) {
                deviceType = DESKTOP_CASH_MT_S4MINI;
            }  else if (model.equals("MT-L4")) {
                deviceType = DESKTOP_CASH_MT_L4;
            } else if (model.equals("MT-S4p")) {
                deviceType = DESKTOP_CASH_MT_S4;
            } else if (model.equals("MT-N2p")) {
                deviceType = DESKTOP_CASH_MT_N2P;
            } else {
                deviceType = DESKTOP_CASH_MT_OTHER;
                LogUtils.f("其它情况 机型 " + model);
            }
        }
        Display[] displays = getDisplays();
        if (displays.length > 1) {
            screenCount = displays.length;
            screenType = PRESENT_SECONDARY;
           // deviceType = DeviceManager.D2_10INCH_SECONDARY;
            //deviceParameter.getScreen().setScreenType(3);
        } else {
            //deviceType = DeviceManager.D2_NO_SECONDARY;
            ////deviceParameter.getScreen().setScreenType(0);
        }
        LogUtils.f(" 屏幕数量: " + displays.length);
        int realWidth = 0, realHeight = 0;
        for (int i = 0; i < displays.length; i++) {
            DisplayMetrics dm = new DisplayMetrics();
            displays[i].getMetrics(dm);
            LogUtils.f(" widthPixelsL: " + dm.widthPixels + "   heightPixels: " + dm.heightPixels + "  xdpi: " + dm.xdpi + "  ydpi: " + dm.ydpi);
            LogUtils.f(" Dpi: " + dm.densityDpi + " density: " + dm.density);

            Point size = new Point();
            displays[i].getRealSize(size);
            realWidth = size.x;
            realHeight = size.y;
            double mInch = formatDouble(Math.sqrt((realWidth / dm.xdpi) * (realWidth / dm.xdpi) + (realHeight / dm.ydpi) * (realHeight / dm.ydpi)), 1);
            LogUtils.i("  屏幕 " + i + " 尺寸: " + mInch);
        }
        LogUtils.f(" screenCount: " + screenCount);
        LogUtils.d("  model: " + model);
        if(model.endsWith("Ant")){ /*** 支付宝机器 上面代码获取屏幕只有一个，实际是两个屏幕!!! ***/
            screenCount =  screenCount + 1;
        }
        //boolean isSmilePay = isHaveSunmiPay();
        ////serviceIsRunning(this, "com.alipay.zoloz.smile2pay.service.Smile2PayService");
        //deviceParameter.setSmile2pay(isSmilePay);
        //LogUtils.v(" 机器参数： " + new Gson().toJson(deviceParameter));
        //if(deviceParameter.isSmile2pay()){
        //    // 服务初始化
        //    PaymentService.getInstance().init(this);
        //}
    }
}
