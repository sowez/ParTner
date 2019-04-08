package com.example.partner;

import android.util.Log;

public class JumpingJack extends Exercise{

    private float[][] point;
//    private int exCount;


    public JumpingJack(int exCount){
        super(exCount);
    }

    // 스쿼트 준비자세 체크하는 함수
    @Override
    public boolean checkReady(){
        boolean isReady = true;

        float[][][] jj_start_range = new float[14][2][2];
        float[] range_num = {
                45, 68, 6, 20,
                45, 68, 15, 33,

                28, 52, 19, 36,
                26, 50, 32, 48,
                21, 45, 40, 61,

                60, 82, 19, 36,
                64, 84, 32, 48,
                64, 84, 40, 61,

                36, 56, 44, 63,
                41, 54, 64, 87,
                40, 57, 83, 103,

                52, 72, 44, 63,
                52, 74, 64, 87,
                48, 68, 83, 103
        };
        int now = 0;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j <= 1; j++) {
                for (int k = 0; k <= 1; k++) {
                    jj_start_range[i][j][k] = range_num[now++];
                }
            }
        }

        for(int i=0;i<=13;i++){
            for(int j=0;j<=1;j++){
                if(jj_start_range[i][j][0]<=point[j][i] && point[j][i]<=jj_start_range[i][j][1]){
                    continue;
                }
                else {
                    Log.e("point error", i+","+j+"좌표: "+jj_start_range[i][j][0]+"<="+ point[j][i]+"<="+jj_start_range[i][j][1]);
                    isReady=false;
                    break;
                }
            }
            if(isReady==false) break;
        }
        return isReady;
    }


    // 점핑잭 운동 동작 인식하는 함수
    @Override
    public void doExercise(){

    }


    // getter setter
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }
}
