package com.example.partner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import java.util.ArrayList;

public class TrainerListActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Spinner spinner;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TrainerListRecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        // 검색을 위한 spinner 설정
        spinner = (Spinner) findViewById(R.id.spinner);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation())
        );
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<TrainerProfile> trainerProfiles = new ArrayList<>();
        String[] a = {"요가", "필라테스"};
        String[] b = {"스트레칭", "근력"};
        String[] c = {"스트레칭", "요가"};
        String[] d = {"근력"};
        trainerProfiles.add(new TrainerProfile("여성", "김모씨", "안녕하세요", 3, a));
        trainerProfiles.add(new TrainerProfile("여성", "이모씨", "안녕하세요", 4, b));
        trainerProfiles.add(new TrainerProfile("남성", "박모씨", "안녕하세요", 2, c));
        trainerProfiles.add(new TrainerProfile("남성", "최모씨", "안녕하세요", 1, d));
        trainerProfiles.add(new TrainerProfile("여성", "김모씨", "안녕하세요", 3, a));
        trainerProfiles.add(new TrainerProfile("여성", "이모씨", "안녕하세요", 4, b));
        trainerProfiles.add(new TrainerProfile("남성", "박모씨", "안녕하세요", 2, c));
        trainerProfiles.add(new TrainerProfile("남성", "최모씨", "안녕하세요", 1, d));
        trainerProfiles.add(new TrainerProfile("여성", "김모씨", "안녕하세요", 3, a));
        trainerProfiles.add(new TrainerProfile("여성", "이모씨", "안녕하세요", 4, b));
        trainerProfiles.add(new TrainerProfile("남성", "박모씨", "안녕하세요", 2, c));
        trainerProfiles.add(new TrainerProfile("남성", "최모씨", "안녕하세요", 1, d));
        trainerProfiles.add(new TrainerProfile("여성", "김모씨", "안녕하세요", 3, a));
        trainerProfiles.add(new TrainerProfile("여성", "이모씨", "안녕하세요", 4, b));
        trainerProfiles.add(new TrainerProfile("남성", "박모씨", "안녕하세요", 2, c));
        trainerProfiles.add(new TrainerProfile("남성", "최모씨", "안녕하세요", 1, d));



        recyclerAdapter = new TrainerListRecyclerAdapter(trainerProfiles);
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void setRecyclerView() {



    }
}
