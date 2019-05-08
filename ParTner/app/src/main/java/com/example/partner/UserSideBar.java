package com.example.partner;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class UserSideBar {
    private String TAG = "TAG";

    private Context context;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    public UserSideBar(Context context){
        this.context = context;
        this.mainLayout = mainLayout.findViewById(R.id.id_main);
        this.viewLayout = viewLayout.findViewById(R.id.fl_slide);
        this.sideLayout = sideLayout.findViewById(R.id.view_slidebar);
    }

    private void addSideView() {

        UserSideBarView sidebar = new UserSideBarView(context);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(view -> {
        });

        sidebar.setEventListener(new UserSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                Log.d("TAG", "btnCancel");
                closeMenu();
            }

            @Override
            public void btnHome() {

            }

            @Override
            public void btnTraining() {
                Log.d(TAG, "btnLevel btnTraining");
                closeMenu();
            }

            @Override
            public void btnCall() {
                Log.d(TAG, "btnLevel btncall");
                closeMenu();
            }

            @Override
            public void btnBookmark() {
                Log.d(TAG, "btnLevel btn bookmark");
                closeMenu();
            }

            @Override
            public void btnLogout() {
                Log.d(TAG, "btnLevel btnlogout");
                closeMenu();
            }

            @Override
            public void btnSetting() {
                Log.d(TAG, "btnLevelbtnsetting");
                closeMenu();
            }
        });
    }

    public void closeMenu() {

        Animation slide = AnimationUtils.loadAnimation(context, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(() -> {
            viewLayout.setVisibility(View.GONE);
            viewLayout.setEnabled(false);
            //mainLayout.setEnabled(true);
        }, 450);
    }

    public void showMenu() {

        Animation slide = AnimationUtils.loadAnimation(context, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        //mainLayout.setEnabled(false);
        Log.e("TAG", "메뉴버튼 클릭");
    }
}
