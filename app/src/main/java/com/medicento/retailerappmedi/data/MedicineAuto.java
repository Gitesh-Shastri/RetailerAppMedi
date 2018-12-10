package com.medicento.retailerappmedi.data;

public class MedicineAuto {

    private String name, company;
    private int mrp;

    public MedicineAuto(String name, String company, int mrp) {
        this.name = name;
        this.company = company;
        this.mrp = mrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }
}
