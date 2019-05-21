package com.example.partner;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TrainerEditData {

    private String id;
    private String name;
    private String self_introduction;
    private String sex;
    private ArrayList<String> training_type;

    public TrainerEditData(String id, String name, String self_introduction, String sex, ArrayList<String> training_type) {
        this.name = name;
        this.self_introduction = self_introduction;
        this.sex = sex;
        this.training_type = training_type;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelf_introduction() {
        return self_introduction;
    }

    public void setSelf_introduction(String self_introduction) {
        this.self_introduction = self_introduction;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ArrayList<String> getTraining_type() {
        return training_type;
    }

    public void setTraining_type(ArrayList<String> training_type) {
        this.training_type = training_type;
    }
}
