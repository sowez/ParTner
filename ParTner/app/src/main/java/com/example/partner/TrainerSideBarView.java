package com.example.partner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class TrainerSideBarView extends RelativeLayout implements View.OnClickListener {

    public EventListener listener;

    public void setEventListener(EventListener eventListener) {
        this.listener = eventListener;
    }

    public interface EventListener {
        void btnCancel();

        void btnCall();

        void btnLogout();

        void btnSetting();

    }

    public TrainerSideBarView(Context context) {
        this(context, null);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.trainer_sidebar, this, true);
        findViewById(R.id.trainer_side_cancel).setOnClickListener(this);
        findViewById(R.id.trainer_side_call).setOnClickListener(this);
        findViewById(R.id.trainer_side_logout).setOnClickListener(this);
        findViewById(R.id.trainer_side_setting).setOnClickListener(this);

    }

    public TrainerSideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trainer_side_cancel:
                listener.btnCancel();
                break;
            case R.id.trainer_side_call:
                listener.btnCall();
                break;
            case R.id.trainer_side_logout:
                listener.btnLogout();
                break;
            case R.id.trainer_side_setting:
                listener.btnSetting();
                break;

            default:
                Log.d("TAG", "onClick: error!");
                break;
        }
    }
}
