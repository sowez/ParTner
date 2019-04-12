package com.example.partner;

public class TrainerProfile {
    private String id;
    private String pw;
    private String sex;
    private String name;
    private String self_introduction;
    private Integer star_rate;
    private String[] training_type;
    private String online;
    private String img;

    public TrainerProfile(String sex, String name, String self_introduction, Integer star_rate, String[] training_type) {
        this.sex = sex;
        this.name = name;
        this.self_introduction = self_introduction;
        this.star_rate = star_rate;
        this.training_type = training_type;
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

    public String[] getTraining_type() {
        return training_type;
    }

    public void setTraining_type(String[] training_type) {
        this.training_type = training_type;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
