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
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerCallHistoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Context context = this;
    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TrainerCallHistoryRecyclerAdapter recyclerAdapter;

    private ImageButton menu_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_call_history);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mainLayout = (ViewGroup) findViewById(R.id.trainer_call_history);
        viewLayout = (ViewGroup) findViewById(R.id.trainer_fl_slide);
        sideLayout = (ViewGroup) findViewById(R.id.trainer_view_slidebar);
        addSidebar();

        menu_btn = (ImageButton) findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation())
        );
        recyclerView.setLayoutManager(linearLayoutManager);

//        Date from = new Date();
//        Date to = new Date();
//
//        // 통화 기록 데이터 추가
//        ArrayList<CallHistory> callHistories = new ArrayList<>();
//        callHistories.add(new CallHistory("도경수", "강동원", from, to, new Integer(33)));
//        callHistories.add(new CallHistory("도경수", "이혁재", from, to, new Integer(34)));
//        callHistories.add(new CallHistory("도경수", "이승기", from, to, new Integer(35)));
//        callHistories.add(new CallHistory("도경수", "이홍기", from, to, new Integer(36)));

        ServerComm serverComm = new ServerComm();
        RetrofitCommnunication retrofitCommnunication = serverComm.init();

        Call<List<CallHistory>> callHistory = retrofitCommnunication.getCallHistory(SharedPreferenceData.getId(this), SharedPreferenceData.getType(this));

        callHistory.enqueue(new Callback<List<CallHistory>>() {
            @Override
            public void onResponse(Call<List<CallHistory>> call, Response<List<CallHistory>> response) {
                List<CallHistory> callHistories = response.body();

                recyclerAdapter = new TrainerCallHistoryRecyclerAdapter(callHistories);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onFailure(Call<List<CallHistory>> call, Throwable t) {
                Toast.makeText(TrainerCallHistoryActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", "onFailure: " + t.getMessage() );
            }
        });



    }

    private void setRecyclerView() {


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!SharedPreferenceData.getAutologinChecked(this)) {
            SharedPreferenceData.clearUserData(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!SharedPreferenceData.getAutologinChecked(this)) {
            SharedPreferenceData.clearUserData(this);
        }
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
                finish();
            } else {
                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> isExitFlag = false, 2000);
            }
        }

    }

    public void addSidebar() {

        TrainerSideBarView sidebar = new TrainerSideBarView(this);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(view -> {
            closeMenu();
        });

        sidebar.setEventListener(new TrainerSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                Log.d("TAG", "btnCancel");
                closeMenu();
            }

            @Override
            public void btnHome() {
                isMenuShow = false;
                Intent intent = new Intent(context, TrainerMainMenuActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnCall() {
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
        }, 450);
    }

    public void showMenu() {
        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.setVisibility(View.VISIBLE);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
        mainLayout.setClickable(false);
    }
}
