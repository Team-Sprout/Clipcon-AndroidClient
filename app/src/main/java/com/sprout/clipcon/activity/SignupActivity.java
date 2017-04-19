package com.sprout.clipcon.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sprout.clipcon.R;


/**
 * Created by Yongwon on 2017. 4. 17..
 */

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        final EditText nickname = (EditText) findViewById(R.id.signup_nickname);
        final EditText email = (EditText) findViewById(R.id.signup_email);
        final EditText pw1 = (EditText) findViewById(R.id.signup_password1);
        final EditText pw2 = (EditText) findViewById(R.id.signup_password2);
        final TextView resultText = (TextView) findViewById(R.id.signup_result);


        final Button signupBtn = (Button) findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempPw1 = pw1.getText().toString();
                String tempPw2 = pw2.getText().toString();
                String tempName = nickname.getText().toString();
                String tempEmail = email.getText().toString();

                if(tempName.equals("") || tempEmail.equals("") || tempPw1.equals("") || tempPw2.equals("")){
                    resultText.setText("모든 항목을 입력하세요.");
                    resultText.setTextColor(Color.RED);
                }else{
                    if(tempPw1.equals(tempPw2)){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }else{
                        resultText.setText("비밀번호를 확인하세요.");
                        resultText.setTextColor(Color.RED);
                    }
                }
            }
        });
    }
}
