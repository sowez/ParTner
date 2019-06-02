package com.example.partner;

public class CallData {

    private boolean Called;
    private String callReceiverID;
    private String callReceiverName;
    private int Calltime;


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
        this.callReceiverID = callReceiverID;
    }

    public String getCallReceiverName() {
        return callReceiverName;
    }

    public void setCallReceiverName(String callReceiverName) {
        this.callReceiverName = callReceiverName;
    }

    public int getCalltime() {
        return Calltime;
    }

    public void setCalltime(int calltime) {
        Calltime = calltime;
    }

    private static CallData instance = null;


    public static synchronized CallData getInstance(){

        if(null==instance){

            instance = new CallData();

        }

        return instance;

    }

}
