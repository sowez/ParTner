package com.example.partner;

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
                correctionContents = getjJCorrectionContents();
                break;
        }
        return correctionContents.get(errorNo);
    }




    public ArrayList<String> getjJCorrectionContents() {
        ArrayList<String> jJCorrectionContents = new ArrayList<>();
        jJCorrectionContents.add("");

        return jJCorrectionContents;
    }
    public ArrayList<String> getSquatCorrectionContents() {
        ArrayList<String> squatCorrectionContents = new ArrayList<>();
        squatCorrectionContents.add(0, "팔을 앞으로 뻗으세요");
        squatCorrectionContents.add(1, "팔을 구부리지 마세요");
        squatCorrectionContents.add(2, "고개를 숙이지 말고 정면을 바라보세요");
        squatCorrectionContents.add(3, "정면을 바라보세요");
        squatCorrectionContents.add(4, "다음 스텝으로 못 넘어감");

        return squatCorrectionContents;
    }
    public ArrayList<String> getFlankCorrectionContents() {
        ArrayList<String> flankCorrectionContents = new ArrayList<>();
        flankCorrectionContents.add("");

        return flankCorrectionContents;
    }
}
