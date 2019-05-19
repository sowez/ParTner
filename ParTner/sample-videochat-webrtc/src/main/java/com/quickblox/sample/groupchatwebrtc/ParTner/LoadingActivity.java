package com.quickblox.sample.groupchatwebrtc.ParTner;

import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.quickblox.sample.groupchatwebrtc.R;

import java.util.ArrayList;

public class LoadingActivity extends Activity {

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(SharedPreferenceData.getToken(this).length() == 0) {
            // call Login Activity
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();

        } else {
            // Call Next Activity
            if(SharedPreferenceData.getType(this).equals("sportsman")){
                intent = new Intent(this, UserMainMenuActivity.class);
                intent.putExtra("STD_NUM", SharedPreferenceData.getToken(this));
                startActivity(intent);
                this.finish();
            }else{
                intent = new Intent(this, TrainerMainMenuActivity.class);
                intent.putExtra("STD_NUM", SharedPreferenceData.getToken(this));
                startActivity(intent);
                this.finish();
            }
        }
    }
}
