package com.example.partner;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

public class ExPreviewActivity extends AppCompatActivity {

    TextView tv_title;
    VideoView vv_preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_preview);

        Intent intent = getIntent();
        int exType = intent.getExtras().getInt("exType");
        int count = intent.getExtras().getInt("count");

        tv_title = findViewById(R.id.tv_exPreview);
        vv_preview = findViewById(R.id.vv_exPreview);
        setExPreview(exType,tv_title,vv_preview);
    }

    private void setExPreview(int exType, TextView tv, VideoView vv){
        String exName = "Exercise";
        Uri video;
        switch (exType){
            case 1:
                exName = "플랭크";
                video = Uri.parse("android.resource://" + getPackageName()+ "/"+R.raw.flank_preview);
                vv.setVideoURI(video);
                break;
            case 2:
                exName = "스쿼트";
                video = Uri.parse("android.resource://" + getPackageName()+ "/"+R.raw.flank_preview);
                vv.setVideoURI(video);
                break;
            case 3:
                exName = "점핑잭";
                video = Uri.parse("android.resource://" + getPackageName()+ "/"+R.raw.flank_preview);
                vv.setVideoURI(video);
                break;
                default: break;
        }
        tv.setText(exName);
        vv.requestFocus();
        vv.start();
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }
}
