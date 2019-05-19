package com.example.partner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrainingHistory {
    private Date start_time, end_time;
    private int ex_count, ex_type;

    public TrainingHistory(Date start_time, Date end_time, int ex_count, int ex_type){
        this.start_time = start_time;
        this.end_time = end_time;
        this.ex_count = ex_count;
        this.ex_type = ex_type;
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

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
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
}
