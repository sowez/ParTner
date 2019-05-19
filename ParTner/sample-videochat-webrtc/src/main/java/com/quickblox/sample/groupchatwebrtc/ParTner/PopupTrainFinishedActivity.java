package com.quickblox.sample.groupchatwebrtc.ParTner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quickblox.sample.groupchatwebrtc.R;

public class PopupTrainFinishedActivity extends Activity {

    private TextView trainTime;
    private TextView trainerName;
    private RatingBar mRating;
    private ImageView favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trainfinished_popup);

        trainTime = (TextView) findViewById(R.id.training_time);
        trainerName = (TextView) findViewById(R.id.name);
        mRating = (RatingBar) findViewById(R.id.ratingbar);
        favorites = (ImageView) findViewById(R.id.favorites);

        // 값 넘겨 받기
//        Intent intent = getIntent();
//        String name_data = intent.getStringExtra("name");
//        trainerName.setText(name_data);
    }

    public void mOk(View v) {
//        Intent intent = new Intent();
//        intent.putExtra("","");
//        setResult(RESULT_OK, intent);
        // 평가 안하면 안닫히도록
        finish();
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
