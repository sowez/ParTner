package com.example.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class TrainerMainMenuActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RatingBar mRating;
    private ImageView editImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_menu);

        // Toolbar 설정
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        mRating = (RatingBar) findViewById(R.id.ratingbar);
        editImage = (ImageView) findViewById(R.id.edit);

        //RatingBar ratingBar, float rating, boolean fromUser
        mRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {

        });

        editImage.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TrainerProfileEditActivity.class);
            startActivity(intent);
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

}
