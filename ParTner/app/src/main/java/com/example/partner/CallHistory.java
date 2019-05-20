package com.example.partner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallHistory {
    private Date start_time;
    private Date end_time;
    private Integer call_duration;
    private String trainer;
    private String user;

    public CallHistory(Date start_time, Date end_time, Integer call_duration, String trainer, String user) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.call_duration = call_duration;
        this.trainer = trainer;
        this.user = user;
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

    public Integer getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(Integer call_duration) {
        this.call_duration = call_duration;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
