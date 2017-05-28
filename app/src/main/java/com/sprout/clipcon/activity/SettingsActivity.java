package com.sprout.clipcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.sprout.clipcon.R;


/**
 * Created by Yongwon on 2017. 4. 17..
 */

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        LinearLayout teamLayout = (LinearLayout)findViewById(R.id.settings_team);
        LinearLayout tutorialLayout = (LinearLayout)findViewById(R.id.settings_tutorial);
        LinearLayout faqLayout = (LinearLayout)findViewById(R.id.settings_faq);

        toolbar.setTitle(R.string.setting);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        teamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsTeamActivity.class);
                startActivity(intent);
            }
        });

        tutorialLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsTutorialActivity.class);
                startActivity(intent);
            }
        });

        faqLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsFaqActivity.class);
                startActivity(intent);
            }
        });
    }
}
