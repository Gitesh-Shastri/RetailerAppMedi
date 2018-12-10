package com.medicento.retailerappmedi.data;

import java.io.Serializable;

public class MakeYourOwn implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MakeYourOwn(String name) {

        this.name = name;
    }
}
