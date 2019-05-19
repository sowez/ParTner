package com.quickblox.sample.groupchatwebrtc.ParTner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quickblox.sample.groupchatwebrtc.R;

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
        String name_data = intent.getStringExtra("name");
        name.setText(name_data);
    }

    public void mOnClose(View v) {

        Log.d("poppop", "mOnClose: 통화연결버튼");
        Intent intent = new Intent(this,com.quickblox.sample.groupchatwebrtc.activities.LoginActivity.class);
        intent.putExtra("trainer_name", name.getText());
        startActivity(intent);
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
