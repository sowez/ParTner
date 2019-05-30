package com.example.partner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PopupTrainFinishedActivity extends Activity {

    private TextView trainTime;
    private TextView trainerName;
    private RatingBar mRating;
    private ToggleButton favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trainfinished_popup);

        trainTime = (TextView) findViewById(R.id.popup_finish_training_time);
        trainerName = (TextView) findViewById(R.id.popup_finish_trainer_name);
        mRating = (RatingBar) findViewById(R.id.popup_finish_ratingbar);
        favorites = (ToggleButton) findViewById(R.id.popup_finish_bookmark);

    }

    public void mOk(View v) {

        // 평가 안하면 안닫히도록
        if(mRating.getRating() > 0){
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
