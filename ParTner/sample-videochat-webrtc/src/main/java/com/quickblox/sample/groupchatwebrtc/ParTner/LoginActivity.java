package com.quickblox.sample.groupchatwebrtc.ParTner;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.sample.groupchatwebrtc.R;
import com.quickblox.sample.groupchatwebrtc.activities.BaseActivity;
import com.quickblox.sample.groupchatwebrtc.services.CallService;
import com.quickblox.sample.groupchatwebrtc.utils.Consts;
import com.quickblox.sample.groupchatwebrtc.utils.QBEntityCallbackImpl;
import com.quickblox.sample.groupchatwebrtc.utils.UsersUtils;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    private String TAG = "TAG";

    private EditText login_id, login_pw;
    private Button loginButton, signupButton;
    private CheckBox autologinCheck;

    private Context context = LoginActivity.this;
    private QBUser userForSave;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        permission();
        init();
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.id_partner_login);
    }

    public void init() {

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);

        autologinCheck = findViewById(R.id.autologin);

        loginButton = findViewById(R.id.loginbutton);
        signupButton = findViewById(R.id.signupbutton);

        loginButton.setOnClickListener(this::onClickLogin);
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /* partner 서버 로그인 */
    private void onClickLogin(View v) {

        String loginId = login_id.getText().toString();
        userId = loginId;

        String loginPw = login_pw.getText().toString();

        ServerComm serverComm = new ServerComm();
        RetrofitCommnunication retrofitComm = serverComm.init();
        JsonObject logindata = new JsonObject();
        logindata.addProperty("id", loginId);
        logindata.addProperty("pw", loginPw);

        retrofitComm.login(logindata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    String loginResult = data.get("loginResult").getAsString();
                    Log.d(TAG, "onClickLogin: " + loginResult);
                    switch (loginResult) {
                        case "fail": {
                            Toast.makeText(this, "로그인에 실패하였습니다.아이디 또는 비밀번호를 다시 한번 입력해 주세요", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case "diffrent": {
                            Toast.makeText(this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case "login_success": {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClickLogin: data" + data);
                            String token = data.get("token").getAsString();
                            String type = data.get("type").getAsString();
                            String username = data.get("username").getAsString();
                            String id = data.get("id").getAsString();
                            boolean auto = autologinCheck.isChecked();

                            SharedPreferenceData.saveToken(this, id, token, username, type, auto);

                            if (type.equals("trainer")) {
                                startSignUpNewUser(createUserWithEnteredData());
                            } else {
                                Intent intent = new Intent(this, UserMainMenuActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                        }
                        default: {
                            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                }, err -> {
                    Log.d(TAG, "onClickLogin: error 발생");
                    Log.d(TAG, "onClickLogin: " + err.getMessage());
                });
    }

    /* 영상통화를 위한 로그인 */

    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(userId, userId);
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
                Toaster.longToast(getString(R.string.login_chat_login_error) + errorMessage);
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
                            Toaster.longToast(R.string.sign_up_error);
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
                    removeAllUserData(result);
                }else{
                    Intent intent = new Intent(context, TrainerMainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, false);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }

    private void removeAllUserData(final QBUser user) {
        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                UsersUtils.removeUserData(getApplicationContext());
                startSignUpNewUser(createUserWithEnteredData());
            }

            @Override
            public void onError(QBResponseException e) {
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }




    /* 권한 요청 */
    private void permission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                Log.e("TAG", "onPermissionGranted: 권한요청 성공");
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                Log.e("TAG", "onPermissionDenied: 권한요청 실패");
            }
        };

        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setDeniedMessage("사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

}