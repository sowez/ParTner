package com.example.partner;

import android.graphics.PointF;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Double.isNaN;
import static java.lang.Double.min;


public class Squat extends Exercise {

    private static final String TAG = "Squat";

    private float[][] point;
//    private int exCount;

    private ArrayList<PointF> dpPoint; // 화면 상 좌표
    private ArrayList<PointNAngle> pointHistory = new ArrayList<>(); // 이전 좌표 저장
    private double minAngle = 180.0;
    private int minIndex = -1;
    private boolean hasSat = false;

    Squat(int exCount) {
        super(exCount);
    }

    // 스쿼트 준비자세 체크하는 함수
    @Override
    public boolean checkReady() {
        boolean isReady = true;

        // 1. 모든 x좌표가 38.5~72.85 사이에 있어야 함(0<=x<=96)
        for(int i=0;i<=13;i++){
            float tX = point[0][i];
            if (tX<33.5 || tX >75.85) {
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
    public ArrayList<Integer> doExercise(int currentStep){

//        // 두 점의 지표면과 평행한 선분 생성
//        super.dpPoint.add(new PointF(0, 0));   // -> dpPoint(14)
//        super.dpPoint.add(new PointF(100, 0)); // -> dpPoint(15)
//
//        // 왼쪽 엉덩이-무릎-발목 사이의 각도 구하기
//        double legAngle = getAngle(9, 8, 9, 10);
//
//        // 왼쪽 손-팔꿈치-어깨 각도 구하기
//        double armAngle = getAngle(3, 2, 3, 4);
//
//        // 지표현 - (머리-목) 각도 구하기
//        double headAngle = getAngle(14, 15, 0,1);
//
//        // 지표면-왼쪽팔 각도(방향) 구하기
//        double armDirection = getAngle(14, 15, 2, 4);
//        Log.d(TAG, "왼쪽 다리: " + legAngle);
//        Log.d(TAG, "팔각도: " + armAngle);
//        Log.d(TAG, "팔방향: " + armDirection);

        int legState;
        int legAngleSlope = 3;
        ArrayList<Integer> res = new ArrayList<>();

        // 좌표 없음
        if (super.dpPoint.get(0).x == 0.0){
            res.add(-1);
            res.add(0);
            return res;
        }

        PointNAngle pointNAngle = new PointNAngle(super.dpPoint);

        // 만약 기타 오류 생기면 바로 return해줌
        res = checkBasicErr(pointNAngle);
        if (res.get(0) == -1){
            return res;
        }
        else
            res.clear();


        // 다리 상태 저장 - 서있으면 0, 그 외에는 1
        if (pointNAngle.legAngle >= 150) {
            legState = 0;
        } else {
            legState = 1;
        }

        // 직전 다리각도와의 차이로 slope 구하기
        int index = pointHistory.size()-1;
        if (index != -1){   // 이전에 저장된 좌표가 있을 때
            double angleDiff = pointNAngle.legAngle - pointHistory.get(index).legAngle;
            if (angleDiff<=10 && angleDiff >= -10)
                legAngleSlope = 0;
            else if (angleDiff>10 && angleDiff <60)
                legAngleSlope = 1;
            else if (angleDiff< -10 && angleDiff > -60)
                legAngleSlope = -1;
            else
                legAngleSlope = -2;
        }

        if (currentStep == 0){
            if (legState == 0){ // 0단계면서 제대로 서있을 때 -> 현재 좌표 저장하고 다음 스텝으로 넘어감
                Log.d(TAG, "doExercise: 스텝 1로 넘어감");
                pointHistory.add(pointNAngle);
                res.add(1);
            } else {            // 0단계인데 제대로 안 서있을 때 -> 똑바로 서세요
                res.add(-1);
                res.add(5); // 똑바로 서세요
            }
        } else if (currentStep == 1){
            if (legAngleSlope == -2){
                res.add(0); // 좌표 잘못 찍혀서 각도가 급변하는 경우는 무시
            } else {
                if (legState == 0){
                    if (legAngleSlope == 1 && hasSat){ // 운동 1번 끝 -> 다음 스텝(2)으로 넘어감
                        Log.d(TAG, "doExercise: 스텝 2로 넘어감, legAngle: "+pointNAngle.legAngle);
                        res.add(1);

                    } else{ // 운동 시작했는데 계속 서있음 -> 일단 에러처리 안하고 그냥 놔두자
                        Log.d(TAG, "doExercise: step 1, 안움직이는 중");
                        pointHistory.add(pointNAngle);
                        res.add(-1);
                        res.add(9);
                    }
                } else {    // 운동 시작했고 다리 굽혀진 상태 -> 좌표 계속 저장 & 최소 다리각도 업데이트
                    Log.d(TAG, "doExercise: step 1, 운동 중, legAngle: "+pointNAngle.legAngle);
                    hasSat = true;
                    pointHistory.add(pointNAngle);
                    if (pointNAngle.legAngle < minAngle && (legAngleSlope == 0 || legAngleSlope == -1)){
                        minAngle = pointNAngle.legAngle;
                        minIndex = pointHistory.size()-1;
                    }
                    res.add(0);
                }
            }
        } else if(currentStep == 2){
            // 운동 성공/실패 판단해주기 (다리 각도 -> 무릎 확인)
            Log.d(TAG, "doExercise: step 2, 운동 성공/실패 확인");
            PointNAngle squat = pointHistory.get(minIndex);
            if (squat.legAngle < 70){ // 너무 많이 앉음(err 6)
                Log.d(TAG, "doExercise: step 2, 너무 많이 앉음");
                res.add(-2);
                res.add(6);
            } else if (squat.legAngle > 110){ // 너무 안앉음(err 7)
                Log.d(TAG, "doExercise: step 2, 너무 안앉음");
                res.add(-2);
                res.add(7);
            } else {
                if (squat.dpPoint.get(9).x-squat.dpPoint.get(10).x > 30){
                    // 무릎 너무 많이 나옴(err 8)
                    Log.d(TAG, "doExercise: step 2, 무릎 넣어야댐");
                    res.add(-2);
                    res.add(8);
                } else {
                    // 운동 성공
                    Log.d(TAG, "doExercise: step 3로 넘어감, 운동 성공");
                    res.add(1);
                }
            }
            // 초기화
            pointHistory.clear();
            hasSat = false;
            minAngle = 180.0;
            minIndex = -1;
        }

        Log.d(TAG, "doExercise: -----------------------------");
        return res;
    }

    private ArrayList<Integer> checkBasicErr(PointNAngle angle){
        ArrayList<Integer> result = new ArrayList<>();
        int headState;
        boolean isArmStraight;
        boolean isArmParallel;

        // 팔 상태 저장
        if (angle.armAngle>150||angle.armAngle<30)
            isArmStraight = true;
        else
            isArmStraight = false;

        if (angle.armDirection>150||angle.armDirection<30)
            isArmParallel = true;
        else
            isArmParallel = false;

        // 머리 상태 저장
        if (angle.headAngle < 60)
            headState = -1;
        else if(angle.headAngle > 100)
            headState = 0;
        else
            headState = 1;
        
        if (!isArmParallel){
            Log.d(TAG, "E/팔을 앞으로 뻗으세요");
            result.add(-1);
            result.add(1);
            return result;
        }
        if (!isArmStraight){
            Log.d(TAG, "E/팔을 구부리지 마세요");
            result.add(-1);
            result.add(2);
            return result;
        }

        if (headState == -1){
            Log.d(TAG, "E/고개를 숙이지 마세요");
            result.add(-1);
            result.add(3);
            return result;
        }
        else if (headState == 0){
            Log.d(TAG, "E/정면을 바라보세요");
            result.add(-1);
            result.add(4);
            return result;
        }

        // 아무 오류도 안걸림
        result.add(1);
        return result;

//        switch (currentStep){
//            case 0:
//                if (legState==0){
//                    Log.d(TAG, "goto Step 1");
//                    result.add(1);
//                }
//                break;
//            case 1:
//                if (legState==1){
//                    Log.d(TAG, "goto Step 2");
//                    result.add(1);
//                }
//                break;
//            case 2:
//                if (legState==2){
//                    Log.d(TAG, "goto Step 3");
//                    result.add(1);
//                }
//                break;
//            case 3:
//                if (legState==1){
//                    Log.d(TAG, "goto Step 4");
//                    result.add(1);
//                }
//                break;
//            case 4:
//                if (legState==0){
//                    Log.d(TAG, "goto Step 5 and restart");
//                    result.add(1);
//                }
//                break;
//        }
//        if (result.size() == 0){
//            result.add(-1);
//            result.add(4);
//        }

//        Log.d(TAG, "E/cannot go to next step"+result.get(0));
//        return result;
    }

    private class PointNAngle {
        ArrayList<PointF> dpPoint = new ArrayList<>();
        double legAngle, armAngle, headAngle, armDirection;

        PointNAngle(ArrayList<PointF> dpPoint){
            this.dpPoint = dpPoint;
            this.dpPoint.add(new PointF(0, 0));   // -> dpPoint(14)
            this.dpPoint.add(new PointF(100, 0)); // -> dpPoint(15)

            setDpPoint(this.dpPoint);
            // 왼쪽 엉덩이-무릎-발목 사이의 각도 구하기
            this.legAngle = getAngle(9, 8, 9, 10);

            // 왼쪽 손-팔꿈치-어깨 각도 구하기
            this.armAngle = getAngle(3, 2, 3, 4);

            // 지표면 - (머리-목) 각도 구하기
            this.headAngle = getAngle(14, 15, 0,1);

            // 지표면-왼쪽팔 각도(방향) 구하기
            this.armDirection = getAngle(14, 15, 2, 4);
        }
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
        return 3;
    }
}
