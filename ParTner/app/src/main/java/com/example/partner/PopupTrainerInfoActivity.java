package com.example.partner;

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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.partner.Core.utils.SharedPrefsHelper;
import com.example.partner.Core.utils.Toaster;
import com.example.partner.GroupChatWebRTC.activities.BaseActivity;
import com.example.partner.GroupChatWebRTC.activities.OpponentsActivity;
import com.example.partner.GroupChatWebRTC.services.CallService;
import com.example.partner.GroupChatWebRTC.utils.Consts;
import com.example.partner.GroupChatWebRTC.utils.QBEntityCallbackImpl;
import com.example.partner.GroupChatWebRTC.utils.UsersUtils;
import com.google.gson.JsonObject;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.model.QBUser;

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

    // 영상통화
    private Context context = PopupTrainerInfoActivity.this;
    private QBUser userForSave;
    private String userId;
    private String trainerId;

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
        bookmarkBtn = findViewById(R.id.bookmark);

        Intent intent = getIntent();

        // 이미지도 넣어야함...
        float starrate_data = Float.parseFloat(intent.getStringExtra("star_rate"));
        trainerId = intent.getStringExtra("id");
        String name_data = intent.getStringExtra("name");
        String train_data = intent.getStringExtra("traintype");
        String intro_data = intent.getStringExtra("intro");

        mRating.setRating(starrate_data);
        name.setText(name_data);
        traingType.setText(train_data);
        introduction.setText(intro_data);

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
        userId = SharedPreferenceData.getId(context);
        startSignUpNewUser(createUserWithEnteredData());
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
