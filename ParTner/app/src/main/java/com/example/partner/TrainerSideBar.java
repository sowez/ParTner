package com.example.partner;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class TrainerSideBar {

    private String TAG = "TAG";

    private Context context;
    private boolean isMenuShow;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    public TrainerSideBar(Context context){
        this.context = context;
        this.isMenuShow = false;
        this.mainLayout = mainLayout.findViewById(R.id.id_trainer_menu);
        this.viewLayout = viewLayout.findViewById(R.id.trainer_fl_slide);
        this.sideLayout = sideLayout.findViewById(R.id.trainer_view_slidebar);
    }

    public void addSidebarView() {

        TrainerSideBarView sidebar = new TrainerSideBarView(context);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(view -> {
        });

        sidebar.setEventListener(new TrainerSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                Log.d("TAG", "btnCancel");
                closeMenu();
            }

            @Override
            public void btnHome() {

            }

            @Override
            public void btnCall() {
                Log.d(TAG, "btnLevel btncall");
                closeMenu();
            }

            @Override
            public void btnLogout() {
                Log.d(TAG, "btnLevel btnlogout");
                closeMenu();
            }

            @Override
            public void btnSetting() {
                Log.d(TAG, "btnLevel btnsetting");
                closeMenu();
            }
        });
    }

    public boolean closeMenu() {
        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(context, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(() -> {
            viewLayout.setVisibility(View.GONE);
            viewLayout.setEnabled(false);
            mainLayout.setEnabled(true);
        }, 450);
        return isMenuShow;
    }

    public boolean showMenu() {
        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(context, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
        Log.e("TAG", "메뉴버튼 클릭");
        return isMenuShow;
    }
}
