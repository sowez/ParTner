package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
//user
public class UserSignUpActivity extends AppCompatActivity {

    private EditText userId, userPw, userPw_check, userName;
    private RadioGroup userSex;
    private RadioButton userMale, userFemale;
    private Button btn_userSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        init();
    }

    private void init() {
        userId = findViewById(R.id.user_id);
        userPw = findViewById(R.id.user_pw);
        userPw_check = findViewById(R.id.user_pw_check);
        userName = findViewById(R.id.user_name);
        userMale = findViewById(R.id.user_male);
        userFemale = findViewById(R.id.user_female);
        userSex = findViewById(R.id.user_sex);
        btn_userSignUp = findViewById(R.id.btn_user_signup);
        btn_userSignUp.setOnClickListener(this::OnClick);

    }

    private void OnClick(View view) {
        String id = userId.getText().toString();
        String pw = userPw.getText().toString();
        String name = userName.getText().toString();
        String sex = null;
        if (userMale.isChecked()) {
            sex = "male";
        } else if (userFemale.isChecked()) {
            sex = "female";
        }
        SignUpData signUpData = new SignUpData("user", id, pw, name, sex, null);

        ServerComm serverComm = new ServerComm();
        serverComm.init();
        serverComm.postSignUp(signUpData, this);
    }

}
