package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.partner.R;

//import androidx.appcompat.app.AppCompatActivity;

public class ExListActivity extends AppCompatActivity {

    private ImageView iv_ex1, iv_ex2, iv_ex3, iv_ex4, iv_ex5, iv_ex6;
    int exType = 0;

    // side bar

    private Toolbar mToolbar;
    private Context context = ExListActivity.this;
    private ViewGroup mainLayout;
    private ViewGroup viewLayout;
    private ViewGroup sideLayout;
    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    private ImageButton menu_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_list);

        Log.d("color", "onCreate: " + R.color.color_background);

        // side bar

        mToolbar = (Toolbar) findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mainLayout = (ViewGroup) findViewById(R.id.id_ex_list);
        viewLayout = (ViewGroup) findViewById(R.id.user_fl_slide);
        sideLayout = (ViewGroup) findViewById(R.id.user_view_slidebar);

        menu_btn = (ImageButton) findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        addSideBar();

        iv_ex1 = (ImageView) findViewById(R.id.iv_ex1);
        iv_ex2 = (ImageView) findViewById(R.id.iv_ex2);
        iv_ex3 = (ImageView) findViewById(R.id.iv_ex3);
        iv_ex4 = (ImageView) findViewById(R.id.iv_ex4);
        iv_ex5 = (ImageView) findViewById(R.id.iv_ex5);
        iv_ex6 = (ImageView) findViewById(R.id.iv_ex6);
        iv_ex1.setOnClickListener(listner_exList);
        iv_ex2.setOnClickListener(listner_exList);
        iv_ex3.setOnClickListener(listner_exList);
        iv_ex4.setOnClickListener(listner_comingsoon);
        iv_ex5.setOnClickListener(listner_comingsoon);
        iv_ex6.setOnClickListener(listner_comingsoon);

    }

    View.OnClickListener listner_exList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_ex1:
                    exType = 1;
                    break;
                case R.id.iv_ex2:
                    exType = 2;
                    break;
                case R.id.iv_ex3:
                    exType = 3;
                    break;
                default:
                    break;
            }
            ExStartPopup popup = new ExStartPopup(ExListActivity.this, exType, new ExStartPopup.PopupEventListener() {
                @Override
                public void popupEvent(String result, int difficulty) {
                    // 횟수 입력되었으면 운동 프리뷰 액티비티로 넘어가기
                    if (!result.equals("Cancel")) {
//                        Toast.makeText(ExListActivity.this, "count: "+result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ExListActivity.this, ExPreviewActivity.class);
                        int exCount = Integer.parseInt(result);

                        intent.putExtra("exCount",exCount);
                        intent.putExtra("exType",exType);
                        intent.putExtra("exDifficulty",difficulty);

                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    };

    View.OnClickListener listner_comingsoon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "추후에 업데이트 될 예정입니다!", Toast.LENGTH_SHORT).show();
        }
    };

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
                if (!SharedPreferenceData.getAutologinChecked(this)) {
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

        viewLayout.setOnClickListener(v -> {
           closeMenu();
        });

        sidebar.setEventListener(new UserSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                closeMenu();
            }

            @Override
            public void btnHome() {
                isMenuShow = false;
                Intent intent = new Intent(context, UserMainMenuActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnTraining() {
                isMenuShow = false;
                Intent intent = new Intent(context, ExHistoryActivity.class);
                startActivity(intent);
                finish();
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
                isMenuShow = false;
                Intent intent = new Intent(context, UserBookmarkActivity.class);
                startActivity(intent);
                finish();
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
            viewLayout.setOnTouchListener((v, event) -> false);

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
        mainLayout.setEnabled(false);
        mainLayout.setClickable(false);
    }
}
