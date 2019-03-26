package com.example.partner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ExListActivity extends AppCompatActivity {

    private Button btn_ex1, btn_ex2, btn_ex3;
    int exType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_list);

        btn_ex1 = findViewById(R.id.btn_ex1);
        btn_ex2 = findViewById(R.id.btn_ex2);
        btn_ex3 = findViewById(R.id.btn_ex3);
        btn_ex1.setOnClickListener(listner_exList);
        btn_ex2.setOnClickListener(listner_exList);
        btn_ex3.setOnClickListener(listner_exList);
    }

    View.OnClickListener listner_exList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_ex1:
                    exType = 1;
                    break;
                case R.id.btn_ex2:
                    exType = 2;
                    break;
                case R.id.btn_ex3:
                    exType = 3;
                    break;
                    default:break;
            }
            exStartPopup popup = new exStartPopup(ExListActivity.this, exType, new exStartPopup.PopupEventListener() {
                @Override
                public void popupEvent(String exCount) {
                    // 횟수 입력되었으면 운동 프리뷰 액티비티로 넘어가기
                    if (!exCount.equals("Cancel")){
                        Toast.makeText(ExListActivity.this, "count: "+exCount, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ExListActivity.this,ExPreviewActivity.class);
                        int count = Integer.parseInt(exCount);
                        intent.putExtra("count",count);
                        intent.putExtra("exType",exType);
                        startActivity(intent);
                    }
                }
            });
        }
    };
}
