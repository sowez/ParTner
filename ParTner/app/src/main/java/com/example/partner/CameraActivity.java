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
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

/** Main {@code Activity} class for the Camera app. */
public class CameraActivity extends Activity{

  public static boolean isOpenCVInit = false;

  /** Tag for the {@link Log}. */
  private static final String TAG = "butcher2";

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
    if (null == savedInstanceState) {
        getFragmentManager()
          .beginTransaction()
          .replace(R.id.container, Camera2BasicFragment.newInstance())
          .commit();
    }
  }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        setContentView(R.layout.activity_camera);
//        super.onConfigurationChanged(newConfig);
//    }


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
