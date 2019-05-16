package com.example.partner;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

public class Exercise {
    private float[][] point;
    private int exCount;
    public ArrayList<PointF> dpPoint;
    public Exercise(int exCount){
        this.exCount = exCount;
    }

    // 운동 준비자세 체크하는 함수
    public boolean checkReady(){
        boolean isReady = true;
        return isReady;
    }


    //운동 동작 인식하는 함수
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

    public double getAngle(int ff, int fs, int sf, int ss){
        double []v1 = {dpPoint.get(ff).x-dpPoint.get(fs).x,dpPoint.get(ff).y-dpPoint.get(fs).y};
        double []v2 = {dpPoint.get(sf).x-dpPoint.get(ss).x,dpPoint.get(sf).y-dpPoint.get(ss).y};

        Log.d("angle1", v1[0]+","+v1[1]+","+v2[0]+","+v2[1]);
        double angle = 0;
        angle = ((v1[0]*v2[0]+v1[1]*v2[1])/(Math.sqrt(v1[0]*v1[0] + v1[1]*v1[1])*Math.sqrt(v2[0]*v2[0] + v2[1]*v2[1])));
        Log.d("angle 각도", Math.acos(angle)*180/Math.PI+"");
        return Math.acos(angle)*180/Math.PI;
    }

    public int getSteps(){return 0;}
    public void setDpPoint(ArrayList<PointF> dpPoint) { this.dpPoint = dpPoint; }
}
