package com.example.jawahra.models;

public class EmiratesModel {

    private String emirateName;

    private EmiratesModel() {}

    private EmiratesModel(String emirateName){
        this.emirateName = emirateName;
    }

    public String getEmirateName() {
        return emirateName;
    }

    public void setEmirateName(String emirateName) {
        this.emirateName = emirateName;
    }
}

