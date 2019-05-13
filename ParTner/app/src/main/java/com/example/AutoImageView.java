package com.example;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.support.v7.widget.AppCompat

public class AutoImageView extends ImageView {


    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public AutoImageView(Context context) {
        this(context, null);
    }

    public AutoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that is,
     * calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        ImageView personImg = (ImageView) findViewById(R.id.person_frame);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) personImg.getLayoutParams();
        params.width = width;
        params.height = height;
        if (0 != mRatioWidth && 0 != mRatioHeight) {
            if (width < height * mRatioWidth / mRatioHeight) {
//                params.width = width;
                params.height = width * mRatioHeight / mRatioWidth;

//                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                params.width = height * mRatioWidth / mRatioHeight;
//                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
        personImg.setLayoutParams(params);
    }

}
