package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class TrainerProfileEditActivity extends AppCompatActivity {

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
        editDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JsonObject update_data = new JsonObject();
//                update_data.addProperty("name", userId);
//                update_data.addProperty("self_introduction", userPw);
//                update_data.addProperty("sex",);
//                update_data.addProperty("training_type",);;


                finish();
            }
        });

    }
}
