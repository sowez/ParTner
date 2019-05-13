package com.example;

public class AngleResource {
    private int jjStepAngle[][][] = new int[5][8][4];
    private int jjStepAngleRange[][][] = new int[5][8][2];
    private float jjStartRange[][][]  = new float[14][2][2];

    private int jj_step_angle_size[] = {8, 8};
    private float[] jj_start_range = {
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
    private int jj_step_angle[] = {
            9, 8, 9, 10,
            12, 11, 12, 13,
            3, 2, 3, 4,
            6, 5, 6, 7,
            1, 0, 8, 9,
            1, 0, 11, 12,
            1, 0, 2, 3,
            1, 0, 5, 6,

            9, 8, 9, 10,
            12, 11, 12, 13,
            3, 2, 3, 4,
            6, 5, 6, 7,
            1, 0, 8, 9,
            1, 0, 11, 12,
            1, 0, 2, 3,
            1, 0, 5, 6
    };
    private int jj_step_angle_range[] = {
            150, 180,
            150, 180,
            100, 180,
            100, 180,
            120, 180,
            120, 180,
            130, 180,
            130, 180,

            150, 180,
            150, 180,
            100, 180,
            100, 180,
            110, 180,
            110, 180,
            0, 110,
            0, 110
    };

    public int[][][] getJjStepAngle() {
        int point = 0;
        for(int i=0;i<2;i++){
            for(int j=0;j<jj_step_angle_size[i];j++){
                for(int k=0;k<4;k++){
                    jjStepAngle[i][j][k]=jj_step_angle[point++];
                }
            }
        }
        return jjStepAngle;
    }

    public int[][][] getJjStepAngleRange() {
        int point = 0;
        for(int i=0;i<2;i++){
            for(int j=0;j<jj_step_angle_size[i];j++){
                for(int k=0;k<2;k++){
                    jjStepAngleRange[i][j][k]=jj_step_angle_range[point++];
                }
            }
        }

        return jjStepAngleRange;
    }

    public float[][][] getJjStartRange() {
        int point = 0;
        for (int i = 0; i <= 13; i++) {
            for (int j = 0; j <= 1; j++) {
                for (int k = 0; k <= 1; k++) {
                    jjStartRange[i][j][k] = jj_start_range[point++];
                }
            }
        }
        return jjStartRange;
    }
}
