package com.sprout.clipcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sprout.clipcon.R;

/**
 * Created by Yongwon on 2017. 5. 12..
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // new EndpointInBackGround().execute(Message.CONNECT); // connect

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
               // while (!isConnection) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
               // }
            }
        }, 1000);

    }

    private void changeMainAvtivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
