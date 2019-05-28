package com.example.partner;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.partner.Core.utils.SharedPrefsHelper;
import com.example.partner.Core.utils.Toaster;
import com.example.partner.GroupChatWebRTC.activities.BaseActivity;
import com.example.partner.GroupChatWebRTC.activities.OpponentsActivity;
import com.example.partner.GroupChatWebRTC.services.CallService;
import com.example.partner.GroupChatWebRTC.utils.Consts;
import com.example.partner.GroupChatWebRTC.utils.QBEntityCallbackImpl;
import com.example.partner.GroupChatWebRTC.utils.UsersUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.model.QBUser;

public class PopupTrainerInfoActivity extends BaseActivity {

    private ImageView profileImage;
    private RatingBar mRating;
    private TextView name;
    private TextView traingType;
    private TextView introduction;

    // 영상통화
    private Context context = PopupTrainerInfoActivity.this;
    private QBUser userForSave;
    private String userName;
    private String trainerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        setContentView(R.layout.activity_trainerinfo_popup);


        profileImage = (ImageView) findViewById(R.id.profile_img);
        mRating = (RatingBar) findViewById(R.id.ratingbar);
        name = (TextView) findViewById(R.id.name);
        traingType = (TextView) findViewById(R.id.training_type);
        introduction = (TextView) findViewById(R.id.self_introduction);

        Intent intent = getIntent();

        // 이미지도 넣어야함...
        float starrate_data = Float.parseFloat(intent.getStringExtra("star_rate"));
        String name_data = intent.getStringExtra("name");
        String train_data = intent.getStringExtra("traintype");
        String intro_data = intent.getStringExtra("intro");

        mRating.setRating(starrate_data);
        name.setText(name_data);
        traingType.setText(train_data);
        introduction.setText(intro_data);
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.id_trainer_info_popup);
    }

    public void mOnClose(View v) {
        // 영상통화 하는 부분으로 그냥 넘겨줌
//        Intent intent = new Intent(this,com.example.partner.GroupChatWebRTC.activities.LoginActivity.class);
//        intent.putExtra("trainer_name", name.getText());
//        startActivity(intent);

        /* 영상통화 로그인하기 */
        showProgressDialog(R.string.waiting_facetalk);
        userName = SharedPreferenceData.getUserName(context);
        trainerName = name.getText().toString();
        startSignUpNewUser(createUserWithEnteredData());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
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
        return createQBUserWithCurrentData(userName, trainerName);
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
                    removeAllUserData(result);
                } else {
                    hideProgressDialog();
                    Intent intent = new Intent(context, OpponentsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, false);
                    startActivity(intent);
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

    private void removeAllUserData(final QBUser user) {
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
