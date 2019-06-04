package com.example.partner;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Double.isNaN;

public class Flank extends Exercise{

    private float[][] point;

    public Flank(int exCount){ super(exCount); }

    @Override
    public boolean checkReady(){
        boolean isReady = false;

        Flank.PointNAngle pointNAngle = new Flank.PointNAngle(super.dpPoint);
        if(isNaN(pointNAngle.headAngle)|| isNaN(pointNAngle.hipAngle) || isNaN(pointNAngle.legAngle) || isNaN(pointNAngle.armAngle)){
            return false;
        }

        //head-neck-shoulder-hip은 180도도
        //hip-knee-ankle은 90도
        //shoulder-elbow-wrist 90도
//        double hnsAngle = getAngle(1,0,1,2);
//        double nshAngle = getAngle(2,1,2,8);
//        double hkaAngle = getAngle(9,8,9,10);
//        double sewAngle = getAngle(3,2,3,4);

//        if(hnsAngle>=110 && hnsAngle <=180 &&
//            nshAngle>=110 && nshAngle<= 180
//            && hkaAngle>=40 && hkaAngle<=110 &&
//            sewAngle>=40 && hkaAngle<=110)
//            isReady=true;

        if(pointNAngle.headAngle>=70 && pointNAngle.headAngle<=110
                && pointNAngle.legAngle>=60 && pointNAngle.legAngle<=120
                && pointNAngle.armAngle>=60 && pointNAngle.armAngle<=120)
           return true;
        Log.d("flank", pointNAngle.headAngle+","+pointNAngle.legAngle+","+pointNAngle.armAngle);

        return isReady;
    }

    // 플랭크 운동 동작 인식하는 함수
    @Override
    public ArrayList<Integer> doExercise(int currentStep) {
        //head-neck-shoulder-hip-knee-ankle은 180
        //shoulder-elbow-wrist 90도

        ArrayList<Integer> res = new ArrayList<>();

        Flank.PointNAngle pointNAngle = new Flank.PointNAngle(super.dpPoint);
        Log.d("flank", pointNAngle.headAngle+","+pointNAngle.hipAngle+","+pointNAngle.legAngle+","+pointNAngle.armAngle);

        if(isNaN(pointNAngle.headAngle)|| isNaN(pointNAngle.hipAngle) || isNaN(pointNAngle.legAngle) || isNaN(pointNAngle.armAngle)){
            res.add(-1);
            res.add(0);
            return res;
        }

        if(pointNAngle.headAngle<=75){
            res.add(-1);
            res.add(1);
            return res;
        }
        else if(pointNAngle.headAngle>=120){
            res.add(-1);
            res.add(2);
            return res;
        }

        if(pointNAngle.hipAngle<=75) {
            res.add(-1);
            res.add(3);
            return res;
        }
        else if(pointNAngle.hipAngle>=105){
            res.add(-1);
            res.add(4);
            return res;
        }

        if(pointNAngle.legAngle<=150){
            res.add(-1);
            res.add(5);
            return res;
        }

        if(pointNAngle.armAngle<=50 || pointNAngle.armAngle >= 140) {
            res.add(-1);
            res.add(6);
            return res;
        }

        res.add(1);
        return res;
    }

    private class PointNAngle {
        ArrayList<PointF> dpPoint = new ArrayList<>();
        double headAngle, hipAngle, legAngle, armAngle;

        PointNAngle(ArrayList<PointF> dpPoint){
            this.dpPoint = dpPoint;
            this.dpPoint.add(new PointF(0, 0));   // -> dpPoint(14)
            this.dpPoint.add(new PointF(10, 0)); // -> dpPoint(15)
            this.dpPoint.add(new PointF(0,10));// -> dpPoint(16)
            setDpPoint(this.dpPoint);

            this.headAngle = getAngle(0,1,14,15);
            this.hipAngle = getAngle(1,8,14,15);
            this.legAngle = getAngle(9,8,9,10);
            this.armAngle = getAngle(2,3,14,16);
        }
    }

    @Override
    public int getSteps() {
        return 1000;
    }
    public float[][] getPoint() {
        return point;
    }
    public void setPoint(float[][] point) {
        this.point = point;
    }

}
