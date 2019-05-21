package com.example.partner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PopupTrainerInfoActivity extends Activity {

    private ImageView profileImage;
    private RatingBar mRating;
    private TextView name;
    private TextView traingType;
    private TextView introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trainerinfo_popup);


        profileImage = (ImageView) findViewById(R.id.profile_img);
        mRating = (RatingBar) findViewById(R.id.ratingbar);
        name = (TextView) findViewById(R.id.name);
        traingType = (TextView) findViewById(R.id.training_type);
        introduction = (TextView) findViewById(R.id.self_introduction);

        Intent intent = getIntent();

        // 이미지도 넣어야함...
        float starrate_data = Float.parseFloat(intent.getStringExtra("star_rate"));
        String name_data = intent.getStringExtra("name");
        String train_data = intent.getStringExtra("traintype");
        String intro_data = intent.getStringExtra("intro");

        mRating.setRating(starrate_data);
        name.setText(name_data);
        traingType.setText(train_data);
        introduction.setText(intro_data);
    }

    public void mOnClose(View v) {
//        Intent intent = new Intent();
//        intent.putExtra("","");
//        setResult(RESULT_OK, intent);
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

//    @Override
//    public void onBackPressed() {
//        //안드로이드 백버튼 막기
//        return;
//    }

}
