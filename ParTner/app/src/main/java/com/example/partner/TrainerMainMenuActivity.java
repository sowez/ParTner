package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerMainMenuActivity extends AppCompatActivity {

    private Context context = this;

    private String TAG = "TAG";
    private Toolbar mToolbar;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    private ArrayList<String> traintypeArrayList = new ArrayList<String>();

    // 트레이너 프로필 정보
    private RatingBar mRating;
    private ImageView editImage;
    private ImageButton menu_btn;
    private TextView name;
    private TextView selfIntroduction;
    private TextView trainingType;
    private TextView gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_menu);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mainLayout = (ViewGroup) findViewById(R.id.id_trainer_menu);
        viewLayout = (ViewGroup) findViewById(R.id.trainer_fl_slide);
        sideLayout = (ViewGroup) findViewById(R.id.trainer_view_slidebar);

        menu_btn = (ImageButton) findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        addSidebar();

        mRating = findViewById(R.id.ratingbar);
        editImage = findViewById(R.id.edit);
        name = findViewById(R.id.name);
        selfIntroduction = findViewById(R.id.self_introduction);
        trainingType = findViewById(R.id.training_type);
        gender = findViewById(R.id.sex);


//        //RatingBar ratingBar, float rating, boolean fromUser
//        mRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
//
//        });


        editImage.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TrainerProfileEditActivity.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("intro", selfIntroduction.getText().toString());
            intent.putStringArrayListExtra("traintype", traintypeArrayList);
            intent.putExtra("gender", gender.getText().toString());
            startActivity(intent);
        });

        getProfile();

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
        });

        sidebar.setEventListener(new TrainerSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                Log.d("TAG", "btnCancel");
                closeMenu();
            }

            @Override
            public void btnHome() {
                Log.d("TAG", "btnHome");
                closeMenu();
            }

            @Override
            public void btnCall() {
                Log.d(TAG, "btnLevel btncall");
                isMenuShow = false;
                Intent intent = new Intent(context, TrainerCallHistoryActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void btnLogout() {
                Log.d(TAG, "btnLevel btnlogout");
                SharedPreferenceData.clearUserData(context);
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                isMenuShow = false;
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnSetting() {
                Log.d(TAG, "btnLevel btnsetting");
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
        Log.e("TAG", "메뉴버튼 클릭");
    }

    // GET profile
    public void getProfile() {
        String id = SharedPreferenceData.getId(this);

        Log.i("UserID", id);

        RetrofitCommnunication retrofitCommnunication = new ServerComm().init();

        Call<TrainerProfile> trianerProfile = retrofitCommnunication.trainerProfile(id);

        trianerProfile.enqueue(new Callback<TrainerProfile>() {
            @Override
            public void onResponse(Call<TrainerProfile> call, Response<TrainerProfile> response) {
                TrainerProfile trainerProfile = response.body();

                Log.i("Received", trainerProfile.getName());
                Log.i("Received", trainerProfile.getSelf_introduction());
                Log.i("Received", trainerProfile.getStar_rate().toString());

                String traintypeList = "";
                for(int i=0; i<trainerProfile.getTraining_type().size(); i++){
                    traintypeList = traintypeList + trainerProfile.getTraining_type().get(i) + " ";
                    traintypeArrayList.add(trainerProfile.getTraining_type().get(i));
                }
                name.setText(trainerProfile.getName());
                selfIntroduction.setText(trainerProfile.getSelf_introduction());
                trainingType.setText(traintypeList);
                gender.setText(trainerProfile.getSex());
                mRating.setRating(trainerProfile.getStar_rate());
            }

            @Override
            public void onFailure(Call<TrainerProfile> call, Throwable t) {
                Toast.makeText(TrainerMainMenuActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", "onFailure: " + t.getMessage() );
            }
        });

    }

}
