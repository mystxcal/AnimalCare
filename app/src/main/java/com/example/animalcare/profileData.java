package com.example.animalcare;

import android.widget.EditText;

public class profileData {
    String name,about,no,imagename,imageUrl;

    public profileData(String name, String about, String no) {
        this.name = name;
        this.about = about;
        this.no = no;
       // this.imageUrl = imageUrl;
    }

    public profileData( String imageUrl) {
        //this.imagename = imagename;
        this.imageUrl = imageUrl;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
