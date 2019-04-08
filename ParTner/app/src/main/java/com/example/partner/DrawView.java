package com.example.partner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;


public class DrawView extends View {

    private int mRatioWidth  = 0;
    private int mRatioHeight  = 0;

    private ArrayList<PointF> mDrawPoint = new ArrayList<PointF>();
    private int mWidth       = 0;
    private int mHeight      = 0;
    private float mRatioX    = 0;
    private float mRatioY    = 0;
    private int mImgWidth    = 0;
    private int mImgHeight   = 0;
    private int time = 0;

    private ImageView person_frame;

    private int mColorArray[] = {
            getResources().getColor(R.color.color_top, null),
            getResources().getColor(R.color.color_neck, null),
            getResources().getColor(R.color.color_l_shoulder, null),
            getResources().getColor(R.color.color_l_elbow, null),
            getResources().getColor(R.color.color_l_wrist, null),
            getResources().getColor(R.color.color_r_shoulder, null),
            getResources().getColor(R.color.color_r_elbow, null),
            getResources().getColor(R.color.color_r_wrist, null),
            getResources().getColor(R.color.color_l_hip, null),
            getResources().getColor(R.color.color_l_knee, null),
            getResources().getColor(R.color.color_l_ankle, null),
            getResources().getColor(R.color.color_r_hip, null),
            getResources().getColor(R.color.color_r_knee, null),
            getResources().getColor(R.color.color_r_ankle, null),
            getResources().getColor(R.color.color_background, null)
    };

//    private WindowManager mWindowManager;
//    private DisplayMetrics metrics;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float circleRadius = 0;
    private void initcircleRadius(){

        if ( circleRadius == 0 ){

            int mDenstity = 3 * (int) getContext().getResources().getDisplayMetrics().density;
            circleRadius = (float)mDenstity;
        }
    }

    private Paint mPaint = null;
    private void initmPaint(){

        if ( mPaint == null ){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            mPaint.setStyle(Paint.Style.FILL);

            int mDenstity = 2 * (int) getContext().getResources().getDisplayMetrics().density;
            int mScaledDensity = 13 * (int) getContext().getResources().getDisplayMetrics().scaledDensity;
            mPaint.setStrokeWidth((float) mDenstity);
            mPaint.setTextSize((float) mScaledDensity);
        }
    }

    // 画像のサイズをセットするメソッド
    public void setImgSize(int width, int height) {
        mImgWidth  = width;
        mImgHeight = height;
        requestLayout();
    }

    // 配列のデータをポイント型に変換して点群配列に追加する
    public void setDrawPoint(float[][] point, float ratio){

        mRatioX = (float)mImgWidth / (float)mWidth;
        mRatioY = (float)mImgHeight / (float)mHeight;

        mDrawPoint.clear();

        float tempX;
        float tempY;

        for(int i = 0; i <= 13; i++) {
            tempX = point[0][i] / ratio / mRatioX;
            tempY = point[1][i] / ratio / mRatioY;
            PointF point_temp = new PointF(tempX,tempY);
            mDrawPoint.add(point_temp);
        }
    }

    // アスペクト比を計算するメソッド
    public void setAspectRatio(int width, int height){
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be nagative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    public void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        initcircleRadius();
        initmPaint();

        if (mDrawPoint.isEmpty()) {
            return;
        }
        PointF prePointF = null;
        mPaint.setColor((int)0xff6fa8dc);
        PointF p1 = mDrawPoint.get(1);


        for (int i = 0; i < mDrawPoint.size(); i++) {
            if (i == 1) continue;
            switch (i) {
                case 0:
                    canvas.drawLine(mDrawPoint.get(i).x, mDrawPoint.get(i).y, p1.x, p1.y, mPaint);
                    break;
                case 2:
                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint);
                    break;
                case 5:
                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint);
                    break;
                case 8:
                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint);
                    break;
                case 11:
                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint);
                    break;
                default:
                    if (prePointF != null) {
                        mPaint.setColor(0xff6fa8dc);
                        canvas.drawLine(prePointF.x, prePointF.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint);
                    }
                    break;
            }
            prePointF = mDrawPoint.get(i);
        }

//        // 스쿼트 준비 영역 확인
//        mPaint.setColor(0xff6fa8dc);
//        canvas.drawLine(mWidth*(float)0.32,0,mWidth*(float)0.32,mHeight,mPaint);
//        canvas.drawLine(mWidth*(float)0.67,0,mWidth*(float)0.67,mHeight,mPaint);

        for (int i = 0; i < mDrawPoint.size(); i++) {
            mPaint.setColor((int)mColorArray[i]);
            canvas.drawCircle(mDrawPoint.get(i).x, mDrawPoint.get(i).y,circleRadius, mPaint);
        }
    }



    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                mWidth  = width;
                mHeight = width * mRatioHeight / mRatioWidth;
            } else {
                mWidth  = height * mRatioWidth / mRatioHeight;
                mHeight = height;
            }
        }
        setMeasuredDimension(mWidth, mHeight);

        if ( width != 0 &&  height !=0 ){
            mWidth  = width;
            mHeight = height;
        }

    }

}
