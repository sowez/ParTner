package com.quickblox.sample.groupchatwebrtc.ParTner;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class SignUpData {
    private String result = "fail";
    private String type, id, pw, name, sex;
    private ArrayList<String> training_type;

    public SignUpData(String type, String id, String pw, String name, String sex, ArrayList<String> training_type) {
        this.type = type;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.sex = sex;
        this.training_type = training_type;
    }

    public String getResult() {
        return result;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public ArrayList<String> getTraining_type() {
        return training_type;
    }

}
