package com.example.partner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrainingHistory {
    private Date start_time;
    private int ex_count, ex_type, ex_difficulty, ex_accuracy;

    public TrainingHistory(Date start_time, int ex_count, int ex_type, int ex_difficulty, int ex_accuracy){
        this.start_time = start_time;
        this.ex_count = ex_count;
        this.ex_type = ex_type;
        this.ex_difficulty = ex_difficulty;
        this.ex_accuracy = ex_accuracy;

    }

    public Date getDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(start_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public int getEx_count() {
        return ex_count;
    }

    public void setEx_count(int ex_count) {
        this.ex_count = ex_count;
    }

    public int getEx_type() {
        return ex_type;
    }

    public void setEx_type(int ex_type) {
        this.ex_type = ex_type;
    }

    public int getEx_difficulty() {
        return ex_difficulty;
    }

    public void setEx_difficulty(int ex_difficulty) {
        this.ex_difficulty = ex_difficulty;
    }

    public int getEx_accuracy() {
        return ex_accuracy;
    }

    public void setEx_accuracy(int ex_accuracy) {
        this.ex_accuracy = ex_accuracy;
    }

}
