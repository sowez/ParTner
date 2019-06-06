package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserSignUpActivity extends AppCompatActivity {

    private EditText userId, userPw, userPw_check, userName;
    private RadioGroup userSex;
    private RadioButton userMale, userFemale;
    private Button btn_userSignUp, btn_idOverlapCheck;
    private boolean overlapCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        init();
    }

    private void init() {

        userId = (EditText) findViewById(R.id.user_id);
        userPw = (EditText) findViewById(R.id.user_pw);
        userPw_check = (EditText) findViewById(R.id.user_pw_check);
        userName = (EditText) findViewById(R.id.user_name);
        userMale = (RadioButton) findViewById(R.id.user_male);
        userFemale = (RadioButton) findViewById(R.id.user_female);
        userSex = (RadioGroup) findViewById(R.id.user_sex);
        btn_userSignUp = (Button) findViewById(R.id.btn_user_signup);
        btn_userSignUp.setOnClickListener(this::onClickSignup);

        btn_idOverlapCheck = (Button) findViewById(R.id.btn_userid_overlap_check);
        btn_idOverlapCheck.setOnClickListener(this::onClickOverlapCheck);

    }

    private void onClickOverlapCheck(View v){
        String id = userId.getText().toString();

        ServerComm serverComm = new ServerComm();
        RetrofitCommnunication retrofitComm = serverComm.init();
        retrofitComm.getOverlapCheck("sportsman", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data->{
                    String res = data.get("result").getAsString();
                    if(res.equals("none")){
                        overlapCheck = true;
                        Toast.makeText(this, "사용할 수 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "중복되는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                    }
                }, err->{
                    Log.e("TAG", "onClickBtnOverlapCheck: error발생");
                });
    }

    private void onClickSignup(View view) {

        String id = userId.getText().toString();
        String pw = userPw.getText().toString();
        String pw_check = userPw_check.getText().toString();
        String name = userName.getText().toString();
        String sex;
        if (userMale.isChecked()) {
            sex = "male";
        } else {
            sex = "female";
        }

        //영어, 숫자, 특수문자 포함 6자리
        String idPattern = "^[A-Za-z0-9+]{3,12}$";
        String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,}$";
        if (Pattern.matches(idPattern, id) && Pattern.matches(pwPattern, pw) && pw.equals(pw_check)) {
            SignUpData signUpData = new SignUpData("sportsman", id, pw, name, sex, null);
            ServerComm serverComm = new ServerComm();
            serverComm.init();
            serverComm.postSignUp(signUpData, this);
            finish();
        } else if(!Pattern.matches(idPattern, id)){
            Toast.makeText(this, "아이디는 영문자와 숫자만 포함하여 3자리 이상 12자리 이하로 만들어주세요", Toast.LENGTH_LONG).show();
        } else if (pw.equals(pw_check)) {
            Toast.makeText(this, "비밀번호는 영문자, 숫자, 특수문자($,@,$,!,%,*,#,?,&)를 포함하여 6자리 이상으로 만들어주세요", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "비밀번호와 비밀번호 재확인이 일치하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }

}
