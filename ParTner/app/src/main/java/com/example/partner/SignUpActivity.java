package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
//signup
public class SignUpActivity extends AppCompatActivity {

    private ImageButton signupTrainer, signupUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupUser = findViewById(R.id.signup_user);
        signupUser.setOnClickListener(this::signupUserClickEvent);

        signupTrainer = findViewById(R.id.signup_trainer);
        signupTrainer.setOnClickListener(this::signupTrainerClickEvent);

    }

    private void signupUserClickEvent(View v){
        Intent intent = new Intent(this, UserSignUpActivity.class);
        startActivity(intent);
    }

    private void signupTrainerClickEvent(View v){
        Intent intent = new Intent(this, TrainerSignUpActivity.class);
        startActivity(intent);
    }
}
