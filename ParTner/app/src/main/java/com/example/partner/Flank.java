package com.example.partner;

public class Flank extends Exercise{

    private float[][] point;

    public Flank(int exCount){
        super(exCount);
    }

    @Override
    public boolean checkReady(){
        boolean isReady = false;
        return isReady;
    }

    // 플랭크 운동 동작 인식하는 함수
    @Override
    public boolean doExercise(int currentStep){

        return false;
    }

    // getter setter
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }
}
