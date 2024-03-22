package com.fly.credit.jiana.util;

public interface IScaleProcess {

    boolean match();

    float getScaleNet();

    float getScaleTare();

    void update(float netWeight, float tareWeight);

    boolean isConnect();

    /**
     * 重置
     */
    void forceZeroing();

    /**
     * 去皮
     */
    void tare();


    void checkScaleState(int checkStateTimeOut, boolean isRecheck);

    void setReadState(boolean stopRead);

    /**
     * 设置单价与总价
     *
     * @param unitPrice  单价
     * @param totalPrice 总价
     * @return -1:失败  0:成功
     */
    boolean setUnitAndTotalPrices(float unitPrice, float totalPrice);
}
