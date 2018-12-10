package com.medicento.retailerappmedi.data;

public class DistributorName {
    private String name;
    private int rating;

    public DistributorName(String name,int rating) {
        this.name = name;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
