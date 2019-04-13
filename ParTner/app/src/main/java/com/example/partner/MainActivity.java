package com.example.partner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "TAG";

    private Context mContext = MainActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Toolbar mToolbar;

    private Boolean isMenuShow = false;

    private ImageButton menu_btn;
    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        addSideView();  //사이드바 add
    }

    private void init() {

        mainLayout = findViewById(R.id.id_main);
        viewLayout = findViewById(R.id.fl_slide);
        sideLayout = findViewById(R.id.view_slidebar);

        // // Toolbar 설정
        mToolbar = findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menu_btn = findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        btn1 = findViewById(R.id.trainer_btn);
        btn2 = findViewById(R.id.user_btn);

        // 트레이너로 로그인 했을 때
        btn1.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TrainerMainMenuActivity.class);
            startActivity(intent);
        });

        // 사용자로 로그인 했을 때
        btn2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), UserMainMenuActivity.class);
            startActivity(intent);
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
        }
    }

    private void addSideView() {

        UserSideBarView sidebar = new UserSideBarView(mContext);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(view -> {
        });

        sidebar.setEventListener(new UserSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                Log.d(TAG, "btnCancel");
                closeMenu();
            }

            @Override
            public void btnMyPage() {
                Log.d(TAG, "btnLevel1");
                closeMenu();
            }

            @Override
            public void btnTraining() {
                Log.d(TAG, "btnLevel2");
                closeMenu();
            }

            @Override
            public void btnCall() {
                Log.d(TAG, "btnLevel3");
                closeMenu();
            }

            @Override
            public void btnBookmark() {
                Log.d(TAG, "btnLevel4");
                closeMenu();
            }

            @Override
            public void btnLogout() {
                Log.d(TAG, "btnLevel5");
                closeMenu();
            }

            @Override
            public void btnSetting() {
                Log.d(TAG, "btnLevel6");
                closeMenu();
            }
        });
    }

    public void closeMenu() {

        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(() -> {
            viewLayout.setVisibility(View.GONE);
            viewLayout.setEnabled(false);
            mainLayout.setEnabled(true);
        }, 450);
    }

    public void showMenu() {
        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
        Log.e(TAG, "메뉴버튼 클릭");
    }
}
