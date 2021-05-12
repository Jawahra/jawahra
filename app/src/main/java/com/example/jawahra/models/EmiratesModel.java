package com.example.jawahra.models;

import com.google.firebase.firestore.DocumentSnapshot;

public class EmiratesModel{
    private String emirateName;
    private String coverImg;

    private EmiratesModel() {
    }

    private EmiratesModel(String emirateName, String coverImg){

        this.emirateName = emirateName;
        this.coverImg = coverImg;
    }

    public String getEmirateName() {
        return emirateName;
    }

    public void setEmirateName(String emirateName) {
        this.emirateName = emirateName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}

