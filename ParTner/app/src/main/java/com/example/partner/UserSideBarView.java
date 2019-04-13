package com.example.partner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class UserSideBarView extends RelativeLayout implements View.OnClickListener {

    public EventListener listener;

    public void setEventListener(EventListener eventListener) {
        this.listener = eventListener;
    }

    public interface EventListener {
        void btnCancel();

        void btnMyPage();

        void btnTraining();

        void btnCall();

        void btnBookmark();

        void btnLogout();

        void btnSetting();

    }

    public UserSideBarView(Context context) {
        this(context, null);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.user_sidebar, this, true);
        findViewById(R.id.user_side_cancel).setOnClickListener(this);
        findViewById(R.id.user_side_mypage).setOnClickListener(this);
        findViewById(R.id.user_side_training).setOnClickListener(this);
        findViewById(R.id.user_side_call).setOnClickListener(this);
        findViewById(R.id.user_side_bookmark).setOnClickListener(this);
        findViewById(R.id.user_side_logout).setOnClickListener(this);
        findViewById(R.id.user_side_setting).setOnClickListener(this);

    }
//user
    public UserSideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_side_cancel:
                listener.btnCancel();
                break;
            case R.id.user_side_mypage:
                listener.btnMyPage();
                break;
            case R.id.user_side_training:
                listener.btnTraining();
                break;
            case R.id.user_side_call:
                listener.btnCall();
                break;
            case R.id.user_side_bookmark:
                listener.btnBookmark();
                break;
            case R.id.user_side_logout:
                listener.btnLogout();
                break;
            case R.id.user_side_setting:
                listener.btnSetting();
                break;
            default:
                Log.e("TAG", "onClick: error!");
                break;
        }
    }
}
