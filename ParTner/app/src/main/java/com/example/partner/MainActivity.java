package com.example.partner;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "TAG";

    private Context mContext = MainActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Toolbar toolbar;
    private ActionBar actionBar;

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        addSideView();  //사이드바 add
    }

    private void init() {

        mainLayout = findViewById(R.id.id_main);
        viewLayout = findViewById(R.id.fl_silde);
        sideLayout = findViewById(R.id.view_sildebar);

        toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.appbar_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu :
                showMenu();
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
//main
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
                finish();
            } else {

                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitFlag = false;
                    }
                }, 2000);
            }
        }
    }

    private void addSideView() {

        UserSideBarView sidebar = new UserSideBarView(mContext);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sidebar.setEventListener(new UserSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                Log.d(TAG, "btnCancel");
                closeMenu();
            }

            @Override
            public void btnLevel1() {
                Log.d(TAG, "btnLevel1");
                closeMenu();
            }

            @Override
            public void btnLevel2() {
                Log.d(TAG, "btnLevel2");

                closeMenu();
            }

            @Override
            public void btnLevel3() {
                Log.d(TAG, "btnLevel3");

                closeMenu();
            }

            @Override
            public void btnLevel4() {
                Log.d(TAG, "btnLevel4");
                closeMenu();
            }

            @Override
            public void btnLevel5() {
                Log.d(TAG, "btnLevel5");
                closeMenu();
            }

            @Override
            public void btnLevel6() {
                Log.d(TAG, "btnLevel6");
                closeMenu();
            }
        });
    }


    public void closeMenu() {

        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(mContext, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewLayout.setVisibility(View.GONE);
                viewLayout.setEnabled(false);
                mainLayout.setEnabled(true);
            }
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
