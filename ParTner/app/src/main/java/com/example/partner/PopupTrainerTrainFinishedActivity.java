package com.example.partner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupTrainerTrainFinishedActivity extends Activity {

    private Context context = PopupTrainerTrainFinishedActivity.this;

    private TextView trainTime;
    private TextView userName;

    private String userNamestr;
    private long exerciseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trainer_train_finished_activity);


        trainTime = (TextView) findViewById(R.id.training_time);
        userName = (TextView) findViewById(R.id.name);

        userNamestr = CallData.getInstance().getCallReceiverName();
        exerciseTime = CallData.getInstance().getCallTime();

        trainTime.setText(Long.toString(exerciseTime));
        userName.setText(userNamestr);

    }

    public void mOk(View v) {
        ServerComm serverComm = new ServerComm();
        serverComm.init();
        serverComm.setTrainerOnline(SharedPreferenceData.getId(context), context);
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
