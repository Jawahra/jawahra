package com.example.jawahra.ui.visit;

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
