package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TrainerProfileEditActivity extends AppCompatActivity {

    private Context context = this;

    private Toolbar mToolbar;

    private Button editDoneBtn;


    private EditText nameEditText;
    private EditText introEditText;
    private CheckBox yogaCheckBox;
    private CheckBox muscleCheckBox;
    private CheckBox pilatesCheckBox;
    private CheckBox stretchingCheckBox;
    private RadioGroup radioGroup;
    private RadioButton manRadioBtn;
    private RadioButton womanRadioBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile_edit);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        editDoneBtn = (Button) findViewById(R.id.profile_edit_btn);

        nameEditText = (EditText) findViewById(R.id.edit_name);
        introEditText = (EditText) findViewById(R.id.edit_intro);
        yogaCheckBox = (CheckBox) findViewById(R.id.checkBox1);
        muscleCheckBox = (CheckBox) findViewById(R.id.checkBox2);
        pilatesCheckBox = (CheckBox) findViewById(R.id.checkBox3);
        stretchingCheckBox = (CheckBox) findViewById(R.id.checkBox4);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        manRadioBtn = (RadioButton) findViewById(R.id.man);
        womanRadioBtn = (RadioButton) findViewById(R.id.woman);

        Intent intent = getIntent();

        nameEditText.setText(intent.getStringExtra("name"));
        introEditText.setText(intent.getStringExtra("intro"));
        ArrayList<String> traintypeArrayList = intent.getStringArrayListExtra("traintype");
        for(int i=0; i<traintypeArrayList.size(); i++){
            if(traintypeArrayList.contains("yoga")) {
                yogaCheckBox.setChecked(true);
            }
            if(traintypeArrayList.contains("muscle")) {
                muscleCheckBox.setChecked(true);
            }
            if(traintypeArrayList.contains("pilates")){
                pilatesCheckBox.setChecked(true);
            }
            if(traintypeArrayList.contains("stretching")){
                stretchingCheckBox.setChecked(true);
            }
        }
        if(intent.getStringExtra("gender").equals("male")) {
            manRadioBtn.setChecked(true);
        }
        else {
            womanRadioBtn.setChecked(true);
        }

        // 수정 완료 버튼 리스너
        editDoneBtn.setOnClickListener(v -> {
            ArrayList<String> trainingtype = new ArrayList<String>();
            TrainerEditData trainerEditData;
            if(yogaCheckBox.isChecked()) {
                trainingtype.add("yoga");
            }
            if(muscleCheckBox.isChecked()) {
                trainingtype.add("muscle");
            }
            if(pilatesCheckBox.isChecked()) {
                trainingtype.add("pilates");
            }
            if(stretchingCheckBox.isChecked()) {
                trainingtype.add("stretching");
            }

            RadioButton selectedRdo = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
            if(selectedRdo.getText().toString().equals("남자")){

                trainerEditData = new TrainerEditData(SharedPreferenceData.getId(this), nameEditText.getText().toString(), introEditText.getText().toString(), "male", trainingtype);
            }
            else {
                trainerEditData = new TrainerEditData(SharedPreferenceData.getId(this), nameEditText.getText().toString(), introEditText.getText().toString(), "female", trainingtype);
            }

            RetrofitCommnunication retrofitCommnunication = new ServerComm().init();
            Call<JsonObject> editProfile = retrofitCommnunication.trainerEditProfile(trainerEditData);
            editProfile.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("EditProfile", response.body().toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(TrainerProfileEditActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG)
                            .show();
                    Log.e("TAG", "onFailure: " + t.getMessage() );
                }
            });

            finish();
        });

    }
}
