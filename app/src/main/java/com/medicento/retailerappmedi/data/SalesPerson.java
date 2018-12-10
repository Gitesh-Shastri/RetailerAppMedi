package com.medicento.retailerappmedi.data;

import java.io.Serializable;

public class SalesPerson implements Serializable{
    private String mName;
    private Long mTotalSales;
    private int mNoOfOrder;
    private int mReturn;
    private float mEarnings;
    private String mId;
    private String mAllocatedAreaId, mAllocatedPharmaId;
    private String usercode;

    public SalesPerson() {
    }

    public SalesPerson(String name, Long totalSales, int noOfOrder, int returns, float earnings, String id, String allocatedAreaId, String pId) {
        mName = name;
        mTotalSales = totalSales;
        mNoOfOrder = noOfOrder;
        mReturn = returns;
        mEarnings = earnings;
        mId = id;
        mAllocatedAreaId = allocatedAreaId;
        mAllocatedPharmaId = pId;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getName() {
        return mName;
    }

    public float getTotalSales() {
        return mTotalSales;
    }

    public int getNoOfOrder() {return mNoOfOrder;}

    public int getReturn() {
        return mReturn;
    }

    public float getEarnings() {
        return mEarnings;
    }

    public String getId() {
        return mId;
    }

    public String getAllocatedArea() {
        return mId;
    }

    public String getmAllocatedPharmaId() {
        return mAllocatedPharmaId;
    }
}
