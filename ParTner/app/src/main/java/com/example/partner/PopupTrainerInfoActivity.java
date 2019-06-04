package com.example.partner;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.partner.Core.utils.SharedPrefsHelper;
import com.example.partner.Core.utils.Toaster;
import com.example.partner.GroupChatWebRTC.activities.BaseActivity;
import com.example.partner.GroupChatWebRTC.activities.CallActivity;
import com.example.partner.GroupChatWebRTC.activities.OpponentsActivity;
import com.example.partner.GroupChatWebRTC.activities.PermissionsActivity;
import com.example.partner.GroupChatWebRTC.db.QbUsersDbManager;
import com.example.partner.GroupChatWebRTC.services.CallService;
import com.example.partner.GroupChatWebRTC.utils.CollectionsUtils;
import com.example.partner.GroupChatWebRTC.utils.Consts;
import com.example.partner.GroupChatWebRTC.utils.PermissionsChecker;
import com.example.partner.GroupChatWebRTC.utils.PushNotificationSender;
import com.example.partner.GroupChatWebRTC.utils.QBEntityCallbackImpl;
import com.example.partner.GroupChatWebRTC.utils.UsersUtils;
import com.example.partner.GroupChatWebRTC.utils.WebRtcSessionManager;
import com.google.gson.JsonObject;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupTrainerInfoActivity extends BaseActivity {

    private ImageView profileImage;
    private RatingBar mRating;
    private TextView name;
    private TextView traingType;
    private TextView introduction;
    private ToggleButton bookmarkBtn;
    private Button callBtn;

    // 영상통화 로그인
    private Context context = PopupTrainerInfoActivity.this;
    private QBUser userForSave;
    private String userId;
    private String trainerId;
    private String name_data;
    private String train_data;
    private String intro_data;
    private String trainer_qb_id;
    private URL url;
    private Bitmap bitmap;
    private String imgpath;

    // 영상통화 call
    private QBUser currentUser;
    private QbUsersDbManager dbManager;
    private boolean isRunForCall;
    private WebRtcSessionManager webRtcSessionManager;
    private PermissionsChecker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        setContentView(R.layout.activity_trainerinfo_popup);


        if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
            CallActivity.start(context, true);
        }
        checker = new PermissionsChecker(getApplicationContext());

        profileImage = (ImageView) findViewById(R.id.profile_img);
        mRating = (RatingBar) findViewById(R.id.ratingbar);
        name = (TextView) findViewById(R.id.name);
        traingType = (TextView) findViewById(R.id.training_type);
        introduction = (TextView) findViewById(R.id.self_introduction);
        bookmarkBtn = findViewById(R.id.bookmark);
        callBtn = findViewById(R.id.call_btn);
        callBtn.setOnClickListener(this::mOnClose);

        Intent intent = getIntent();
        // 이미지도 넣어야함...
        float starrate = Float.parseFloat(intent.getStringExtra("star_rate"));
        float starrate_num = Float.parseFloat(intent.getStringExtra("star_rate_num"));
        float starrate_data = starrate/starrate_num;

        trainerId = intent.getStringExtra("id");
        name_data = intent.getStringExtra("name");
        train_data = intent.getStringExtra("traintype");
        intro_data = intent.getStringExtra("intro");
        imgpath = intent.getStringExtra("imgpath");


        trainer_qb_id = intent.getStringExtra("qb_id");
        Log.d("qbid", "startCall: " + trainer_qb_id);
        String name_data = intent.getStringExtra("name");
        String train_data = intent.getStringExtra("traintype");
        String intro_data = intent.getStringExtra("intro");
        if(intent.getBooleanExtra("bookmark",false)){
            bookmarkBtn.setChecked(true);
            bookmarkBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.icons_star_filled));
        }
        mRating.setClickable(false);
        mRating.setRating(starrate_data);
        name.setText(name_data);
        traingType.setText(train_data);
        introduction.setText(intro_data);

        String trainer_state = intent.getStringExtra("state");
        if(trainer_state.equals("offline")){
            callBtn.setClickable(false);
            callBtn.setBackgroundColor(Color.rgb(72,72,72));
        }

        RetrofitCommnunication retrofitcomm = ServerComm.init();
        bookmarkBtn.setOnClickListener(v -> {
            if(bookmarkBtn.isChecked()){
                bookmarkBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.icons_star_filled));

                JsonObject bookmark = new JsonObject();
                bookmark.addProperty("sportsmanID",SharedPreferenceData.getId(this));
                bookmark.addProperty("trainerID", trainerId);

                retrofitcomm.bookmarkUpdate(bookmark).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        if(res.get("result").getAsString().equals("success")){
                            Toast.makeText(context, "트레이너 즐겨찾기가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "트레이너 즐겨찾기가 실패하었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("bookmark", "sportsman bookmark onFailure");
                    }
                });

            }else{
                bookmarkBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.icons_star));
                JsonObject bookmark = new JsonObject();
                bookmark.addProperty("sportsmanID",SharedPreferenceData.getId(this));
                bookmark.addProperty("trainerID", trainerId);
                retrofitcomm.bookmarkDelete(bookmark).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        if(res.get("result").getAsString().equals("success")){
                            Toast.makeText(context, "트레이너 즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "트레이너 즐겨찾기 해제가 실패하었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("bookmark", "sportsman bookmark onFailure");
                    }
                });
            }
        });

        ServerComm serverComm = new ServerComm();
        imgpath = serverComm.getURL() + "db/resources/images/trainer_profile/" + imgpath;

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    url = new URL(imgpath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try {
            mThread.join();
            hideProgressDialog();
            profileImage.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.id_trainer_info_popup);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            isRunForCall = intent.getExtras().getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
            if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                CallActivity.start(context, true);
            }
        }
    }

    public void mOnClose(View v) {
        // 영상통화 하는 부분으로 그냥 넘겨줌
//        Intent intent = new Intent(this,com.example.partner.GroupChatWebRTC.activities.LoginActivity.class);
//        intent.putExtra("trainer_name", name.getText());
//        startActivity(intent);

        /* 영상통화 로그인하기 */

        showProgressDialog(R.string.waiting_facetalk);
        userId = SharedPreferenceData.getId(context);
        startSignUpNewUser(createUserWithEnteredData());

        // 통화 기록 남기
        CallData.getInstance().setCallReceiverID(trainerId);
        CallData.getInstance().setCallReceiverName(name_data);
        Toast.makeText(PopupTrainerInfoActivity.this, "CallData에 저장"+trainerId, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        //안드로이드 백버튼 막기
//        return;
//    }

    /* 영상통화 로그인 */

    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(userId, trainerId);
    }

    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
            StringifyArrayList<String> userTags = new StringifyArrayList<>();
            userTags.add(chatRoomName);

            qbUser = new QBUser();
            qbUser.setFullName(userName);
            qbUser.setLogin(Utils.generateDeviceId(this));
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
            qbUser.setTags(userTags);
        }

        return qbUser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);
                signInCreatedUser(userForSave, false);
            } else {
                hideProgressDialog();
                Toaster.longToast(getString(R.string.login_chat_login_error) + " / onActivityResult : " + errorMessage);
            }
        }
    }

    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.getTags().get(0));
        sharedPrefsHelper.saveQbUser(qbUser);
    }

    private void startSignUpNewUser(final QBUser newUser) {
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        loginToChat(result);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser, true);
                        } else {
                            hideProgressDialog();
                            Toaster.longToast(R.string.sign_up_error + " / startSignUpNewUser");
                            Log.d("loginlogin", "onError: startSignUpNewUser :// " + e.getMessage());
                            signInCreatedUser(newUser, true);
                        }
                    }
                }
        );
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);

        userForSave = qbUser;
        startLoginService(qbUser);
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, CallService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallService.start(this, qbUser, pendingIntent);
    }

    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                if (deleteCurrentUser) {
                    loginRemoveAllUserData(result);
                } else {
                    hideProgressDialog();

                    Intent intent = new Intent(context, PopupTrainerInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, false);

                    if (isLoggedInChat()) {
                        initFields();
                        startLoadUsers();
                    }
                    if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                            PermissionsActivity.startActivity((Activity) context, false, Consts.PERMISSIONS);
                    }
                    finish();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                hideProgressDialog();
                Toaster.longToast(R.string.sign_up_error + " / SigninCreateUser");
            }
        });
    }

    private boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            Toaster.shortToast(R.string.dlg_signal_error);
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    private void tryReLoginToChat() {
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            CallService.start(this, qbUser);
        }
    }

    private void startCall(boolean isVideoCall) {
        Log.d("popup_trainer_info", "startCall()");
        Log.d("qbid", "startCall: " + trainer_qb_id);

        Integer trainerID = Integer.parseInt(trainer_qb_id);
        ArrayList<QBUser> trainer_qb_data = new ArrayList<>();
        trainer_qb_data.add(dbManager.getUserById(trainerID));
        ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(trainer_qb_data);
        Log.d("qbdb", "startCall: " + dbManager.getAllUsers());
        opponentsList.remove(userForSave);

        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

        PushNotificationSender.sendPushMessage(opponentsList, userForSave.getFullName());

        CallActivity.start(this, false);
        Log.d("popup_trainer_info", "conferenceType = " + conferenceType);
    }


    private void initFields() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
        }

        currentUser = sharedPrefsHelper.getQbUser();
        dbManager = QbUsersDbManager.getInstance(getApplicationContext());
        webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
    }

    private void startLoadUsers() {
        String currentRoomName = SharedPrefsHelper.getInstance().get(Consts.PREF_CURREN_ROOM_NAME);
        requestExecutor.loadUsersByTag(currentRoomName, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> result, Bundle params) {
                dbManager.saveAllUsers(result, true);
                startCall(true);
            }
            @Override
            public void onError(QBResponseException responseException) {
                showErrorSnackbar(R.string.loading_users_error, responseException, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoadUsers();
                    }
                });
            }
        });
    }

    private void loginRemoveAllUserData(final QBUser user) {
        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                UsersUtils.removeUserData(getApplicationContext());
                startSignUpNewUser(createUserWithEnteredData());
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
                Toaster.longToast(R.string.sign_up_error + " / removeAllUserData");
            }
        });
    }

}
