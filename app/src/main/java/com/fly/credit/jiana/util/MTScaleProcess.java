package com.fly.credit.jiana.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;



/**
 * 美团一体机
 */
public class MTScaleProcess implements IScaleProcess {
    private Runnable scaleRunnable = null;
    private Handler scaleHandler = null;
    private float scaleNet = 0;
    private float scaleTare = 0;
    private int checkStatus = 0;
    private Handler timeOutHandler = null;
    private Runnable timeOutRunnable = null;


    //功能：连接称重服务
    private void connectScaleService() {
        if (scaleRunnable == null) {
            if (scaleHandler == null) {
                scaleHandler = new Handler(Looper.getMainLooper());
            }
            scaleRunnable = new Runnable() {
                @Override
                public void run() {
                    update(scaleNet, scaleTare);
                }
            };
            getScaleData();
        }
    }

    //功能：获取到重量，更新数据
    private void getScaleData() {
    }

    @Override
    public boolean match() {
        return true;
    }

    @Override
    public float getScaleNet() {
        return scaleNet;
    }

    @Override
    public float getScaleTare() {
        return scaleTare;
    }


    /**
     * 电子秤防回调，更新重量
     *
     * @param netWeight，净重
     * @param tareWeight ss
     * @return
     */
    @Override
    public void update(float netWeight, float tareWeight) {
    }

    @Override
    public boolean isConnect() {
        return false;
    }

    /**
     * 设置单价与总价
     *
     * @param unitPrice  单价
     * @param totalPrice 总价
     * @return -1:失败  0:成功
     */
    public boolean setUnitAndTotalPrices(float unitPrice, float totalPrice) {
        boolean isSuccess = false;
        if (isConnect()) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 重置
     */
    @Override
    public void forceZeroing() {
        if (isConnect()) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 去皮
     */
    @Override
    public void tare() {
        if (isConnect()) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置电子秤客显亮灭状态 (收银机重启后，会恢复为点亮状态)
     *
     * @param type   0：单价 1：总价
     * @param enable true：亮 false：灭
     * @return 0:成功  -1:失败
     */
    public int setScaleDisplayEnable(int type, boolean enable) {
        return 2;
    }



    @Override
    public void checkScaleState(int checkStateTimeOut, boolean isRecheck) {
        if (timeOutHandler == null) {
            timeOutHandler = new Handler();
        }

        if (timeOutRunnable == null) {
            timeOutRunnable = new Runnable() {
                @Override
                public void run() {
                    if (checkStatus == 0) {
                        //超时了
                        checkStatus = -1;
                        setReadState(true);

                    }
                }
            };
        }

        timeOutHandler.postDelayed(timeOutRunnable, checkStateTimeOut);
        //重新检测数据状态时，Thread已启动，所以不需要再启动
        if (isRecheck) {
            checkStatus = 0;
            setReadState(false);
        }
    }

    @Override
    public void setReadState(boolean stopRead) {
        //scaleHandler==null表示称重服务还没连接成功，不能读取数据，否则崩溃
        if (scaleHandler != null) {
            if (stopRead) {
            } else {
                getScaleData();
            }
        }
    }
}
