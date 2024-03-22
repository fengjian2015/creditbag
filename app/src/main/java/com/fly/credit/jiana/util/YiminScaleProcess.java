package com.fly.credit.jiana.util;

import android.os.Handler;
import android.os.Looper;


/**
 * 一敏一体机
 */
public class YiminScaleProcess {
    private Runnable scaleRunnable = null;
    private Handler scaleHandler = null;
    private float mWeight = 0;
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
                }
            };
            getScaleData();
        }
    }

    //功能：获取到重量，更新数据
    private void getScaleData() {
    }









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
