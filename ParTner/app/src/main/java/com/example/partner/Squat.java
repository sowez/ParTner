package com.example.partner;

import android.graphics.PointF;
import android.util.Log;

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

        // 1. 모든 x좌표가 38.5~72.85 사이에 있어야 함(0<=x<=96)
        for(int i=0;i<=13;i++){
            float tX = point[0][i];
            if (tX<38.5 || tX >72.85) {
                isReady = false;
                Log.d("Exercise", "그림에 맞춰 서세요");
            }
        }

//        // 2. 머리의 좌표가 목의 좌표보다 왼쪽에 있어야 함(왼쪽을 보고 서야 함)  // 특이 케이스(뚜딘!) 수정 필요
//        if (point[0][0] > point[0][1])
//            isReady = false;
//        Log.d("Exercise", "왼쪽을 보고 서세요");

        // 3. 머리, 목, 어깨가 위쪽에 있어야 함
        if (point[1][0]>34.3||point[1][1]>34.3||point[1][2]>34.3||point[1][5]>34.3){
            isReady = false;
            Log.d("Exercise", "핸드폰의 각도를 조절하세요");
        }

        // 4. 몸통의 좌표들이 기준좌표(목) 범위를 크게 벗어나지 않아야 함(일자로 서있어야 함)
        // 목 기준 +-15
        float neck = point[0][1];
        for (int j=2;j<=13;j++){
            if(Math.abs(neck-point[0][j])>13){
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
        double legAngle = getAngle(9, 8, 9, 10);
        double armAngle = getAngle(3, 2, 3, 4);

        // 두 점으 지표면과 평행한 선분 생성
        super.dpPoint.add(new PointF(0, 0));   // dpPoint(14)
        super.dpPoint.add(new PointF(100, 0)); // dpPoint(15)로

        // 지표면-왼쪽팔 각도(방향) 구하기
        double armDirection = getAngle(14, 15, 2, 4);

        Log.d("각도", "왼쪽 다리: " + legAngle);
        Log.d("각도", "팔각도: " + armAngle);
        Log.d("각도", "팔방향: " + armDirection);

        int legState;
        boolean isArmStraight;
        boolean isArmParallel;

        // 다리 상태 저장
        if (legAngle >= 155) {
            legState = 0;
        } else if (legAngle >= 100) {
            legState = 1;
        } else if (legAngle >= 70) {
            legState = 2;
        } else {
            legState = -1;
        }

        // 팔 상태 저장
        if (armAngle>150||armAngle<30)
            isArmStraight = true;
        else
            isArmStraight = false;

        if (armDirection>150||armDirection<30)
            isArmParallel = true;
        else
            isArmParallel = false;

        return gotoNextStep(currentStep,legState, isArmStraight, isArmParallel);
    }

    private boolean gotoNextStep(int currentStep, int legState, boolean isArmStraight, boolean isArmParallel){
        Log.d("gotoNextStep", "currentStep: "+currentStep+", "+legState+", "+isArmStraight+", "+isArmParallel);
        switch (currentStep){
            case 0:
                if (legState==0&&isArmStraight&&isArmParallel){
                    Log.d("gotoNextStep", "goto Step 1");
                    return true;
                }
                break;

            case 1:
                if (legState==1&&isArmStraight&&isArmParallel){
                    Log.d("gotoNextStep", "goto Step 2");
                    return true;
                }
                break;
            case 2:
                if (legState==2&&isArmStraight&&isArmParallel){
                    Log.d("gotoNextStep", "goto Step 3");
                    return true;
                }
                break;
            case 3:
                if (legState==1&&isArmStraight&&isArmParallel){
                    Log.d("gotoNextStep", "goto Step 4");
                    return true;
                }
                break;
            case 4:
                if (legState==0&&isArmStraight&&isArmParallel){
                    Log.d("gotoNextStep", "goto Step 5 and restart");
                    return true;
                }
                break;
            default:
                break;
        }
        Log.d("gotoNextStep", "cannot go to next step");
        return false;
    }



//    // getter setter

    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }

    @Override
    public int getSteps() {
        return 5;
    }

//    public void setDpPoint(ArrayList<PointF> dpPoint) { this.dpPoint = dpPoint; }
}
