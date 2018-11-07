package com.medicento.retailerappmedi.data;

import java.io.Serializable;

public class RecentOrderC implements Serializable {
    private String pOrderId;
    private String pDate;
    private int total;

    public RecentOrderC(String pOrderId, String pDate, int total) {
        this.pOrderId = pOrderId;
        this.pDate = pDate;
        this.total = total;
    }

    public String getpOrderId() {
        return pOrderId;
    }

    public String getpDate() {
        return pDate;
    }

    public int getTotal() {
        return total;
    }
}
