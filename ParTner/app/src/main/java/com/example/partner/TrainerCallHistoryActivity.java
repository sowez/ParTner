package com.example.partner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

public class TrainerCallHistoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TrainerCallHistoryRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_call_history);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation())
        );
        recyclerView.setLayoutManager(linearLayoutManager);

        Date from = new Date();
        Date to = new Date();

        // 통화 기록 데이터 추가
        ArrayList<CallHistory> callHistories = new ArrayList<>();
        callHistories.add(new CallHistory(from, to, new Integer(33), "김수진", "코코"));
        callHistories.add(new CallHistory(from, to, new Integer(34), "김수진", "코코"));
        callHistories.add(new CallHistory(from, to, new Integer(35), "김수진", "코코"));
        callHistories.add(new CallHistory(from, to, new Integer(36), "김수진", "코코"));

        recyclerAdapter = new TrainerCallHistoryRecyclerAdapter(callHistories);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setRecyclerView() {



    }
}
