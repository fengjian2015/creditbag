package com.fly.credit.jiana.bean;

public class RoomComListBean {

    private String Id="";
    private String OrderID="";
    private String DetailID="";
    private String GoodsID="";
    private String StaffID="";
    private String StaffName="";
    private int StaffType;
    private int AssignType;
    private int IsResting;
    private String ClassName="";
    private String ShopID="";
    private double CommissionMoney;
    private String Remark="";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getDetailID() {
        return DetailID;
    }

    public void setDetailID(String detailID) {
        DetailID = detailID;
    }

    public String getGoodsID() {
        return GoodsID;
    }

    public void setGoodsID(String goodsID) {
        GoodsID = goodsID;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }

    public int getStaffType() {
        return StaffType;
    }

    public void setStaffType(int staffType) {
        StaffType = staffType;
    }

    public int getAssignType() {
        return AssignType;
    }

    public void setAssignType(int assignType) {
        AssignType = assignType;
    }

    public int getIsResting() {
        return IsResting;
    }

    public void setIsResting(int isResting) {
        IsResting = isResting;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public double getCommissionMoney() {
        return CommissionMoney;
    }

    public void setCommissionMoney(double commissionMoney) {
        CommissionMoney = commissionMoney;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
