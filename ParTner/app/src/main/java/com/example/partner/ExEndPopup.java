package com.example.partner;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;

public class ExEndPopup {

    public interface PopupEventListener {
        public void popupEvent(String exCount);
    }

    private PopupEventListener listener;
    private Context context;

    public ExEndPopup(Context context, int exType,int exCount, PopupEventListener popupEventListener){
        this.context = context;
        this.listener = popupEventListener;
        callFunction(exType, exCount);
    }

    public void callFunction(final int exType, final int exCount) {

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
        dig.setContentView(R.layout.popup_set_done_ex_count);
        dig.show();

        final TextView tv_title = dig.findViewById(R.id.tv_exType);
        final TextView tv_done_ex_count = dig.findViewById(R.id.tv_done_ex_count);
        final TextView tv_done_ex_countType = dig.findViewById(R.id.tv_done_ex_countType);
        final Button btn_exEnd_exSelect = dig.findViewById(R.id.btn_exEnd_exSelect);
        final Button btn_exEnd_Cal = dig.findViewById(R.id.btn_exEnd_Cal);

        tv_title.setText(exName);
        tv_done_ex_count.setText(""+exCount);
        tv_done_ex_countType.setText(countType);

        btn_exEnd_exSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.popupEvent("selectEx");
                dig.dismiss();
            }
        });

        btn_exEnd_Cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.popupEvent("goCalander");
                dig.dismiss();
            }
        });
    }
}
