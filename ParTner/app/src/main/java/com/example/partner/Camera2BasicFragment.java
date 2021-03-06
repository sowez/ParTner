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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.ImageReader;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.legacy.app.FragmentCompat;

import com.example.partner.R;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import static android.speech.tts.TextToSpeech.ERROR;

/**
 * Basic fragments for the Camera.
 */
public class Camera2BasicFragment extends Fragment
        implements FragmentCompat.OnRequestPermissionsResultCallback, TextToSpeech.OnInitListener{

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "ParTner";

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final String HANDLE_THREAD_NAME = "CameraBackground";

    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private final Object lock = new Object();
    private boolean runClassifier = false;
    private boolean checkedPermissions = false;
    private AutoFitTextureView textureView;
    private AutoFitFrameLayout layoutFrame;
    private ExCorrection exCorrection;
    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private DrawView drawView;
    private PoseEstimation classifier;
    private ImageView personImg;
    private Exercise exercise;
    private ViewGroup layoutBottom;
//    private ImageClassifier classifier;
    private int nowHeight;
    private int nowWidth;

    private Button btn_endEx;
    private ImageView btn_changeView;
    private String mCameraFacing="1";

    private String start_time;
    private int exType;
    private int exCount;
    private int exDifficulty;

    private int img_red;
    private int img_green;

    private int readyCounter = 0;
    private int exerciseCounter = 0;
    private int resetStepCounter = 0;
    private int exerciseStep = 0;
    private int endStep = 0;
    public static final int READY_BOUND = 15;
    public static final int RESET_STEP_BOUND = 70;
    public TextToSpeech tts;
    private ArrayList<Integer> errorHistory = new ArrayList<>();
    private double exAccuracy;
    private int[] exResult = {0,0};
    private boolean readyTts = false;
    private boolean exTts = false;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a {@link
     * TextureView}.
     */
    private final TextureView.SurfaceTextureListener surfaceTextureListener =
            new TextureView.SurfaceTextureListener() {

                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
                    openCamera(width, height);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture texture) {
                }
            };

    /**
     * ID of the current {@link CameraDevice}.
     */
    private String cameraId;

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession captureSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice cameraDevice;

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size previewSize;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback stateCallback =
            new CameraDevice.StateCallback() {

                @Override
                public void onOpened(@NonNull CameraDevice currentCameraDevice) {
                    // This method is called when the camera is opened.  We start camera preview here.
                    cameraOpenCloseLock.release();
                    cameraDevice = currentCameraDevice;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice currentCameraDevice) {
                    cameraOpenCloseLock.release();
                    currentCameraDevice.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice currentCameraDevice, int error) {
                    cameraOpenCloseLock.release();
                    currentCameraDevice.close();
                    cameraDevice = null;
                    Activity activity = getActivity();
                    if (null != activity) {
                        activity.finish();
                    }
                }
            };

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread backgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler backgroundHandler;

    /**
     * An {@link ImageReader} that handles image capture.
     */
    private ImageReader imageReader;

    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder previewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #previewRequestBuilder}
     */
    private CaptureRequest previewRequest;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore cameraOpenCloseLock = new Semaphore(1);

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to capture.
     */
    private CameraCaptureSession.CaptureCallback captureCallback =
            new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureProgressed(
                        @NonNull CameraCaptureSession session,
                        @NonNull CaptureRequest request,
                        @NonNull CaptureResult partialResult) {
                }

                @Override
                public void onCaptureCompleted(
                        @NonNull CameraCaptureSession session,
                        @NonNull CaptureRequest request,
                        @NonNull TotalCaptureResult result) {
                }
            };

    /**
     * Shows a {@link Toast} on the UI thread for the classification results.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(text);
                            drawView.invalidate();
                        }
                    });
        }
    }

    /**
     * Resizes image.
     * <p>
     * Attempting to use too large a preview size could  exceed the camera bus' bandwidth limitation,
     * resulting in gorgeous previews but the storage of garbage capture data.
     * <p>
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that is
     * at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size, and
     * whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(
            Size[] choices,
            int textureViewWidth,
            int textureViewHeight,
            int maxWidth,
            int maxHeight,
            Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth
                    && option.getHeight() <= maxHeight) {
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    public static Camera2BasicFragment newInstance() {
        return new Camera2BasicFragment();
    }

    /**
     * Layout the preview and buttons.
     */
    private void showToast2(String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            textView2.setText(text);
                            drawView.invalidate();
                        }
                    });
        }
    }

    private void showToast3(String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            textView3.setText(text);
                            drawView.invalidate();
                        }
                    });
        }
    }

    /** Layout the preview and buttons. */

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera2_basic, container, false);

        btn_endEx = v.findViewById(R.id.btn_endEx);
        btn_changeView = v.findViewById(R.id.btn_changeView);
        btn_endEx.setOnClickListener(listner_exEnd);
        btn_changeView.setOnClickListener(listner_changeView);

        personImg = v.findViewById(R.id.person_frame);

        exType = this.getArguments().getInt("exType");
        exCount = this.getArguments().getInt("exCount");
        exDifficulty = this.getArguments().getInt("exDifficulty");
        exCorrection = new ExCorrection();

        tts= new TextToSpeech(getActivity().getApplicationContext(),this);
        exCorrection = new ExCorrection();


        // 운동 종류에 따라 class, imgsrc 등 설정
        //exercise에 상속
        switch (exType){
            case 1:
                exercise = new Flank(exCount, exDifficulty);
                img_red = R.drawable.flank_red;
                img_green = R.drawable.flank_green;
                break;
            case 2:
                exercise = new Squat(exCount, exDifficulty);
                img_red = R.drawable.squat_red;
                img_green = R.drawable.squat_green;
                break;
            case 3:
                exercise = new JumpingJack(exCount, exDifficulty);
                img_red = R.drawable.jumping_red;
                img_green = R.drawable.jumping_green;
                break;
            default: break;
        }
        endStep = exercise.getSteps();
        personImg.setImageResource(img_red);
        start_time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        return v;
//        return inflater.inflate(R.layout.fragment_camera2_basic, container, false);
    }

    /**
     * Connect the buttons to their event handler.
     */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        textureView = view.findViewById(R.id.texture);
        textView = view.findViewById(R.id.text);
        textView2 = view.findViewById(R.id.text2);
        textView3 = view.findViewById(R.id.text3);

        layoutFrame = view.findViewById(R.id.layout_frame);
        drawView = view.findViewById(R.id.drawview);
        personImg = view.findViewById(R.id.person_frame);
        if (classifier != null)
            drawView.setImgSize(classifier.getImageSizeX(), classifier.getImageSizeY());
    }

    /**
     * Load the model and labels.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            // create either a new ImageClassifierQuantizedMobileNet or an PoseEstimationFloatInception
            classifier = new PoseEstimationFloatInception(getActivity());
            if (drawView != null)
                drawView.setImgSize(classifier.getImageSizeX(), classifier.getImageSizeY());
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize an image classifier.", e);
        }
        startBackgroundThread();
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        classifier.close();
        super.onDestroy();

        if(tts!=null){
            tts.stop();
            tts.shutdown();
            tts=null;
        }
    }

    View.OnClickListener listner_exEnd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("pop up 전", exType+""+exCount);
            endEx();
        }
    };
    View.OnClickListener listner_changeView = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if(mCameraFacing=="1") mCameraFacing="0";
            else mCameraFacing="1";
            closeCamera();
            openCamera(textureView.getWidth(), textureView.getHeight());
        }

    };
    public void endEx(){

        closeCamera();
        exAccuracy = (int)(((double)exResult[0]/((double)exResult[0]+(double)exResult[1]))*100);
        ExEndPopup popup = new ExEndPopup(getActivity(), exType, exerciseCounter, exAccuracy, new ExEndPopup.PopupEventListener() {
            @Override
            public void popupEvent(String result) {
                if (result.equals("selectEx")){
                    Toast.makeText(getActivity(), "go to select exercise page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),ExListActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if(result.equals("goCalander")){
                    Toast.makeText(getActivity(), "go Calender", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),ExHistoryActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        if (exerciseCounter != 0)
            postHist();
    }

    private void postHist(){
        // db에 저장
        ServerComm serverComm = new ServerComm();
        RetrofitCommnunication retrofitComm = serverComm.init();
        JsonObject trainingHist = new JsonObject();
        trainingHist.addProperty("id", SharedPreferenceData.getId(getActivity()));
        trainingHist.addProperty("start_time", start_time);
        trainingHist.addProperty("ex_count",exerciseCounter);
        trainingHist.addProperty("ex_type",exType);
        trainingHist.addProperty("ex_difficulty", exDifficulty);
        trainingHist.addProperty("ex_accuracy", exAccuracy);
        Log.d(TAG, exDifficulty+","+exAccuracy);
        retrofitComm.postTrainingHist(trainingHist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data->{
                    Log.e("datedate", "postHist: " + trainingHist);
                    String histResult = data.get("result").getAsString();
                    if (!histResult.equals("saved")){
                        Log.d(TAG, "endEx: 운동기록 저장 실패");
                    }
                });
    }

    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private void setUpCameraOutputs(int width, int height) {
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // // For still image captures, we use the largest available size.
                Size largest =
                        Collections.max(
                                Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                imageReader =
                        ImageReader.newInstance(
                                largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/ 2);

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                // noinspection ConstantConditions
        /* Orientation of the camera sensor */
                int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (sensorOrientation == 90 || sensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (sensorOrientation == 0 || sensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                previewSize =
                        chooseOptimalSize(
                                map.getOutputSizes(SurfaceTexture.class),
                                rotatedPreviewWidth,
                                rotatedPreviewHeight,
                                maxPreviewWidth,
                                maxPreviewHeight,
                                largest);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                nowHeight= previewSize.getHeight();
                nowWidth = previewSize.getWidth();
                int orientation = getResources().getConfiguration().orientation;
                if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    nowHeight= previewSize.getWidth();
                    nowWidth = previewSize.getHeight();
                }
                layoutFrame.setAspectRatio(nowWidth, nowHeight);
                textureView.setAspectRatio(nowWidth, nowHeight);
                drawView.setAspectRatio(nowWidth, nowHeight);

                nowWidth = View.MeasureSpec.getSize(nowWidth);
                nowHeight = View.MeasureSpec.getSize(nowHeight);
//        이미지 사이즈 조절 하기
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) personImg.getLayoutParams();
                params.width = width;
                params.height = height;
                if (0 != nowWidth && 0 != nowHeight) {
                    if (width < height * nowWidth / nowHeight) {
                        params.height = width * nowHeight / nowWidth;
                    } else {
                        params.width = height * nowWidth / nowHeight;
                    }
                }

                personImg.setLayoutParams(params);
                this.cameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to access Camera", e);
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }

    private String[] getRequiredPermissions() {
        Activity activity = getActivity();
        try {
            PackageInfo info =
                    activity
                            .getPackageManager()
                            .getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    /**
     * Opens the camera specified by {@link Camera2BasicFragment#cameraId}.
     */
    private void openCamera(int width, int height) {
        if (!checkedPermissions && !allPermissionsGranted()) {
            FragmentCompat.requestPermissions(this, getRequiredPermissions(), PERMISSIONS_REQUEST_CODE);
            return;
        } else {
            checkedPermissions = true;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
//          1-> 전면 카메라, 0->후면 카메라
            manager.openCamera(mCameraFacing, stateCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to open Camera", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
            cameraOpenCloseLock.acquire();
            if (null != captureSession) {
                captureSession.close();
                captureSession = null;
            }
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
            if (null != imageReader) {
                imageReader.close();
                imageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            cameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        backgroundThread = new HandlerThread(HANDLE_THREAD_NAME);
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
        synchronized (lock) {
            runClassifier = true;
        }
        backgroundHandler.post(periodicClassify);
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
            synchronized (lock) {
                runClassifier = false;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted when stopping background thread", e);
        }
    }

    /**
     * Takes photos and classify them periodically.
     */
    private Runnable periodicClassify =
            new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        if (runClassifier) {
                            classifyFrame();
                        }
                    }
                    backgroundHandler.post(periodicClassify);
                }
            };

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            previewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice.createCaptureSession(
                    Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == cameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession;
                            try {
                                previewRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_ON);
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build();
                                captureSession.setRepeatingRequest(
                                        previewRequest, captureCallback, backgroundHandler);
                            } catch (CameraAccessException e) {
                                Log.e(TAG, "Failed to set up config to capture Camera", e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    },
                    null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to preview Camera", e);
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `textureView`. This
     * method should be called after the camera preview size is determined in setUpCameraOutputs and
     * also the size of `textureView` is fixed.
     *
     * @param viewWidth  The width of `textureView`
     * @param viewHeight The height of `textureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == textureView || null == previewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale =
                    Math.max(
                            (float) viewHeight / previewSize.getHeight(),
                            (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }

    /**
     * Classifies a frame from the preview stream.
     */
    private void classifyFrame() {
        if (classifier == null || getActivity() == null || cameraDevice == null) {
//            showToast("Uninitialized model or invalid context.");
            return;
        }
        Bitmap bitmap = textureView.getBitmap(classifier.getImageSizeX(), classifier.getImageSizeY());
        String textToShow = classifier.classifyFrame(bitmap);
        bitmap.recycle();

//        drawView.setDrawPoint(classifier.mPrintPointArray, 0.25f);
        drawView.setDrawPoint(classifier.mPrintPointArray, 0.5f);

        if(readyCounter <= READY_BOUND) {// 준비 안된 상태

            // 여기에서 함수 호출해서 결과값 받아서 UI 변경
            exercise.setPoint(classifier.mPrintPointArray);
            exercise.setDpPoint(drawView.mDrawPoint);
            if(readyTts==false){
                tts.speak("그림에 맞춰 서주세요", TextToSpeech.QUEUE_ADD,null);
                readyTts=true;
            }
            readyEx(exercise.checkReady());
//            showToast("readyCounter: "+readyCounter);
        } else {
            // 운동 시작
            // 운동 실행하는 함수 호출
            exercise.setPoint(classifier.mPrintPointArray);
            exercise.setDpPoint(drawView.mDrawPoint);
            if(exTts==false){
                tts.speak("운동을 시작해 주세요", TextToSpeech.QUEUE_ADD,null);
                exTts=true;
            }
            startEx(exercise.doExercise(exerciseStep));
            Log.d(TAG, exerciseStep+", "+exerciseStep/100);
            if(exType==1) showToast("Step: "+ Integer.toString(exerciseStep/100)+", Count : "+Integer.toString(exerciseCounter));
            else showToast("Step: "+ Integer.toString(exerciseStep)+", Count : "+Integer.toString(exerciseCounter));
//            showToast2(Double.toString(exercise.getAngle(9, 8, 9, 10)));
//            showToast3(Double.toString(exercise.getAngle(3,2, 3, 4)));

        }
    }



    private void readyEx(boolean isReady) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (isReady){
                                Log.d("Exercise", "준비됨");
                                personImg.setImageResource(img_green);
                                readyCounter++;
                            }
                            else {
                                Log.d("Exercise", "안됨");
                                personImg.setImageResource(img_red);
                                readyCounter = 0;
                            }
                        }
                    });
        }
    }
    boolean isStart = false;
    long flankTime = 0;
    long lastTime = 0;
    int lastExCounter = 0;
    private void startEx(ArrayList<Integer> isStepDone) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            if(exerciseCounter>=exCount) return;
                            personImg.setVisibility(View.INVISIBLE);
                            if (!isStepDone.isEmpty()){
                                if(exerciseCounter>=exCount) return;
                                if (isStepDone.get(0)==1){
                                    Log.d("Exercise", "다음 step으로");
                                    resetStepCounter = 0;
                                    exResult[0]++;
                                    if(exType==1){
                                        //시간 운동일 때
                                        if(isStart==false){
                                            //전에가 잘못된 자세였다면
                                            isStart = true;
                                            lastTime = System.currentTimeMillis();
                                        }else{
                                            //전에 잘된 자세였다면 전시각과 현재시각의 차이를 flankTime에 넣고 step에 추가
                                            flankTime = (System.currentTimeMillis()-lastTime);
                                            exerciseStep +=flankTime;
                                            lastTime = System.currentTimeMillis();
                                            Log.d("flank", lastTime+", "+flankTime);
                                            //step이 1000(1초) 이상이라면
                                            if (exerciseStep >= endStep) {
                                                //count 증가시키기
                                                exerciseCounter++;
                                                if(exType==1){
                                                    if(exerciseCounter<=5 || exerciseCounter%5==0)CountSpeak(exerciseCounter);
                                                }
                                                else CountSpeak(exerciseCounter);
                                                if (exerciseCounter == exCount)
                                                    endEx();
                                                else
                                                    exerciseStep -= endStep;
                                            }
                                        }
                                    }
                                    else{
                                        //횟수 운동일 때
                                        exerciseStep++;
                                        if (exerciseStep >= endStep) {
                                            exerciseCounter++;
                                            CountSpeak(exerciseCounter);
                                            if (exerciseCounter == exCount)
                                                endEx();
                                            else
                                                exerciseStep -= endStep;
                                        }
                                    }


                                }
                                else if (isStepDone.get(0) == -1){
                                    Log.d("Exercise", "step 못 넘어감"+ isStepDone.get(1));
                                    if(isStepDone.size()>1) errorHistory.add(isStepDone.get(1));
                                    if(exType==1) {
                                        isStart = false;
                                    }
                                    // 너무 오랫동안 다음 Step으로 못넘어가는 경우 Step 초기화
                                    if (++resetStepCounter > RESET_STEP_BOUND){
                                        exResult[1]++;
                                        exerciseStep = 0;
                                        resetStepCounter = 0;

                                        //가장 많았던 오류 찾기
                                        int[] errorList = new int[10];
                                        int maxNum = 0;
                                        int maxWhi = -1;
                                        for(int i=0;i<10;i++)errorList[i]=0;
                                        for(int i=0;i<errorHistory.size();i++){
                                            errorList[errorHistory.get(i)]++;
                                        }
                                        for(int i=0;i<10;i++){
                                            if(errorList[i]>maxNum) {
                                                maxNum = errorList[i];
                                                maxWhi=i;
                                            }
                                        }
                                        errorHistory.clear();
                                        ErrorSpeak(maxWhi);
                                    }
                                } else if (isStepDone.get(0) == -2){
                                    // 바로 피드백 해주는 오류
                                    exResult[1]++;
                                    ErrorSpeak(isStepDone.get(1));
                                    exerciseStep = 0;
                                    resetStepCounter = 0;
                                }
                            }

                        }
                    });
        }
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    private static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum(
                    (long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    @Override
    public void onInit(int i) {
    }

    private void ErrorSpeak(int i) {
        String correction = exCorrection.getCorrection(exType, i);
        tts.speak(correction, TextToSpeech.QUEUE_ADD,null);
    }

    private void CountSpeak(int count){
        tts.speak(Integer.toString(count), TextToSpeech.QUEUE_ADD,null);
    }

    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(
                            android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            })
                    .create();
        }
    }
}
