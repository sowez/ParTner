package com.example.partner;

import android.graphics.PointF;
import android.util.Log;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;
import static java.lang.Double.min;

public class JumpingJack extends Exercise {
    private float[][] point;
    private ArrayList<PointNAngle> pointHistory = new ArrayList<>(); // 이전 좌표 저장
    private double maxLegAngle = 0.0;
    private int Index = -1;
    private double minArmAngle = 180.0;
    private boolean hasSat = false;

    public JumpingJack(int exCount) {
        super(exCount);
    }

    // 스쿼트 준비자세 체크하는 함수
    @Override
    public boolean checkReady() {
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
        double r_hnaAngle = getAngle(9,8,9,10);
        double r_sewAngle = getAngle(3,2,3,4);
        double r_legAngle = getAngle(1,0,8,9);
        double r_armAngle = getAngle(1,0,2,3);
        double l_hnaAngle = getAngle(12,11,12,13);
        double l_sewAngle = getAngle(6,5,6,7);
        double l_legAngle = getAngle(1,0,2,3);
        double l_armAngle = getAngle(1,0,5,6);
        if(r_hnaAngle>=150 && r_hnaAngle<=180 &&
                l_hnaAngle>=150 && l_hnaAngle<=180 &&
                r_sewAngle>=100 && r_sewAngle<=180 &&
                l_sewAngle>=100 && l_sewAngle<=180){
            if(r_legAngle>=120 && r_legAngle<=180 &&
                    l_legAngle>= 120 && l_legAngle<=180 &&
                    r_armAngle>=130 && r_armAngle<=180 &&
                    l_armAngle>=130 && l_armAngle<=180) {
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
        int legState;
        int legAngleSlope = 3;
        int armState;
        int armAngleSlope = 3;
        int headState = 0;//0:아래, 1: 위

        PointNAngle pointNAngle = new PointNAngle(super.dpPoint);
        Log.d("jumpingJack", "l_arm: "+pointNAngle.l_armAngle + "l_leg: "+pointNAngle.l_legAngle + "l_hna: "+pointNAngle.l_hnaAngle + "l_sew: "+pointNAngle.l_sewAngle);

        Log.d("jumpingJack", "r_arm: "+pointNAngle.r_armAngle + "r_leg: "+pointNAngle.r_legAngle + "r_hna: "+pointNAngle.r_hnaAngle + "r_sew: "+pointNAngle.r_sewAngle);
        Log.d("jumpingJack", "legAngle: "+pointNAngle.legAngle);
        Log.d("jumpingJack", "l_armAngle: "+pointNAngle.l_armAngle+"r_armAngle: "+pointNAngle.r_armAngle);
//        Log.d(TAG, "head"+pointNAngle.)

        res = checkBasicErr(pointNAngle);
        if (res.get(0) == -1) {
            return res;
        } else res.clear();

        // 다리 상태 저장 - 서있으면 0, 그 외에는 1
        if (pointNAngle.legAngle <= 30) {
            legState = 0;
        } else {
            legState = 1;
        }
        // 팔 상태 저장 - 서있으면 0, 그 외에는 1
        if (pointNAngle.r_armAngle >= 130 && pointNAngle.l_armAngle >= 130) {
            armState = 0;
        } else {
            armState = 1;
        }

        // 직전 다리각도와의 차이로 slope 구하기
        int index = pointHistory.size() - 1;
        if (index != -1) {   // 이전에 저장된 좌표가 있을 때
            double legAngleDiff = pointNAngle.legAngle - pointHistory.get(index).legAngle;
            double armLAngleDiff = -(pointNAngle.l_armAngle - pointHistory.get(index).l_armAngle);
            double armRAngleDiff = -(pointNAngle.r_armAngle - pointHistory.get(index).r_armAngle);
            double headDiff = pointNAngle.headHeight - pointHistory.get(index).headHeight;
            if (legAngleDiff <= 10 && legAngleDiff >= 10)
                legAngleSlope = 0;
            else if (legAngleDiff > 10 && legAngleDiff < 80)
                legAngleSlope = 1;
            else if (legAngleDiff < -10 && legAngleDiff > -80)
                legAngleSlope = -1;
            else
                legAngleSlope = -2;

            if ((armLAngleDiff <= 10 || armLAngleDiff >= -10) && (armRAngleDiff <= 10 || armRAngleDiff >= -10))
                armAngleSlope = 0;
            else if ((armLAngleDiff > 10 || armLAngleDiff < 100) && (armRAngleDiff > 10 || armRAngleDiff < 100))
                armAngleSlope = 1;
            else if ((armLAngleDiff < -10 || armLAngleDiff > -100) && (armRAngleDiff < -10 || armRAngleDiff > -100))
                armAngleSlope = -1;
            else
                armAngleSlope = -2;

            if (headDiff > 5) {
                headState = 1;
            } else if (headDiff < -5) {
                headState = 0;
            }
        }


        Log.d(TAG, "armState : "+armState+ "armAngleSlope : "+ armAngleSlope+"legSate : "+legState+"legAngleSlope : "+legAngleSlope);
        Log.d(TAG, "step"+ currentStep);
//        Log.d(TAG, )
        //step = 0: 운동 시작전 정자세
        //step = 1: 운동 중
        //step = 2: 운동 끝
        if (currentStep == 0) {
            Log.d(TAG, "운동 시작");
            if (legState == 0 && armState == 0) {
                Log.d(TAG, "정자세");
                pointHistory.add(pointNAngle);
                res.add(1);
            } else {
                if(legState==0) {
                    Log.d(TAG, "팔이 정자세가 아님");
                    res.add(-1);
                    res.add(4);
                }
                else{
                    Log.d(TAG, "다리가 정자세가 아님");
                    res.add(-1);
                    res.add(5);
                }
            }
        }
        else if (currentStep == 1) {
            if (legAngleSlope == -2 || armAngleSlope == -2) {//좌표 잘못 찍혔을 때
                Log.d(TAG, "자세 잘못짝힘");
                res.add(0);
            }
            else {
                if (legState == 0 && armState == 0) {
                    Log.d(TAG, "운동 중 정자세일 때");
                    if (legAngleSlope == 1 && armAngleSlope == 1 && hasSat) {//운동 끝나서 정자세인경우
                        Log.d(TAG, "운동이 끝남");
                        res.add(1);
                    } else {//운동 시작했는데 계속 서있는 경우
                        Log.d(TAG, "운동 시작을 안함");
                        res.add(-1);
                        res.add(9);
                    }
                }
                //정자세 아니면서 (운동 중이면서)
                else if (legState == 0) {
                    if (hasSat) {//운동이 끝났으면서 팔이 정자세가 아님
                        Log.d(TAG, "운동이 끝났으면서 팔이 정자세가 아님");
                        res.add(-1);
                        res.add(4);
                    } else {//운동 안끝났으면서 다리가 정자세

                        Log.d(TAG, "운동 안끝났으면서 다리가 정자세");
                        res.add(-1);
                        res.add(6);
                    }
                } else if (armState == 0) {
                    if (hasSat) {//운동 끝났으면서 다리가 정자세가 아님
                        Log.d(TAG, "운동 끝났으면서 다리가 정자세가 아님");
                        res.add(-1);
                        res.add(5);
                    } else {//운동 안끝났으면서 팔이 정자세
                        Log.d(TAG, "운동 안끝났으면서 팔이 정자세");
                        res.add(-1);
                        res.add(3);
                    }
                } else {//운동 중일 때
                    Log.d(TAG, "운동중");
                    hasSat = true;
                    pointHistory.add(pointNAngle);
                    if (pointNAngle.legAngle > maxLegAngle && (legAngleSlope == 0 || legAngleSlope == -1) && (pointNAngle.l_armAngle < minArmAngle && pointNAngle.r_armAngle < minArmAngle) && (armAngleSlope == 0 || armAngleSlope == -1)) {
                        Log.d(TAG, "운동 안끝났으면서 다리가 정자세");

                        maxLegAngle = pointNAngle.legAngle;
                        Index = pointHistory.size() - 1;
                        minArmAngle = (pointNAngle.r_armAngle > pointNAngle.l_legAngle) ? pointNAngle.r_armAngle : pointNAngle.l_armAngle;
                    }
                    res.add(0);
                }
            }
        }
        else if (currentStep == 2) {
            Log.d(TAG, "운동 체크");
            PointNAngle jj = pointHistory.get(Index);
            if (jj.legAngle < 15) {
                Log.d(TAG, "다리가 더 벌어져야함");
                res.add(-2);
                res.add(6);
            } else if (pointNAngle.legAngle > 50) {
                Log.d(TAG, "다리가 모아져야함");
                res.add(-2);
                res.add(5);
            } else if (jj.r_armAngle > 50 || jj.l_armAngle > 50) {
                Log.d(TAG, "팔이 더 올라가야");
                res.add(-2);
                res.add(3);
            } else {//운동 성공함
                Log.d(TAG, "운동성");
                res.add(1);
            }

            pointHistory.clear();
            hasSat = false;
            maxLegAngle=0.0;
            minArmAngle=180.0;
            Index = -1;
        }

        return res;
    }

    private ArrayList<Integer> checkBasicErr(JumpingJack.PointNAngle angle){
        ArrayList<Integer> result = new ArrayList<>();
        int headState;
        boolean isArmStraight;
        boolean isLegStraight;

        // 팔 상태 저장

        if(isNaN(angle.r_hnaAngle)|| isNaN(angle.r_sewAngle) || isNaN(angle.r_armAngle) || isNaN(angle.l_hnaAngle) || isNaN(angle.l_sewAngle) || isNaN(angle.l_armAngle) ){
            result.add(-1);
            result.add(0);
            Log.d("error", "0");
            return result;
        }
        else if(angle.r_hnaAngle<160 || angle.l_hnaAngle<160){
            result.add(-1);
            result.add(1);
            Log.d("error", "1");
        }
        else if(angle.r_sewAngle<130 || angle.l_sewAngle < 130){
            result.add(-1);
            result.add(2);
            Log.d("error", "2");
        }


        // 아무 오류도 안걸림
        result.add(1);
        return result;
    }

    private class PointNAngle {
        ArrayList<PointF> dpPoint = new ArrayList<>();
        double r_hnaAngle, r_sewAngle, r_legAngle, r_armAngle, l_hnaAngle, l_sewAngle, l_legAngle, l_armAngle, legAngle, headHeight;


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
        }
    }

    // getter setter
    public float[][] getPoint() {
        return point;
    }

    public int getSteps() { return 3; }
    public void setPoint(float[][] point) {
        this.point = point;
    }


}
