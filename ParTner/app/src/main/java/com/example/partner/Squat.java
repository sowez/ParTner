package com.example.partner;

import android.util.Log;


public class Squat {

    private float[][] point;
    private int exCount;

    public Squat(int exCount){
        this.exCount = exCount;
    }

    // 스쿼트 준비자세 체크하는 함수
    public boolean checkReady(){
        boolean isReady = true;

        // 1. 모든 x좌표가 0.35~0.65 사이에 있어야 함(화면의 가운데에 서야 함)
        for(int i=0;i<=13;i++){
            float tX = point[0][i];
            if (tX<36.0 || tX >76.0)
                isReady = false;
        }

        // 2. 머리의 좌표가 목의 좌표보다 왼쪽에 있어야 함(왼쪽을 보고 서야 함)  // 특이 케이스 수정 필요
        if (point[0][0] > point[0][1])
            isReady = false;
        Log.d("Exercise", "왼쪽을 보고 서세요");

        // 3.
        if (point[1][0]>50.0||point[1][1]>50.0||point[1][2]>50.0||point[1][5]>50.0){
            isReady = false;
            Log.d("Exercise", "핸드폰의 각도를 조절하세요");
        }

        return isReady;
    }


    // 스쿼트 운동 동작 인식하는 함수
    public void doSquat(){

    }


    // getter setter

    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }
}
