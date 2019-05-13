package com.example;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

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
        float jj_start_range[][][] = angleResource.getJjStartRange();
        boolean isReady = true;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j <= 1; j++) {
                if (jj_start_range[i][j][0] <= point[j][i] && point[j][i] <= jj_start_range[i][j][1]) {
                    continue;
                } else {
                    Log.e("point error", i + "," + j + "좌표: " + jj_start_range[i][j][0] + "<=" + point[j][i] + "<=" + jj_start_range[i][j][1]);
                    isReady = false;
                    break;
                }
            }
            if (isReady == false) break;
        }
        return isReady;
    }


    // 점핑잭 운동 동작 인식하는 함수
    @Override
    public boolean doExercise(int currentStep) {
        Log.d("step", currentStep + "");

        int angle[][][] = angleResource.getJjStepAngle();
        int angleRange[][][] = angleResource.getJjStepAngleRange();
        for (int i = 0; i < 8; i++) {
            Log.d("angle here", angle[currentStep][i][0] + "," + angle[currentStep][i][1] + "," + angle[currentStep][i][2] + "," + angle[currentStep][i][3] + "," + angleRange[currentStep][i][0] + "," + angleRange[currentStep][i][1]);
            double currentAngle = getAngle(angle[currentStep][i][0], angle[currentStep][i][1], angle[currentStep][i][2], angle[currentStep][i][3]);
            if (currentAngle >= angleRange[currentStep][i][0] && currentAngle <= angleRange[currentStep][i][1]) {
                Log.d("angle right", currentStep+"step, "+i+": "+angleRange[currentStep][i][0]+"<="+currentAngle+"<="+ angleRange[currentStep][i][1]);
            } else {
                Log.d("angle error", currentStep+"step, "+i+": "+angleRange[currentStep][i][0]+"<="+currentAngle+"<="+ angleRange[currentStep][i][1]);

                return false;
            }
        }
        return true;
    }

    // getter setter
    public float[][] getPoint() {
        return point;
    }
    public int getSteps() { return 2; }
    public void setPoint(float[][] point) {
        this.point = point;
    }
}
