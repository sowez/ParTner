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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private String trainType;
    private String gender;
    private String name;

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
        String[] trainTypes = getResources().getStringArray(R.array.traintype);
        ArrayAdapter<String> trainAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, trainTypes);
        trainAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        trainSpinner.setAdapter(trainAdapter);

        // 성별 스피너
        String[] genderTypes = getResources().getStringArray(R.array.gendertype);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, genderTypes);
        genderAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        trainType = trainSpinner.getSelectedItem().toString();
//        if(trainType.equals("all")) trainType = null;
        gender = genderSpinner.getSelectedItem().toString();
//        if(gender.equals("all")) gender = null;
        name = trainerNameEdittext.getText().toString();
        if (name.equals("")) name = "all";


        search(name, trainType, gender);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainType2 = trainSpinner.getSelectedItem().toString();
//              if(trainType.equals("all")) trainType = null;
                String gender2 = genderSpinner.getSelectedItem().toString();
//              if(gender.equals("all")) gender = null;
                String name2 = trainerNameEdittext.getText().toString();
                if (name2.equals("")) name2 = "all";

                search(name2, trainType2, gender2);
            }
        });

    }

    private void search(String name, String trainType, String gender) {

        RetrofitCommnunication retrofitCommnunication = new ServerComm().init();

        retrofitCommnunication.trainerList(SharedPreferenceData.getId(this), name, trainType, gender).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                JsonObject res = response.body();
                Log.d("bookmarkchecked", "onResponse: " + res);
                List<String> bookmark_trainers = gson.fromJson(res.get("bookmark"), new TypeToken<List<String>>() {
                }.getType());
                List<TrainerProfile> trainerProfiles = gson.fromJson(res.get("trainerProfiles"), new TypeToken<List<TrainerProfile>>() {
                }.getType());

                for (int i = 0; i < trainerProfiles.size(); i++) {
                    for (int j = 0; j < bookmark_trainers.size(); j++) {
                        if (trainerProfiles.get(i).get_id().equals(bookmark_trainers.get(j))) {
                            Log.d("bookmarkchecked", "onResponse: trainer bookmark >> " + bookmark_trainers.get(j));
                            trainerProfiles.get(i).setBookmarked(true);
                            break;
                        }
                    }
                }
                recyclerAdapter = new TrainerListRecyclerAdapter(trainerProfiles);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(TrainerListActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        search(name, trainType, gender);

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


        viewLayout.setOnClickListener(view -> {
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
