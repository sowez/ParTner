package com.example.partner;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class TrainerSignUpActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1;
    private File profileImg;
    private Uri profileURI;

    private EditText trainerId, trainerPw, trainerPw_check, trainerName;
    private RadioGroup trainerSex;
    private RadioButton trainerMale, trainerFemale;
    private CheckBox yoga, muscle, pilates, stretching;
    private Button btn_gallery, btn_trainerSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_signup);
        init();
    }

    private void init() {

        trainerId = findViewById(R.id.trainer_id);
        trainerPw = findViewById(R.id.trainer_pw);
        trainerPw_check = findViewById(R.id.trainer_pw_check);
        trainerName = findViewById(R.id.trainer_name);

        trainerSex = findViewById(R.id.trainer_sex);
        trainerMale = findViewById(R.id.trainer_male);
        trainerFemale = findViewById(R.id.trainer_female);

        yoga = findViewById(R.id.check_yoga);
        muscle = findViewById(R.id.check_muscle);
        pilates = findViewById(R.id.check_pilates);
        stretching = findViewById(R.id.check_stretching);

        btn_gallery = findViewById(R.id.btn_gallery_signup);
        btn_gallery.setOnClickListener(this::onClickBtnGallery);
        btn_trainerSignUp = findViewById(R.id.btn_trainer_signup);
        btn_trainerSignUp.setOnClickListener(this::onClickBtnSignUp);
    }

    public void onClickBtnGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_ALBUM) {
            profileURI = data.getData();

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert profileURI != null;
                cursor = getContentResolver().query(profileURI, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                profileImg = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        }
    }

    private void setImage() {

        ImageView imageView = findViewById(R.id.signup_trainer_profile);
        imageView.setBackground(null);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(profileImg.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);

    }

    public void onClickBtnSignUp(View v) {
        String id = trainerId.getText().toString();
        String pw = trainerPw.getText().toString();
        String pw_check = trainerPw_check.getText().toString();
        String name = trainerName.getText().toString();
        String sex;
        if (trainerMale.isChecked()) {
            sex = "male";
        } else {
            sex = "female";
        }
        ArrayList<String> trainingTypes = new ArrayList<>();
        if (yoga.isChecked()) {
            trainingTypes.add("yoga");
        }
        if (muscle.isChecked()) {
            trainingTypes.add("muscle");
        }
        if (pilates.isChecked()) {
            trainingTypes.add("pilates");
        }
        if (stretching.isChecked()) {
            trainingTypes.add("stretching");
        }

        //영어, 숫자, 특수문자 포함 6자리
        String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,}$";
        if (Pattern.matches(pwPattern, pw) && pw.equals(pw_check)) {
            SignUpData signUpData = new SignUpData("trainer", id, pw, name, sex, trainingTypes);
            ServerComm serverComm = new ServerComm();
            serverComm.init();
            serverComm.postUploadImg(profileImg);
            serverComm.postSignUp(signUpData, this);
        } else if (pw.equals(pw_check)) {
            Toast.makeText(this, "비밀번호는 영문자, 숫자, 특수문자를 포함하여 6자리 이상으로 만들어주세요", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "비밀번호와 비밀번호 재확인이 일치하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }


}
