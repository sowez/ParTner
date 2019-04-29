package com.example.partner;

import android.graphics.PointF;

import java.util.ArrayList;

public class Exercise {
    private float[][] point;
    private int exCount;
    private ArrayList<PointF> dpPoint;

    public Exercise(int exCount){
        this.exCount = exCount;
    }

    // 운동 준비자세 체크하는 함수
    public boolean checkReady(){
        boolean isReady = true;
        return isReady;
    }


    //운동 동작 인식하는 함수
    public void doExercise(){

    }


    // getter setter
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }

    public void setDpPoint(ArrayList<PointF> dpPoint) { this.dpPoint = dpPoint; }
}
