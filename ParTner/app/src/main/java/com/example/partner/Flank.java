package com.example.partner;

import android.util.Log;

public class Flank extends Exercise{

    private float[][] point;

    public Flank(int exCount){
        super(exCount);
    }

    @Override
    public boolean checkReady(){
        boolean isReady = false;

        //head-neck-shoulder-hip은 180도도
        //hip-knee-ankle은 90도
        //shoulder-elbow-wrist 90도
        double hnsAngle = getAngle(1,0,1,2);
        double nshAngle = getAngle(2,1,2,8);
        double hkaAngle = getAngle(9,8,9,10);
        double sewAngle = getAngle(3,2,3,4);

        if(hnsAngle>=110 && hnsAngle <=180 &&
            nshAngle>=110 && nshAngle<= 180)
//            && hkaAngle>=40 && hkaAngle<=100 &&
//            sewAngle>=40 && hkaAngle<=100)
            isReady=true;
        Log.d("jumpingjack", hnsAngle+","+nshAngle+","+hkaAngle+","+sewAngle);

        return isReady;
    }

    // 플랭크 운동 동작 인식하는 함수
    @Override
    public boolean doExercise(int currentStep) {
        //head-neck-shoulder-hip-knee-ankle은 180
        //shoulder-elbow-wrist 90도
        double hnsAngle = getAngle(1, 0, 1, 2);
        double nshAngle = getAngle(2, 1, 2, 8);
        double shkAngle = getAngle(8, 2, 8, 9);
        double hkaAngle = getAngle(9, 8, 9, 10);
        double sewAngle = getAngle(3, 2, 3, 4);
        Log.d("jumpingjack", hnsAngle+","+nshAngle+","+shkAngle+","+hkaAngle+","+sewAngle);


        if (hnsAngle >= 110 && hnsAngle <= 180 &&
                nshAngle >= 110 && nshAngle <= 180 &&
                shkAngle >= 110 && shkAngle <= 180 &&
                hkaAngle >= 110 && hkaAngle <= 180)
//                && sewAngle >= 40 && hkaAngle <= 100)
            return true;
        return false;
    }


    // getter setter
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }

    @Override
    public int getSteps() {
        return 20;
    }
}