package com.example.android.tonight;

import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private Uri pictureUrl;

    public User(String name, String email, Uri url){
        this.name = name;
        this.email = email;
        this.pictureUrl = url;
    }

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(Uri pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
