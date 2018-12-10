package com.medicento.retailerappmedi.data;

import java.io.Serializable;
import java.util.ArrayList;

public class RecentOrderDelivered implements Serializable {

    private String pOrderId;
    private String pDate;
    private int total;

    private ArrayList<RecentOrderMedicine> medicines;

    public ArrayList<RecentOrderMedicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<RecentOrderMedicine> medicines) {
        this.medicines = medicines;
    }

    public RecentOrderDelivered(String pOrderId, String pDate, int total) {
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
