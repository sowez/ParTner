package com.example.partner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;

public class ExStartPopup {

    public interface PopupEventListener {
        public void popupEvent(String exCount);
    }

    private PopupEventListener listener;
    private Context context;

    public ExStartPopup(Context context, int exType, PopupEventListener popupEventListener){
        this.context = context;
        this.listener = popupEventListener;
        callFunction(exType);
    }

    public void callFunction(final int exType) {

        // 운동 종류에 따라 dialog 다르게 설정
        String exName = "Exercise";
        String countType = "회";
        switch (exType){
            case 1:
                exName = "플랭크";
                countType = "초";
                break;
            case 2:
                exName = "스쿼트";
                countType = "회";
                break;
            case 3:
                exName = "점핑잭";
                countType = "회";
                break;
            default: break;
        }

        final Dialog dig = new Dialog(context);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dig.setContentView(R.layout.popup_set_ex_count);
        dig.show();

        final TextView tv_title = dig.findViewById(R.id.tv_exType);
        final TextView tv_countType = dig.findViewById(R.id.tv_countType);
        final EditText et_count = dig.findViewById(R.id.et_exCount);
        final Button btn_exCancel = dig.findViewById(R.id.btn_exCancel);
        final Button btn_exStart = dig.findViewById(R.id.btn_exStartonPopup);

        tv_title.setText(exName);
        tv_countType.setText(countType);

        btn_exCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.popupEvent("Cancel");
                dig.dismiss();
            }
        });

        btn_exStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = et_count.getText().toString();
                if(count.isEmpty())
                    Toast.makeText(context, "숫자를 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    listener.popupEvent(count);
                    dig.dismiss();
                }
            }
        });
    }
}
