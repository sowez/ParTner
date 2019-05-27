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
        jJCorrectionContents.add(0, "안돼");
        jJCorrectionContents.add(1, "팔을 구부리지 마세요");
        jJCorrectionContents.add(2, "다리를 구부리지 마세요");
        jJCorrectionContents.add(3, "팔을 내려 주세요");
        jJCorrectionContents.add(4, "발을 모아주세요");
        jJCorrectionContents.add(5, "발을 벌려주세요");
        jJCorrectionContents.add(6, "발을 벌려주세요");
        return jJCorrectionContents;
    }
    public ArrayList<String> getSquatCorrectionContents() {
        ArrayList<String> squatCorrectionContents = new ArrayList<>();
        squatCorrectionContents.add("");

        return squatCorrectionContents;
    }
    public ArrayList<String> getFlankCorrectionContents() {
        ArrayList<String> flankCorrectionContents = new ArrayList<>();
        flankCorrectionContents.add("");

        return flankCorrectionContents;
    }
}
