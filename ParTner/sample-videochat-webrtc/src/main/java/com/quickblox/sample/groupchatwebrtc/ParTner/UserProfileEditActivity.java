package com.quickblox.sample.groupchatwebrtc.ParTner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quickblox.sample.groupchatwebrtc.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserProfileEditActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        editBtn = (Button)findViewById(R.id.profile_edit_btn);


        // 수정하기 버튼 리스너
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
