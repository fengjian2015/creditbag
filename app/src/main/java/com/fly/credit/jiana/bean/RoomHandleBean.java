package com.fly.credit.jiana.bean;

import java.util.List;

public class RoomHandleBean {

    private String Id = "";
    private String BillCode = "";
    private int OrderType;
    private String HandCode = "";
    private long CreateTime;
    private double TotalMoney;
    private String MemID = "";
    private String CardID = "";
    private String CardName = "";
    private String UseTime = "";
    private String RoomID = "";
    private String WaterBillCode = "";
    private String OrderID = "";
    private List<RoomComListBean> comList;
    private List<DetailListBean> detailList;
    private boolean isSelected = false;
    //预付款新加字段
    //是否存在预付款 true存在，false不存在
    private boolean IsAdvanceCharge = false;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBillCode() {
        return BillCode;
    }

    public void setBillCode(String billCode) {
        BillCode = billCode;
    }

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int orderType) {
        OrderType = orderType;
    }

    public String getHandCode() {
        return HandCode;
    }

    public void setHandCode(String handCode) {
        HandCode = handCode;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public double getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getMemID() {
        return MemID;
    }

    public void setMemID(String memID) {
        MemID = memID;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getUseTime() {
        return UseTime;
    }

    public void setUseTime(String useTime) {
        UseTime = useTime;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }

    public String getWaterBillCode() {
        return WaterBillCode;
    }

    public void setWaterBillCode(String waterBillCode) {
        WaterBillCode = waterBillCode;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public List<RoomComListBean> getComList() {
        return comList;
    }

    public void setComList(List<RoomComListBean> comList) {
        this.comList = comList;
    }

    public List<DetailListBean> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DetailListBean> detailList) {
        this.detailList = detailList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isAdvanceCharge() {
        return IsAdvanceCharge;
    }

    public void setAdvanceCharge(boolean advanceCharge) {
        IsAdvanceCharge = advanceCharge;
    }

    public static class DetailListBean {
        /**
         * Id : 15402322344163329
         * GoodsName : 面包
         * Price : 20
         * GoodsType : 1
         */

        private String Id = "";
        private String GoodsName = "";
        private double Price;
        private int GoodsType;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String GoodsName) {
            this.GoodsName = GoodsName;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public int getGoodsType() {
            return GoodsType;
        }

        public void setGoodsType(int GoodsType) {
            this.GoodsType = GoodsType;
        }
    }
}
