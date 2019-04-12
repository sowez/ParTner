package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class UserMainMenuActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Button aloneButton;
    private Button togetherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        aloneButton = (Button)findViewById(R.id.user_btn);
        togetherButton = (Button)findViewById(R.id.trainer_btn);

        //test
        aloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCallHistoryActivity.class);
                startActivity(intent);
            }
        });

        togetherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TrainerListActivity.class);
                startActivity(intent);
            }
        });
    }

}
