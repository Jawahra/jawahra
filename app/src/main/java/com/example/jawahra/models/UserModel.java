package com.example.jawahra.models;

public class UserModel {

    public String username, email, imageUrl;

    public UserModel(){ }

    public UserModel(String username, String email, String imageUrl){
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
