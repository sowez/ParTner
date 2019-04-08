package com.example.partner;

import android.util.Log;


public class Squat extends Exercise{

    private float[][] point;
//    private int exCount;

    public Squat(int exCount){
        super(exCount);
    }

    // 스쿼트 준비자세 체크하는 함수
    @Override
    public boolean checkReady(){
        boolean isReady = true;

        // 1. 모든 x좌표가 0.45~0.85 사이에 있어야 함
        for(int i=0;i<=13;i++){
            float tX = point[0][i];
            if (tX<45.0 || tX >85.0) {
                isReady = false;
                Log.d("Exercise", "그림에 맞춰 서세요");
            }
        }

//        // 2. 머리의 좌표가 목의 좌표보다 왼쪽에 있어야 함(왼쪽을 보고 서야 함)  // 특이 케이스(뚜딘!) 수정 필요
//        if (point[0][0] > point[0][1])
//            isReady = false;
//        Log.d("Exercise", "왼쪽을 보고 서세요");

        // 3. 머리, 목, 어깨가 위쪽에 있어야 함
        if (point[1][0]>40.0||point[1][1]>40.0||point[1][2]>40.0||point[1][5]>40.0){
            isReady = false;
            Log.d("Exercise", "핸드폰의 각도를 조절하세요");
        }

        // 4. 몸통의 좌표들이 기준좌표(목) 범위를 크게 벗어나지 않아야 함(일자로 서있어야 함)
        float neck = point[0][1];
        for (int j=2;j<=13;j++){
            if(Math.abs(neck-point[0][j])>10){
                isReady = false;
                Log.d("Exercise", "옆을 보고 서세요");
            }
        }

        return isReady;
    }


    // 스쿼트 운동 동작 인식하는 함수
    @Override
    public void doExercise(){

    }


//    // getter setter
//
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }
}
