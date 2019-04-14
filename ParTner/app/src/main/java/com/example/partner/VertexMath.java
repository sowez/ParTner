package com.example.partner;
import java.nio.FloatBuffer;



public class VertexMath {

    public static double getAngle(FloatBuffer line1 , FloatBuffer line2){

        float line1VecX = line1.get(3) - line1.get(0);

        float line1VecY = line1.get(4) - line1.get(1);



        float line2VecX = line2.get(3) - line2.get(0);

        float line2VecY = line2.get(4) - line2.get(1);



        return ArcCalc(line1VecX, line1VecY,line2VecX,line2VecY);

    }



    private static double ArcCalc(double vec1X,double vec1Y , double vec2X ,double vec2Y){

        double angle = 0.0f;



        double inner = (vec1X*vec2X + vec1Y*vec2Y);// 기본내적



        double i1=	Math.sqrt(vec1X*vec1X+ vec1Y*vec1Y); // 처음 직선의 노말라이즈 준비

        double i2=	Math.sqrt(vec2X*vec2X+ vec2Y*vec2Y); // 두번째 직선의 노말라이즈 준비



        vec1X=(vec1X/i1); // 각 요소를 단위 벡터로 변환한다.

        vec1Y=(vec1Y/i1);

        vec2X=(vec2X/i2);

        vec2Y=(vec2Y/i2);



        //위 과정을 거치면 결과적으로 계산된 두 직선의 크기는 1이면서 방향은 이전과 같은 단위벡터가 된다.

        inner =(vec1X*vec2X + vec1Y*vec2Y); //다시 내적을 구한다.

        angle = Math.acos(inner)*180/Math.PI;

        // 아크 코사인을 통해 라디안을 구하고 그걸 각도로 변환하기 위해 180을 곱하고 파이로나눈다.

//        if(vec1X > vec2X) angle=360-angle;//?? 이게 아닌거 같다.

        // 사이각은 최대 0-180도 사이마 존재함으로 입력된 x값을 참조하여 360에 가까운 값으로 변환하는 과정을 거친다.



        return angle;

    }

}
