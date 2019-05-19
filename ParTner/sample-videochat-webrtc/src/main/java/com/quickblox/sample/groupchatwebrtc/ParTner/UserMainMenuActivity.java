package com.quickblox.sample.groupchatwebrtc.ParTner;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.sample.groupchatwebrtc.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class UserMainMenuActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Context context = this;
    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역
    private ViewGroup btnLayout;

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    private ImageButton menu_btn;

    private Button aloneButton, togetherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        // Toolbar 설정
        mToolbar = findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mainLayout = findViewById(R.id.id_user_menu);
        viewLayout = findViewById(R.id.user_fl_slide);
        sideLayout = findViewById(R.id.user_view_slidebar);
        btnLayout = findViewById(R.id.user_menu_btns);

        menu_btn = findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        addSideBar();

        aloneButton = (Button)findViewById(R.id.user_btn);
        togetherButton = (Button)findViewById(R.id.trainer_btn);

        //test
        aloneButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), UserCallHistoryActivity.class);
            startActivity(intent);
            finish();
        });

        togetherButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TrainerListActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isMenuShow) {
            closeMenu();
        } else {

            if (isExitFlag) {
                if(!SharedPreferenceData.getAutologinChecked(this)){
                    SharedPreferenceData.clearUserData(this);
                }
                finish();
            } else {
                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> isExitFlag = false, 2000);
            }
        }

    }

    private void addSideBar() {
        UserSideBarView sidebar = new UserSideBarView(context);
        sideLayout.addView(sidebar);
        viewLayout.setOnTouchListener((v,event)->true);
        mainLayout.setOnTouchListener((v,event)->true);
        sidebar.setEventListener(new UserSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                closeMenu();
            }

            @Override
            public void btnHome() {
                closeMenu();
            }

            @Override
            public void btnTraining() {
                Toast.makeText(context, "Training 화면", Toast.LENGTH_LONG).show();
                closeMenu();
            }

            @Override
            public void btnCall() {
                isMenuShow = false;
                Intent intent = new Intent(context, UserCallHistoryActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnBookmark() {
                Toast.makeText(context, "Trainer bookmark 화면", Toast.LENGTH_LONG).show();
                closeMenu();
            }

            @Override
            public void btnLogout() {

                SharedPreferenceData.clearUserData(context);
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                isMenuShow = false;
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnSetting() {
                Toast.makeText(context, "설정버튼", Toast.LENGTH_LONG).show();
                closeMenu();
            }
        });
    }

    public void closeMenu() {
        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        sideLayout.setVisibility(View.GONE);
        new Handler().postDelayed(() -> {
            viewLayout.setVisibility(View.GONE);
            viewLayout.setEnabled(false);
            mainLayout.setEnabled(true);
            mainLayout.setClickable(true);

            aloneButton.setOnTouchListener((v,event)->false);
            togetherButton.setOnTouchListener((v,event)->false);
        }, 450);
    }

    public void showMenu() {
        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.setVisibility(View.VISIBLE);
        sideLayout.setClickable(true);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        viewLayout.setClickable(true);
        mainLayout.setEnabled(false);

        aloneButton.setOnTouchListener((v,event)->true);
        togetherButton.setOnTouchListener((v,event)->true);
        Log.e("TAG", "메뉴버튼 클릭");
    }

}