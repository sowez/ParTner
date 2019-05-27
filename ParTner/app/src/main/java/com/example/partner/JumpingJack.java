package com.example.partner;

import android.graphics.PointF;
import android.util.Log;
import java.util.ArrayList;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

public class JumpingJack extends Exercise {
    private float[][] point;

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

        //hip-knee-ankle 160~180도
        //shoulder-elbow-wrist 130~180도
        //다리 각도  150~180도 || 140~165도
        //팔 각도 150~180도 || 0~50도
        //다리
        ArrayList<Integer> result;
        result = new ArrayList<Integer>();

        super.dpPoint.add(new PointF((point[0][8]+point[0][11])/2, (point[1][8]+point[1][11])/2));   // dpPoint(14)

        double r_hnaAngle = getAngle(9,8,9,10);
        double r_sewAngle = getAngle(3,2,3,4);
        double r_legAngle = getAngle(1,0,8,9);
        double r_armAngle = getAngle(1,0,2,3);
        double l_hnaAngle = getAngle(12,11,12,13);
        double l_sewAngle = getAngle(6,5,6,7);
        double l_legAngle = getAngle(1,0,11,12);
        double l_armAngle = getAngle(1,0,5,6);
        double legAngle = getAngle(14,10,14,13);
        double x = (point[0][8]+point[0][11])/2;
        double y = (point[1][8]+point[1][11])/2;
        Log.d("jumpingjack", x+""+y+""+point[0][12]+""+point[1][12]+""+point[0][9]+""+point[1][9]);


        if(isNaN(r_hnaAngle)|| isNaN(r_sewAngle) || isNaN(r_armAngle) || isNaN(l_hnaAngle) || isNaN(l_sewAngle) || isNaN(l_armAngle) ){
            result.add(-1);
            result.add(0);
            Log.d("error", "0");
            Log.d("error", r_hnaAngle+","+l_hnaAngle+","+r_sewAngle+","+l_sewAngle+","+r_legAngle+","+l_legAngle+","+r_armAngle+","+l_armAngle+","+legAngle+"");

        }
        else if(r_hnaAngle<160 || l_hnaAngle<160){
            result.add(-1);
            result.add(1);
            Log.d("error", "1");
        }
        else if(r_sewAngle<130 || l_sewAngle < 130){
            result.add(-1);
            result.add(2);
            Log.d("error", "2");
        }
        else {
            if(currentStep==0) {
                if(legAngle>15) {
                    result.add(-1);
                    result.add(5);
                    Log.d("error", "5");
                }
                else if(r_armAngle<150 || l_armAngle<150){
                    result.add(-1);
                    result.add(3);

                    Log.d("error", "3");
                }
            }
            else{
                if(legAngle<8) {
                    result.add(-1);
                    result.add(6);
                    Log.d("error", "6");
                }
                else if(legAngle>50){
                    result.add(-1);
                    result.add(5);

                    Log.d("error", "5");
                }
                else if(r_armAngle>50 || l_armAngle>50){
                    result.add(-1);
                    result.add(4);
                    Log.d("error", "4");
                }
            }
        }
        Log.d("jumpingjack", r_hnaAngle+","+l_hnaAngle+","+r_sewAngle+","+l_sewAngle+","+r_legAngle+","+l_legAngle+","+r_armAngle+","+l_armAngle+","+legAngle+"");

        if(result.size()==0 && r_hnaAngle!=NaN) {
            result.add(1);

            Log.d("error", "-1");
        }
        return result;
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
