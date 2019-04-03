package com.example.partner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpMenuActivity extends AppCompatActivity {

    private Button userLoginButton;
    private Button trainerLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_menu);

        userLoginButton = findViewById(R.id.userLoginButton);
        trainerLoginButton = findViewById(R.id.trainerLoginButton);

        userLoginButton.setOnClickListener(this::userLoginButtonClickEvent);
        trainerLoginButton.setOnClickListener(this::trainerLoginButtonClickEvent);
    }

    private void userLoginButtonClickEvent(View v){
        Intent intent = new Intent(this, UserSignUpActivity.class);
        startActivity(intent);
    }

    private void trainerLoginButtonClickEvent(View v){
        Intent intent = new Intent(this, TrainerSignUpActivity.class);
        startActivity(intent);
    }
}


