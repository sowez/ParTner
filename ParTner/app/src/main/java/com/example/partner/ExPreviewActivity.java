package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.partner.R;

public class ExPreviewActivity extends AppCompatActivity {

    TextView tv_title;
    VideoView vv_preview;
    Button btn_exStart;

    // side bar

    private Toolbar mToolbar;
    private Context context = ExPreviewActivity.this;
    private ViewGroup mainLayout;
    private ViewGroup viewLayout;
    private ViewGroup sideLayout;
    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    private ImageButton menu_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_preview);

        // side bar

        mToolbar = (Toolbar) findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mainLayout = (ViewGroup) findViewById(R.id.id_ex_preview);
        viewLayout = (ViewGroup) findViewById(R.id.user_fl_slide);
        sideLayout = (ViewGroup) findViewById(R.id.user_view_slidebar);

        menu_btn = (ImageButton) findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        addSideBar();

        Intent intent = getIntent();
        int exType = intent.getExtras().getInt("exType");
        int exCount = intent.getExtras().getInt("exCount");

        tv_title = (TextView) findViewById(R.id.tv_exPreview);
        vv_preview = (VideoView) findViewById(R.id.vv_exPreview);

        setExPreview(exType,tv_title,vv_preview);

        btn_exStart = (Button) findViewById(R.id.btn_exStartonPreview);
        btn_exStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExPreviewActivity.this,CameraActivity.class);
//                Intent i = new Intent(ExPreviewActivity.this,ExHistoryActivity.class);
                i.putExtra("exCount",exCount);
                i.putExtra("exType",exType);
                startActivity(i);
                finish();
            }
        });

    }

    private void setExPreview(int exType, TextView tv, VideoView vv){
        String exName = "Exercise";
        Uri video;
        switch (exType){
            case 1:
                exName = "플랭크";
                video = Uri.parse("android.resource://" + getPackageName()+ "/"+R.raw.squat_preview);
                vv.setVideoURI(video);
                break;
            case 2:
                exName = "스쿼트";
                video = Uri.parse("android.resource://" + getPackageName()+ "/"+R.raw.squat_preview);
                vv.setVideoURI(video);
                break;
            case 3:
                exName = "점핑잭";
                video = Uri.parse("android.resource://" + getPackageName()+ "/"+R.raw.jumpingjack_preview);
                vv.setVideoURI(video);
                break;
                default: break;
        }
        tv.setText(exName);
        vv.requestFocus();
        vv.start();
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
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
        viewLayout.setOnTouchListener((v, event) -> true);
        mainLayout.setOnTouchListener((v, event) -> true);

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
                Toast.makeText(context, "Trainer bookmark", Toast.LENGTH_LONG).show();
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
            viewLayout.setOnTouchListener((v,event)->false);

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
        mainLayout.setOnTouchListener((v, event) -> true);

        viewLayout.setOnClickListener(v -> {
        });
        viewLayout.setOnTouchListener((v,event)->true);
        Log.e("TAG", "메뉴버튼 클릭");
    }
}
