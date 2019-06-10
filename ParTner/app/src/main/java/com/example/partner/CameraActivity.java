/*
 * Copyright 2018 Zihua Zeng (edvard_hua@live.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.partner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import com.example.partner.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

/**
 * Main {@code Activity} class for the Camera app.
 */
public class CameraActivity extends Activity {

    static {
//        System.loadLibrary("opencv_java");
        System.loadLibrary("opencv_java3");
    }

    public static boolean isOpenCVInit = false;

    private int exType;
    private int exCount;
    private int exDifficulty;
    private Boolean isExitFlag = false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    isOpenCVInit = true;
                    break;
                case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:
                    break;
                case LoaderCallbackInterface.INIT_FAILED:
                    break;
                case LoaderCallbackInterface.INSTALL_CANCELED:
                    break;
                case LoaderCallbackInterface.MARKET_ERROR:
                    break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        exType = intent.getExtras().getInt("exType");
        exCount = intent.getExtras().getInt("exCount");
        exDifficulty = intent.getExtras().getInt("exDifficulty");

//        Toast.makeText(getApplicationContext(), "diff2: "+exDifficulty, Toast.LENGTH_SHORT).show();
        if (null == savedInstanceState) {
            Camera2BasicFragment c2bf = new Camera2BasicFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("exType", exType);
            bundle.putInt("exCount", exCount);
            bundle.putInt("exDifficulty", exDifficulty);
            c2bf.setArguments(bundle);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, c2bf)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

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
