package com.example.partner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private Spinner trainSpinner;
    private Spinner genderSpinner;
    private EditText trainerNameEdittext;
    private Button searchBtn;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TrainerListRecyclerAdapter recyclerAdapter;

    private String URL = "http://192.168.43.53:8000/";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

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
        // 버튼 누르면 검색.. 구현
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitCommnunication retrofitCommnunication = retrofit.create(RetrofitCommnunication.class);

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
            }
        });

    }
}
