package com.example.partner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "TAG";

    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText id, pw;
    private Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        id = findViewById(R.id.login_id);
        pw = findViewById(R.id.login_pw);

        loginButton = findViewById(R.id.loginbutton);
        signupButton = findViewById(R.id.signupbutton);
        loginButton.setOnClickListener(this::loginButtonClickEvent);
        signupButton.setOnClickListener(this::signupButtonClickEvent);
    }

    private void loginButtonClickEvent(View v){
        Log.d(TAG, "loginButtonClickEvent: 로그인");
    }

    private void signupButtonClickEvent(View v){
        Intent SignUpMenuActivityIntent = new Intent(this, SignUpMenuActivity.class);
        startActivity(SignUpMenuActivityIntent);
    }
}
