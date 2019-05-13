package com.example;

public class Flank extends Exercise{

    private float[][] point;

    public Flank(int exCount){
        super(exCount);
    }

    @Override
    public boolean checkReady(){
        boolean isReady = true;
        return isReady;
    }

    // 점핑잭 운동 동작 인식하는 함수
    @Override
    public boolean doExercise(int currentStep){

        return true;
    }

    // getter setter
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }
}
