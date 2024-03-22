package com.fly.credit.jiana.bean;

import java.util.List;

public class RoomBean {

    /**
     * RoomID : 15362663728609280
     * RoomName : 谁是卧底一号桌
     * RoomStatus : 3
     * TotalMoney : 240
     * OrderList : [{"Id":"15402322344163328","BillCode":"XF15402322344163328","OrderType":2,"HandCode":"4440","CreateTime":20210803141231,"TotalMoney":70,"MemID":"45508654","CardID":"0000","CardName":"散客","UseTime":"2:55","RoomID":"15362663728609280","comList":[],"detailList":[{"Id":"15402322344163329","GoodsName":"面包","Price":20,"GoodsType":1},{"Id":"15402322344359936","GoodsName":"碧螺春","Price":50,"GoodsType":1},{"Id":"15402322344261632","GoodsName":"上网计时","Price":0,"GoodsType":3}]},{"Id":"15402321057691648","BillCode":"XF15402321057691648","OrderType":2,"HandCode":"8199","CreateTime":20210803141113,"TotalMoney":170,"MemID":"45508654","CardID":"0000","CardName":"散客","UseTime":"2:56","RoomID":"15362663728609280","comList":[],"detailList":[{"Id":"15402321815861248","GoodsName":"水果拼盘","Price":50,"GoodsType":1},{"Id":"15402321816205312","GoodsName":"面包","Price":20,"GoodsType":1},{"Id":"15402321815926784","GoodsName":"游泳计时","Price":0,"GoodsType":3},{"Id":"15402321815992320","GoodsName":"蒸桑拿计时","Price":0,"GoodsType":3},{"Id":"15402321816074240","GoodsName":"上网计时","Price":0,"GoodsType":3},{"Id":"15402321816139776","GoodsName":"游戏陪玩","Price":0,"GoodsType":3}]}]
     */

    private String RoomID = "";
    private String RoomName = "";
    private int RoomStatus;
    private double TotalMoney;
    private List<RoomHandleBean> OrderList;
    private boolean isFoucus = false;

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String RoomID) {
        this.RoomID = RoomID;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }

    public int getRoomStatus() {
        return RoomStatus;
    }

    public void setRoomStatus(int RoomStatus) {
        this.RoomStatus = RoomStatus;
    }

    public double getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(double TotalMoney) {
        this.TotalMoney = TotalMoney;
    }

    public List<RoomHandleBean> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<RoomHandleBean> OrderList) {
        this.OrderList = OrderList;
    }

    public boolean isFoucus() {
        return isFoucus;
    }

    public void setFoucus(boolean foucus) {
        isFoucus = foucus;
    }
}
