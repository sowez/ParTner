package com.example.partner;

import java.util.ArrayList;

public class TrainerProfile {
    private String _id;
    private String id;
    private String pw;
    private String sex;
    private String name;
    private String token;
    private String self_introduction;
    private Integer star_rate;
    private Integer star_rate_num;
    private ArrayList<String> training_type;
    private ArrayList<CallHistory> call_history;
    private String state;
    private String profileImg;
    private boolean isBookmarked = false;
    private String qb_id;

    public TrainerProfile(String _id, String id, String pw, String sex, String name, String token, String self_introduction, Integer star_rate, Integer star_rate_num, ArrayList<String> training_type, ArrayList<CallHistory> call_history, String state, String profileImg, String qb_id) {
        this._id = _id;
        this.id = id;
        this.pw = pw;
        this.sex = sex;
        this.name = name;
        this.token = token;
        this.self_introduction = self_introduction;
        this.star_rate = star_rate;
        this.star_rate_num = star_rate_num;
        this.training_type = training_type;
        this.call_history = call_history;
        this.state = state;
        this.profileImg = profileImg;
        this.qb_id = qb_id;
    }

    public String get_id() {
        return _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

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

    public Integer getStar_rate() {
        return star_rate;
    }

    public void setStar_rate(Integer star_rate) {
        this.star_rate = star_rate;
    }

    public Integer getStar_rate_num() {
        return star_rate_num;
    }

    public void setStar_rate_num(Integer star_rate_num) {
        this.star_rate_num = star_rate_num;
    }

    public ArrayList<String> getTraining_type() {
        return training_type;
    }

    public void setTraining_type(ArrayList<String> training_type) {
        this.training_type = training_type;
    }

    public ArrayList<CallHistory> getCall_history() {
        return call_history;
    }

    public void setCall_history(ArrayList<CallHistory> call_history) {
        this.call_history = call_history;
    }

    public String getOnline() {
        return state;
    }

    public void setOnline(String online) {
        this.state = online;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String img) {
        this.profileImg = profileImg;
    }

    public boolean getBookmarked() {
        return this.isBookmarked;
    }

    public void setBookmarked(boolean checked) {
        this.isBookmarked = checked;
    }

    public String getQb_id() {
        return this.qb_id;
    }
}
