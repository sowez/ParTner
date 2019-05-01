package com.example.partner;

import android.graphics.PointF;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;


public class Squat extends Exercise{

    private float[][] point;
//    private int exCount;

    private ArrayList<PointF> dpPoint; // 화면 상 좌표

    private int poseState = 0;


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
        // 목 기준 +-15
        float neck = point[0][1];
        for (int j=2;j<=13;j++){
            if(Math.abs(neck-point[0][j])>15){
                isReady = false;
                Log.d("Exercise", "옆을 보고 서세요");
            }
        }

        return isReady;
    }


    // 스쿼트 운동 동작 인식하는 함수
    @Override
    public boolean doExercise(int currentStep){

        // 왼쪽 엉덩이-무릎-발목 사이의 각도 구하기
        double angle = getAngle(8,9,9,10);
        Log.d("각도", "왼쪽: "+angle);

        if (angle>=160){
            poseState = 0;
        } else if (angle >= 120){
            poseState = 1;
        } else if (angle >= 100){
            poseState = 2;
        } else if (angle >= 75){
            poseState = 3;
        } else {
            poseState = -1;
        }

        Log.d("각도 및 상태", "state: "+poseState);


        return false;
    }

    // 두 직선 사이의 각도
    // 출처 : https://gogorchg.tistory.com/entry/Android-두-선-사이의-각도-구하는-공식 [항상 초심으로]
//    public double getAngle(PointF start, PointF end, PointF cross) {
//       float [] line1 = {
//               cross.x, cross.y, 0,
//               start.x, start.y, 0
//       };
//        float [] line2 = {
//                cross.x, cross.y, 0,
//                end.x, end.y, 0
//        };
//
//        ByteBuffer buf1 = ByteBuffer.allocateDirect( line1.length * 4);
//        buf1.order(ByteOrder.nativeOrder());
//        FloatBuffer fbuf1 = buf1.asFloatBuffer();
//        fbuf1.clear();
//        fbuf1.put(line1);
//        fbuf1.position(0);
//
//        ByteBuffer buf2 = ByteBuffer.allocateDirect( line2.length * 4);
//        buf2.order(ByteOrder.nativeOrder());
//        FloatBuffer fbuf2 = buf2.asFloatBuffer();
//        fbuf2.clear();
//        fbuf2.put(line2);
//        fbuf2.position(0);
//
//        return VertexMath.getAngle(fbuf1,fbuf2);
//
//    }


//    // getter setter
//
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }

    public void setDpPoint(ArrayList<PointF> dpPoint) { this.dpPoint = dpPoint; }
}
