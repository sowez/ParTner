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

        // 운동 준비-> 정자세->팔 옆으로-> 정자세-> 팔 위로

        // 정자세 (다리를 어깨너비만큼 벌려주세요)
        // 1. hip-knee-ankle -> 180도 (팔을 구부리지 마세요)
        // 2. shoulder-elbow-wrist -> 180도 (다리를 구부리지 마세요)
        // 3. head-neck / hip-knee -> 30도 이하 /or ankle과 shoulder의 x 좌표가 비슷해야함   (다리를 어깨너비만큼 더 벌려주세요 or 다리를 어깨너비만큼 덜 벌려주세요)
        // 4. head-neck / shoulder-elbow -> 20도 이하 (팔을 너무 벌리지 마세요)



        // 팔 옆인경우
        // 1. hip-knee-ankle -> 180도 (팔을 구부리지 마세요)
        // 2. shoulder-elbow-wrist -> 180도 (다리를 구부리지 마세요)
        // 3. head-neck / hip-knee -> 30도이상 90도 이하 (다리를 더 벌려주세요 or 다리를 덜 벌려주세요)
        // 4. head-neck / shoulder-elbow -> 80 ~ 100도 (팔을 더 올려주세요 or 팔을 좀 더 내려주세요)
        // 5. top 위치가 정자세보다 올라가야함 (뛰세요)

        // 정자세 (다리를 어깨너비만큼 벌려주세요)
        // 1. hip-knee-ankle -> 180도 (팔을 구부리지 마세요)
        // 2. shoulder-elbow-wrist -> 180도 (다리를 구부리지 마세요)
        // 3. head-neck / hip-knee -> 30도 이하 /or ankle과 shoulder의 x 좌표가 비슷해야함   (다리를 어깨너비만큼 더 벌려주세요 or 다리를 어깨너비만큼 덜 벌려주세요)
        // 4. head-neck / shoulder-elbow -> 20도 이하 (팔을 너무 벌리지 마세요)

        // 팔 위인경우
        // 1. hip-knee-ankle -> 180도 (팔을 구부리지 마세요)-> 좀더 오차 많이 주기 or 아예 빼도 됨
        // 2. shoulder-elbow-wrist -> 180도 (다리를 구부리지 마세요)
        // 3. head-neck / hip-knee -> 30도이상 90도 이하 (다리를 더 벌려주세요 or 다리를 덜 벌려주세요)
        // 4. head-neck / shoulder-elbow -> 145~ 180도 (팔을 더 올려주세요)
        // 5. top 위치가 정자세보다 올라가야함 (뛰세요)


    }


    //두 선분(네점)의 각도를 구하는 함수
    public double getAngle(int ff, int fs, int sf, int ss){
        double []v1 = {point[0][ff]-point[0][fs], point[1][ff]-point[1][fs]};
        double []v2 = {point[0][sf]-point[0][ss], point[1][sf]-point[1][ss]};

        double angle = 0;
        angle = ((v1[0]*v2[0]+v1[1]*v2[1])/(Math.sqrt(v1[0]*v1[0] + v1[1]*v1[1])*Math.sqrt(v2[0]*v2[0] + v2[1]*v2[1])));
        return Math.acos(angle)*180/Math.PI;
    }


    // getter setter
    public float[][] getPoint() {
        return point;
    }

    public void setPoint(float[][] point) {
        this.point = point;
    }
}
