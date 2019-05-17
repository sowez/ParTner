package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrainerListActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Context context = this;
    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    private ImageButton menu_btn;

    private Spinner trainSpinner;
    private Spinner genderSpinner;
    private EditText trainerNameEdittext;
    private Button searchBtn;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TrainerListRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mainLayout = (ViewGroup) findViewById(R.id.id_trainer_list);
        viewLayout = (ViewGroup) findViewById(R.id.user_fl_slide);
        sideLayout = (ViewGroup) findViewById(R.id.user_view_slidebar);

        menu_btn = (ImageButton) findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        addSideBar();


        // 검색을 위한 spinner 설정
        trainSpinner = (Spinner) findViewById(R.id.training_type);
        genderSpinner = (Spinner) findViewById(R.id.gender_type);
        trainerNameEdittext = (EditText) findViewById(R.id.trainer_name);
        searchBtn = (Button) findViewById(R.id.trainer_search_btn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation())
        );
        recyclerView.setLayoutManager(linearLayoutManager);

        // 트레이닝 타입 스피너
        String[] trainTypes  = getResources().getStringArray(R.array.traintype);
        ArrayAdapter<String> trainAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, trainTypes);
        trainAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        trainSpinner.setAdapter(trainAdapter);

        // 성별 스피너
        String[] genderTypes  = getResources().getStringArray(R.array.gendertype);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, genderTypes);
        genderAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        String trainType = trainSpinner.getSelectedItem().toString();
//        if(trainType.equals("all")) trainType = null;
        String gender = genderSpinner.getSelectedItem().toString();
//        if(gender.equals("all")) gender = null;
         String name = trainerNameEdittext.getText().toString();
        if(name.equals("")) name = "all";


        search(name, trainType, gender);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainType2 = trainSpinner.getSelectedItem().toString();
//              if(trainType.equals("all")) trainType = null;
                String gender2 = genderSpinner.getSelectedItem().toString();
//              if(gender.equals("all")) gender = null;
                String name2 = trainerNameEdittext.getText().toString();
                if(name2.equals("")) name2 = "all";

                search(name2, trainType2, gender2);
            }
        });
//        Log.i("Received msg",trainerProfiles.get(0).getId());
//        Log.i("Received msg",trainerProfiles.get(1).getId());

//        List<TrainerProfile> trainerProfiles2 = new ArrayList<>();
//        ArrayList list = new ArrayList<String>();
//        list.add("요가");
//        list.add("필라테스");
//        trainerProfiles2.add(new TrainerProfile("1", "female", "name1","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("2", "female", "name2","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("3", "female", "name3","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("4", "female", "name4","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("5", "female", "name5","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("6", "female", "name6","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("7", "female", "name7","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("8", "female", "name8","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("9", "female", "name9","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("10", "female", "name10","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("11", "female", "name11","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("12", "female", "name12","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("13", "female", "name13","intro", 5, list,"online", "img1"));
//        trainerProfiles2.add(new TrainerProfile("14", "female", "name14","intro", 5, list,"online", "img1"));
//        recyclerAdapter = new TrainerListRecyclerAdapter(trainerProfiles2);


    }



    private void search(String name, String trainType, String gender) {

        RetrofitCommnunication retrofitCommnunication = new ServerComm().init();

        Call<List<TrainerProfile>> trainerProfiles = retrofitCommnunication.trainerList(name, trainType, gender);

        trainerProfiles.enqueue(new Callback<List<TrainerProfile>>() {
            @Override
            public void onResponse(Call<List<TrainerProfile>> call, Response<List<TrainerProfile>> response) {
                List<TrainerProfile> trainerProfiles = response.body();

                recyclerAdapter = new TrainerListRecyclerAdapter(trainerProfiles);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onFailure(Call<List<TrainerProfile>> call, Throwable t) {
                Toast.makeText(TrainerListActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", "onFailure: " + t.getMessage() );
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(!SharedPreferenceData.getAutologinChecked(this)){
            SharedPreferenceData.clearUserData(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!SharedPreferenceData.getAutologinChecked(this)){
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

    private void addSideBar() {

        UserSideBarView sidebar = new UserSideBarView(context);
        sideLayout.addView(sidebar);


        viewLayout.setOnClickListener(view -> {
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
                Toast.makeText(context, "Training 화면", Toast.LENGTH_LONG).show();
                closeMenu();
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
                Toast.makeText(context, "Trainer bookmark 화면", Toast.LENGTH_LONG).show();
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
        Log.e("TAG", "메뉴버튼 클릭");
    }
}
