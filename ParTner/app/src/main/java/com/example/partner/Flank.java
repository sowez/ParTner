package com.example.partner;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Double.isNaN;

public class Flank extends Exercise{

    private float[][] point;

    private double flankDifficulty = 0.0;

    public Flank(int exCount, int difficulty){
        super(exCount);
        switch (difficulty){
            case 0: // super easy
                flankDifficulty = 0.2;
                break;
            case 1: // easy
                flankDifficulty = 0.1;
                break;
            case 2: // default
                flankDifficulty = 0.0;
                break;
            case 3: // hard
                flankDifficulty = -0.1;
                break;
        }
    }

    @Override
    public boolean checkReady(){
        boolean isReady = false;

        Flank.PointNAngle pointNAngle = new Flank.PointNAngle(super.dpPoint);
        if(isNaN(pointNAngle.headAngle)|| isNaN(pointNAngle.hipAngle) || isNaN(pointNAngle.legAngle) || isNaN(pointNAngle.armAngle)){
            return false;
        }

        if(pointNAngle.headAngle>=70*(1-flankDifficulty) && pointNAngle.headAngle<=110*(1+flankDifficulty)
                && pointNAngle.legAngle>=60*(1-flankDifficulty) && pointNAngle.legAngle<=120*(1+flankDifficulty)
                && pointNAngle.armAngle>=60*(1-flankDifficulty) && pointNAngle.armAngle<=120*(1+flankDifficulty))
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

        if(pointNAngle.headAngle<=80*(1-flankDifficulty)){
            res.add(-1);
            res.add(1);
            return res;
        }
        else if(pointNAngle.headAngle>=105*(1+flankDifficulty)){
            res.add(-1);
            res.add(2);
            return res;
        }

        if(pointNAngle.hipAngle<=80*(1-flankDifficulty)) {
            res.add(-1);
            res.add(3);
            return res;
        }
        else if(pointNAngle.hipAngle>=105*(1+flankDifficulty)){
            res.add(-1);
            res.add(4);
            return res;
        }

        if(pointNAngle.legAngle<=160*(1-flankDifficulty)){
            res.add(-1);
            res.add(5);
            return res;
        }

        if(pointNAngle.armAngle<=60*(1-flankDifficulty) || pointNAngle.armAngle >= 120*(1+flankDifficulty)) {
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
