package com.example.partner;

import java.util.ArrayList;

public class ExCorrection {
    public String getCorrection(int exType ,Integer errorNo){
        ArrayList<String> correctionContents = new ArrayList<>();
        switch (exType){
            case 1:
                correctionContents = flankCorrectionContents;
                break;
            case 2:
                correctionContents = squatCorrectionContents;
                break;
            case 3:
                correctionContents = jJCorrectionContents;
                break;
        }
        return correctionContents.get(errorNo);
    }



    private ArrayList<String> jJCorrectionContents;
    public ArrayList<String> getjJCorrectionContents() {
        jJCorrectionContents.add("");

        return jJCorrectionContents;
    }
    private ArrayList<String> squatCorrectionContents;
    public ArrayList<String> getSquatCorrectionContents() {
        squatCorrectionContents.add("");

        return squatCorrectionContents;
    }
    private ArrayList<String> flankCorrectionContents;
    public ArrayList<String> getFlankCorrectionContents() {
        flankCorrectionContents.add("");

        return flankCorrectionContents;
    }
}
