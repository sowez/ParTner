package com.example.partner;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ToggleButton;

public class PopupTrainFinishedActivity extends Activity {

    private TextView trainTime;
    private TextView trainerName;
    private RatingBar mRating;
    private ToggleButton favorites;

    private String trainerID;
    private long exerciseTime;

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

        trainerID = CallData.getInstance().getCallReceiverName();
        exerciseTime = CallData.getInstance().getCallTime();

        trainerName.setText(trainerID);
        trainTime.setText(Long.toString(exerciseTime));

    }

    public void mOk(View v) {

        if(mRating.getRating() < 1) {
            Toast.makeText(PopupTrainFinishedActivity.this, "별점을 1점이상 입력해 주세요.", Toast.LENGTH_LONG)
                    .show();
        }
        else {
            // 평가 결과 서버에 올리기
            // 평가 안하면 버튼 안눌리도록
            ServerComm serverComm = new ServerComm();
            RetrofitCommnunication retrofitCommnunication = serverComm.init();

            JsonObject ratedata = new JsonObject();
            ratedata.addProperty("trainerID", trainerID);
            ratedata.addProperty("star_rate", mRating.getRating());

            Call<JsonObject> editStarRate = retrofitCommnunication.trainerStarRate(ratedata);
            editStarRate.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("EditProfile", response.body().toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(PopupTrainFinishedActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG)
                            .show();
                    Log.e("TAG", "onFailure: " + t.getMessage() );
                }
            });


            // 통화 하고 평가했다 것을 표시기
            CallData.getInstance().setCalled(false);

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
