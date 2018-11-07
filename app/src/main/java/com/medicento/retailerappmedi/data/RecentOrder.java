package com.medicento.retailerappmedi.data;

import java.util.ArrayList;

public class RecentOrder {
    private String pOrderId;
    private String pDate, status;
    private int total;

    public RecentOrder(String orderId, String date, String status, int total) {
        pOrderId = orderId;
        pDate = date;
        this.status = status;
        this.total = total;
    }

    public String getOrderId() {
        return pOrderId;
    }

    public String getDate() {
        return pDate;
    }

    public String getStatus() {
        return status;
    }

    public int getCost() {return  total;}
}
