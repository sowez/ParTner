package com.example.partner;

public class CallData {

    private boolean Called;
    private String callReceiverID;
    private String callReceiverName;
    private long callTime;
    private boolean trainerBookmarked;


    public boolean isCalled() {
        return Called;
    }

    public void setCalled(boolean called) {
        Called = called;
    }

    public String getCallReceiverID() {
        return callReceiverID;
    }

    public void setCallReceiverID(String callReceiver) {
        this.callReceiverID = callReceiver;
    }

    public String getCallReceiverName() {
        return callReceiverName;
    }

    public void setCallReceiverName(String callReceiverName) {
        this.callReceiverName = callReceiverName;
    }

    public long getCallTime() {
        return callTime;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }

    private static CallData instance = null;

    public void setTrainerBookmarked(boolean checked){this.trainerBookmarked = checked;}

    public boolean getTrainerBookmarked(){return this.trainerBookmarked; }


    public static synchronized CallData getInstance(){

        if(null==instance){

            instance = new CallData();

        }

        return instance;

    }

}
