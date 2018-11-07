package com.medicento.retailerappmedi.data;

import java.io.Serializable;

public class OrderedMedicine implements Serializable{
    private String mMedicineName;
    private String mMedicineCompany;
    private int mQty;
    private float mRate;
    private String code;
    private float mCost;
    private int stock;

    public OrderedMedicine(){

    }

    public OrderedMedicine(String name, String company, int qty, float rate, float cost,int stock, String code){
        mMedicineName = name;
        mMedicineCompany = company;
        mQty = qty;
        mRate = rate;
        mCost = cost;
        this.code = code;
        this.stock  = stock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMedicineName() {
        return mMedicineName;
    }

    public String getMedicineCompany() {
        return mMedicineCompany;
    }

    public int getQty() {
        return mQty;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock() {

        return stock;
    }

    public float getRate() {
        return mRate;
    }

    public float getCost(){return mCost;}

    public void setMedicineCompany(String company) {
        this.mMedicineCompany = company;
    }

    public void setMedicineName(String name) {
        this.mMedicineName = name;
    }

    public void setQty(int qty) {
        this.mQty = qty;
    }

    public void setRate(float rate) {this.mRate = rate;}

    public void setCost(float cost){
        this.mCost = cost;
    }
}
