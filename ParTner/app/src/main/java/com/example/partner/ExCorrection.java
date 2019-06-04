package com.example.partner;

import android.util.Log;

import java.util.ArrayList;

public class ExCorrection {
    public String getCorrection(int exType ,Integer errorNo){
        ArrayList<String> correctionContents = new ArrayList<>();
        switch (exType){
            case 1:
                correctionContents = getFlankCorrectionContents();
                break;
            case 2:
                correctionContents = getSquatCorrectionContents();
                break;
            case 3:
                correctionContents = getJJCorrectionContents();
                break;
        }
        return correctionContents.get(errorNo);
    }


    public ArrayList<String> getJJCorrectionContents() {
        ArrayList<String> jJCorrectionContents = new ArrayList<>();
        jJCorrectionContents.add(0, "화면 안으로 들어오세요");
        jJCorrectionContents.add(1, "팔을 구부리지 마세요");
        jJCorrectionContents.add(2, "다리를 구부리지 마세요");
        jJCorrectionContents.add(3, "팔을 올려 주세요");
        jJCorrectionContents.add(4, "팔을 내려 주세요");
        jJCorrectionContents.add(5, "발을 모아주세요");
        jJCorrectionContents.add(6, "발을 벌려주세요");
        jJCorrectionContents.add(7, "뛰어주세요");
        jJCorrectionContents.add(8, "");
        jJCorrectionContents.add(9, "운동을 시작하세요");



        return jJCorrectionContents;
    }
    public ArrayList<String> getSquatCorrectionContents() {
        ArrayList<String> squatCorrectionContents = new ArrayList<>();
        squatCorrectionContents.add(0, "화면 안으로 들어오세요");
        squatCorrectionContents.add(1, "운동하는 동안 발을 고정시키세요");
        squatCorrectionContents.add(2, "");
        squatCorrectionContents.add(3, "고개를 숙이지 말고 정면을 바라보세요");
        squatCorrectionContents.add(4, "정면을 바라보세요");
        squatCorrectionContents.add(5, "똑바로 서세요");
        squatCorrectionContents.add(6, "너무 많이 앉지 마세요");
        squatCorrectionContents.add(7, "조금 더 앉으세요");
        squatCorrectionContents.add(8, "무릎이 발보다 많이 나오지 않게 하세요");
        squatCorrectionContents.add(9, "운동을 시작하세요");

        return squatCorrectionContents;
    }
    public ArrayList<String> getFlankCorrectionContents() {
        ArrayList<String> flankCorrectionContents = new ArrayList<>();
        flankCorrectionContents.add(0, "화면 안으로 들어오세요");
        flankCorrectionContents.add(1, "고개를 내리지 마세요");
        flankCorrectionContents.add(2, "고개를 올리지 마세요");
        flankCorrectionContents.add(3, "엉덩이를 내리지 마세요");
        flankCorrectionContents.add(4, "엉덩이를 올리지 마세요");
        flankCorrectionContents.add(5, "다리를 구부리지 마세요");
        flankCorrectionContents.add(6, "팔을 구부리지 마세요");
        flankCorrectionContents.add(7, "조금 더 앉으세요");
        flankCorrectionContents.add(8, "무릎이 발보다 많이 나오지 않게 하세요");
        flankCorrectionContents.add(9, "운동을 시작하세요");

        return flankCorrectionContents;
    }
}
