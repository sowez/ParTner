package com.example.partner;

import android.util.Log;

public class JumpingJack extends Exercise {
    AngleResource angleResource = new AngleResource();
    private float[][] point;
//    private int exCount;
//    private ArrayList<PointF> dpPoint;


    public JumpingJack(int exCount) {
        super(exCount);
    }

    // 스쿼트 준비자세 체크하는 함수
    @Override
    public boolean checkReady() {
//        float jj_start_range[][][] = angleResource.getJjStartRange();
//        boolean isReady = true;
//        for (int i = 0; i <= 13; i++) {
//            for (int j = 0; j <= 1; j++) {
//                if (jj_start_range[i][j][0]*96/112 <= point[j][i] && point[j][i] <= jj_start_range[i][j][1]*96/112) {
//                    continue;
//                } else {
//                    Log.e("point error", i + "," + j + "좌표: " + jj_start_range[i][j][0] + "<=" + point[j][i] + "<=" + jj_start_range[i][j][1]);
//                    isReady = false;
//                    break;
//                }
//            }
//            if (isReady == false) break;
//        }
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
    public boolean doExercise(int currentStep) {
        Log.d("step", currentStep + "");

//        int angle[][][] = angleResource.getJjStepAngle();
//        int angleRange[][][] = angleResource.getJjStepAngleRange();
//        for (int i = 0; i < 8; i++) {
//            Log.d("angle here", angle[currentStep][i][0] + "," + angle[currentStep][i][1] + "," + angle[currentStep][i][2] + "," + angle[currentStep][i][3] + "," + angleRange[currentStep][i][0] + "," + angleRange[currentStep][i][1]);
//            double currentAngle = getAngle(angle[currentStep][i][0], angle[currentStep][i][1], angle[currentStep][i][2], angle[currentStep][i][3]);
//            if (currentAngle >= angleRange[currentStep][i][0] && currentAngle <= angleRange[currentStep][i][1]) {
//                Log.d("angle right", currentStep+"step, "+i+": "+angleRange[currentStep][i][0]+"<="+currentAngle+"<="+ angleRange[currentStep][i][1]);
//            } else {
//                Log.d("angle error", currentStep+"step, "+i+": "+angleRange[currentStep][i][0]+"<="+currentAngle+"<="+ angleRange[currentStep][i][1]);
//
//                return false;
//            }
//        }
//        return true;
        //hip-knee-ankle 150~180도
        //shoulder-elbow-wrist 100~180도
        //다리 각도  120~180도 || 110~180도
        //팔 각도 130~180도 || 0~110도

        double r_hnaAngle = getAngle(9,8,9,10);
        double r_sewAngle = getAngle(3,2,3,4);
        double r_legAngle = getAngle(1,0,8,9);
        double r_armAngle = getAngle(1,0,2,3);
        double l_hnaAngle = getAngle(12,11,12,13);
        double l_sewAngle = getAngle(6,5,6,7);
        double l_legAngle = getAngle(1,0,11,12);
        double l_armAngle = getAngle(1,0,5,6);

        if(r_hnaAngle>=150 && r_hnaAngle<=180 &&
            l_hnaAngle>=150 && l_hnaAngle<=180 &&
            r_sewAngle>=100 && r_sewAngle<=180 &&
            l_sewAngle>=100 && l_sewAngle<=180){
            if(currentStep==0 || currentStep == 2){
                if(r_legAngle>=150 && r_legAngle<=180 &&
                    l_legAngle>= 150 && l_legAngle<=180 &&
                    r_armAngle>=140 && r_armAngle<=180 &&
                    l_armAngle>=140 && l_armAngle<=180) {
                    return true;
                }
            }
            else if(currentStep==1){
                if(r_legAngle>=130 && r_legAngle<=180 &&
                        l_legAngle>= 130 && l_legAngle<=180 &&
                        r_armAngle<=110 &&
                        l_armAngle<=110) {
                    return true;
                }
            }
            else{
                if(r_legAngle>=130 && r_legAngle<=170 &&
                        l_legAngle>= 130 && l_legAngle<=170 &&
                        r_armAngle<=70 &&
                        l_armAngle<=70) {
                    return true;
                }
            }
        }
        Log.d("jumpingjack", r_hnaAngle+","+l_hnaAngle+","+r_sewAngle+","+l_sewAngle+","+r_legAngle+","+l_legAngle+","+r_armAngle+","+l_armAngle+"");
        return false;
    }

    // getter setter
    public float[][] getPoint() {
        return point;
    }
    public int getSteps() { return 4; }
    public void setPoint(float[][] point) {
        this.point = point;
    }
}