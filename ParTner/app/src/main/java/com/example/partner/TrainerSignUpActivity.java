package com.example.partner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class TrainerSignUpActivity extends AppCompatActivity {
    private EditText trainerId, trainerPw, trainerPw_check, trainerName;
    private RadioGroup trainerSex;
    private RadioButton trainerMale, trainerFemale;
    private CheckBox yoga, muscle, pilates, stretching;
    private Button btn_trainerSignUp;

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

        btn_trainerSignUp = findViewById(R.id.btn_trainer_signup);
        btn_trainerSignUp.setOnClickListener(this::onClick);
    }
//trainer
    public void onClick(View v) {
        String id = trainerId.getText().toString();
        String pw = trainerPw.getText().toString();
        String name = trainerName.getText().toString();
        String sex = null;
        if (trainerMale.isChecked()) {
            sex = "male";
        } else if (trainerFemale.isChecked()) {
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

        SignUpData signUpData = new SignUpData("trainer", id, pw, name, sex, trainingTypes);

        ServerComm serverComm = new ServerComm();
        serverComm.init();
        serverComm.postSignUp(signUpData, this);
    }


}
