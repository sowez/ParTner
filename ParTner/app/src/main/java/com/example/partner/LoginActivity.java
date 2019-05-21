package com.example.partner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "TAG";

    private EditText login_id, login_pw;
    private Button loginButton, signupButton, nextButton;
    private CheckBox autologinCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        permission();
        init();
    }

    public void init() {

        login_id = (EditText) findViewById(R.id.login_id);
        login_pw = (EditText) findViewById(R.id.login_pw);

        autologinCheck = (CheckBox) findViewById(R.id.autologin);

        loginButton = (Button) findViewById(R.id.loginbutton);
        signupButton = (Button) findViewById(R.id.signupbutton);
        nextButton = (Button) findViewById(R.id.next);
        loginButton.setOnClickListener(this::onClickLogin);
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
        nextButton.setOnClickListener(view->{
            Intent intent = new Intent(this, UserMainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void onClickLogin(View v) {

        String userId = login_id.getText().toString();
        String userPw = login_pw.getText().toString();

        ServerComm serverComm = new ServerComm();
        RetrofitCommnunication retrofitComm = serverComm.init();
        JsonObject logindata = new JsonObject();
        logindata.addProperty("id", userId);
        logindata.addProperty("pw", userPw);

        retrofitComm.login(logindata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    String loginResult = data.get("loginResult").getAsString();
                    Log.d(TAG, "onClickLogin: " + loginResult);
                    switch (loginResult) {
                        case "fail": {
                            Toast.makeText(this, "로그인에 실패하였습니다.아이디 또는 비밀번호를 다시 한번 입력해 주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case "diffrent": {
                            Toast.makeText(this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case "login_success": {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClickLogin: data" + data);
                            String token = data.get("token").getAsString();
                            String type = data.get("type").getAsString();
                            String username = data.get("username").getAsString();
                            String id = data.get("id").getAsString();

                            boolean auto = autologinCheck.isChecked();

                            SharedPreferenceData.saveToken(this, id, token, username, type, auto);

                            if (type.equals("trainer")) {
                                Intent intent = new Intent(this, TrainerMainMenuActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(this, UserMainMenuActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                        }
                        default: {
                            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                }, err -> {
                    Log.d(TAG, "onClickLogin: error 발생");
                    Log.d(TAG, "onClickLogin: " + err.getMessage());
                });
    }

    private void permission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                Log.e("TAG", "onPermissionGranted: 권한요청 성공");
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                Log.e("TAG", "onPermissionDenied: 권한요청 실패");
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setDeniedMessage("사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

}
