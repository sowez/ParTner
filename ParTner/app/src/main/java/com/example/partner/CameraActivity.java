/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.partner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import androidx.fragment.app.Fragment;

/** Main {@code Activity} class for the Camera app. */
public class CameraActivity extends Activity{

    public static boolean isOpenCVInit = false;
    private Button btn_endEx;

    private int exType;
    private int exCount;

    /** Tag for the {@link Log}. */
    private static final String TAG = "ParTner";

    public static void init() {
        System.loadLibrary("opencv_java3");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    {
                        isOpenCVInit = true;
                    } break;

                default:
                    {
                        super.onManagerConnected(status);
                    } break;
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

//        btn_endEx = findViewById(R.id.btn_endEx);
////        btn_endEx.setOnClickListener(listner_exEnd);

        if (null == savedInstanceState) {
//            fragment에 값 넘겨주기
            Camera2BasicFragment c2bf = new Camera2BasicFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("exType", exType);
            bundle.putInt("exCount", exCount);
            c2bf.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, c2bf)
                    .commit();
        }




    }

    @Override
  public void onResume()
  {
      super.onResume();
      if (!OpenCVLoader.initDebug()) {
          OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
      } else {
          mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
      }
  }




}
