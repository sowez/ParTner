package com.example.partner;

import android.graphics.PointF;
import android.util.Log;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

public class JumpingJack extends Exercise {
    private float[][] point;
    private float headHistory = 0; // 이전 좌표 저장
    private double maxLegAngle = 0.0;
    private int Index = -1;
    private double minArmAngle = 180.0;
    private boolean hasSat = false;
    private double jJDifficulty = 0;

    public JumpingJack(int exCount, int difficulty) {
        super(exCount);
        switch (difficulty){
            case 0: // super easy
                jJDifficulty = 0.2;
                break;
            case 1: // easy
                jJDifficulty = 0.1;
                break;
            case 2: // default
                jJDifficulty = 0;
                break;
            case 3: // hard
                jJDifficulty = -0.1;
                break;
        }
    }

    // 스쿼트 준비자세 체크하는 함수
    @Override
    public boolean checkReady() {
        PointNAngle pointNAngle = new PointNAngle(super.dpPoint);
       //머리 위치 x 위치가 38.5~58.2, y 위치는 5.1~17.1
       if(point[0][0]<38 || point[0][0]>58 || point[1][0]<5 || point[1][0]>17) return false;

       //오른발 위치가 34.2~48.8, 71.1~88.2
       //왼발 위치가 41.1~58.2, 71.1~88.2
        if(point[0][10]<34 || point[0][10]>49 || point[1][10]<71 || point[1][10]>88) return false;
        if(point[0][13]<41 || point[0][13]>58 || point[1][13]<71 || point[1][13]>88) return false;

        //hip-knee-ankle 150~180도
        //shoulder-elbow-wrist 100~180도
        //다리 각도  120~180도
        //팔 각도 130~180도
        if(pointNAngle.r_hnaAngle>=150 && pointNAngle.r_hnaAngle<=180 &&
                pointNAngle.l_hnaAngle>=150 && pointNAngle.l_hnaAngle<=180 &&
                pointNAngle.r_sewAngle>=100 && pointNAngle.r_sewAngle<=180 &&
                pointNAngle.l_sewAngle>=100 && pointNAngle.l_sewAngle<=180){
            if(pointNAngle.r_legAngle>=120 && pointNAngle.r_legAngle<=180 &&
                    pointNAngle.l_legAngle>= 120 && pointNAngle.l_legAngle<=180 &&
                    pointNAngle.r_armAngle>=130 && pointNAngle.r_armAngle<=180 &&
                    pointNAngle.l_armAngle>=130 && pointNAngle.l_armAngle<=180) {
                return true;
            }
        }

        return false;
    }


    // 점핑잭 운동 동작 인식하는 함수
    @Override
    public ArrayList<Integer> doExercise(int currentStep) {
        Log.d("step", currentStep + "");

        //서있기-뛰기-팔벌리기-뛰기-제자
        //hip-knee-ankle 160~180도
        //shoulder-elbow-wrist 130~180도
        //다리 각도  150~180도 || 140~165도
        //팔 각도 150~180도 || 0~50도
        //다리
        ArrayList<Integer> res = new ArrayList<>();

        PointNAngle pointNAngle = new PointNAngle(super.dpPoint);
        res = checkBasicErr(pointNAngle);
        if (res.get(0) == -1) {
            return res;
        }else res.clear();

        //step = 0: 운동 시작전 정자세
        //step = 1: 뛰기
        //step = 2: 팔 올린 상태
        //step = 3: 뛰기
        //step = 4: 다시 정자
        if (currentStep == 1) {
            Log.d(TAG, "운동 시작 또는 ");
            if(pointNAngle.legAngle>35*(1+jJDifficulty)) {
                res.add(-1);
                res.add(5);
                Log.d("error", "5");
            }
            else if(pointNAngle.r_armAngle<140*(1-jJDifficulty) || pointNAngle.l_armAngle<140*(1+jJDifficulty)){
                res.add(-1);
                res.add(4);

                Log.d("error", "3");
            }

            this.headHistory = this.dpPoint.get(0).y;
        }
        else if (currentStep == 0 ) {
            Log.d(TAG, "점프 체크");
            Log.d(TAG, pointNAngle.currentHeadHeight+", "+headHistory);
            if(pointNAngle.currentHeadHeight<(headHistory+10)){
                res.add(-1);
                res.add(7);
            }
            if(pointNAngle.legAngle<25*(1-jJDifficulty)) {
                res.add(-1);
                res.add(6);
                Log.d("error", "6");
            }
//            else if(pointNAngle.legAngle>80){
//                res.add(-1);
//                res.add(5);
//
//                Log.d("error", "5");
//            }
            else if(pointNAngle.r_armAngle>90*(1+jJDifficulty) || pointNAngle.l_armAngle>90*(1+jJDifficulty)){
                res.add(-1);
                res.add(3);
                Log.d("error", "4");
            }

        }
        if(res.size()==0)res.add(1);

        return res;
    }

    private ArrayList<Integer> checkBasicErr(JumpingJack.PointNAngle angle){
        ArrayList<Integer> res = new ArrayList<>();
        int headState;
        // 팔 상태 저장

        if(isNaN(angle.r_hnaAngle)|| isNaN(angle.r_sewAngle) || isNaN(angle.r_armAngle) || isNaN(angle.l_hnaAngle) || isNaN(angle.l_sewAngle) || isNaN(angle.l_armAngle) ){
            res.add(-1);
            res.add(0);
            Log.d("error", "0");
            return res;
        }
        else if(angle.r_hnaAngle<160*(1-jJDifficulty) || angle.l_hnaAngle<160*(1-jJDifficulty)){
            res.add(-1);
            res.add(1);
            Log.d("error", "2");
        }
//        else if(angle.r_sewAngle<120*(1-jJDifficulty) || angle.l_sewAngle < 120*(1-jJDifficulty)){
//            res.add(-1);
//            res.add(2);
//            Log.d("error", "1");
//        }


        // 아무 오류도 안걸림
        res.add(1);
        return res;
    }

    private class PointNAngle {
        ArrayList<PointF> dpPoint = new ArrayList<>();
        double r_hnaAngle, r_sewAngle, r_legAngle, r_armAngle, l_hnaAngle, l_sewAngle, l_legAngle, l_armAngle, legAngle, headHeight;
        float currentHeadHeight;

        PointNAngle(ArrayList<PointF> dpPoint){
            this.dpPoint = dpPoint;
            this.dpPoint.add(new PointF((this.dpPoint.get(8).x+this.dpPoint.get(11).x)/2, (this.dpPoint.get(8).y+this.dpPoint.get(11).y)/2));   // dpPoint(14)
            setDpPoint(this.dpPoint);
            this.headHeight = this.dpPoint.get(0).y;
            // hip - knee - ankle 각도
            this.r_hnaAngle = getAngle(9,8,9,10);
            this.l_hnaAngle = getAngle(12,11,12,13);

            // shoulder - elbow - wrist 각도
            this.r_sewAngle = getAngle(3,2,3,4);
            this.l_sewAngle = getAngle(6,5,6,7);

            //다리 각도
            this.r_legAngle = getAngle(1,0,8,9);
            this.l_legAngle = getAngle(1,0,11,12);

            //팔 각도
            this.r_armAngle = getAngle(1,0,2,3);
            this.l_armAngle = getAngle(1,0,5,6);
            //r_ankle-골반-l_ankle 각도
            this.legAngle = getAngle(14,9,14,12);
            Log.d(TAG, ""+this.legAngle);
            this.currentHeadHeight = this.dpPoint.get(0).y;
        }
    }

    @Override
    public int getSteps() { return 2; }
    public float[][] getPoint() {
        return point;
    }
    public void setPoint(float[][] point) {
        this.point = point;
    }


}
