package com.example.partner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CallHistory {
    private String trainer_id;
    private String user_id;
    private Date start_time;
    private Date end_time;
    private long call_duration;

    public CallHistory(String trainer_id, String user_id, Date start_time, Date end_time, long call_duration) {
        this.trainer_id = trainer_id;
        this.user_id = user_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.call_duration = call_duration;
    }

    public Date getDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = dateFormat.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(start_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public long getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(long call_duration) {
        this.call_duration = call_duration;
    }
}
