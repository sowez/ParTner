package com.example;

import android.content.Intent;
//import android.support.appcompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

public class ExListActivity extends AppCompatActivity {

    private Button btn_ex1, btn_ex2, btn_ex3;
    int exType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_list);

        btn_ex1 = (Button)findViewById(R.id.btn_ex1);
        btn_ex2 = (Button)findViewById(R.id.btn_ex2);
        btn_ex3 = (Button)findViewById(R.id.btn_ex3);
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
            ExStartPopup popup = new ExStartPopup(ExListActivity.this, exType, new ExStartPopup.PopupEventListener() {
                @Override
                public void popupEvent(String result) {
                    // 횟수 입력되었으면 운동 프리뷰 액티비티로 넘어가기
                    if (!result.equals("Cancel")){
//                        Toast.makeText(ExListActivity.this, "count: "+result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ExListActivity.this,ExPreviewActivity.class);
                        int exCount = Integer.parseInt(result);
                        intent.putExtra("exCount",exCount);
                        intent.putExtra("exType",exType);
                        startActivity(intent);
                    }
                }
            });
        }
    };
}
