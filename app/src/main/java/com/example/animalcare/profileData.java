package com.example.animalcare;

import android.widget.EditText;

public class profileData {
    String name,about,no;

    public profileData(String name, String about, String no) {
        this.name = name;
        this.about = about;
        this.no = no;
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
}
