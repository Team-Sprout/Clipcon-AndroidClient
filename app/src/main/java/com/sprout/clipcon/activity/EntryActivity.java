package com.sprout.clipcon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sprout.clipcon.R;


/**
 * Created by Yongwon on 2017. 4. 19..
 */

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);

        Button createBtn = (Button)findViewById(R.id.entry_create);
        Button joinBtn = (Button)findViewById(R.id.entry_join);

        Toolbar toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        toolbar.setTitle("entry");
        setSupportActionBar(toolbar);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GroupActivity.class));
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GroupActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_contacts:
                Toast.makeText(getApplicationContext(), "주소록으로 이동 (아이콘 바꾸기)", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
                return true;

            case R.id.group_settings:
                Toast.makeText(getApplicationContext(), "설정으로 이동", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        new MaterialDialog.Builder(this)
                .content("로그아웃됩니다. 계속 진행하시겠습니까?")
                .negativeText("NO")
                .positiveText("YES")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //// TODO: 2017. 4. 19. add logout action
                        EntryActivity.super.onBackPressed();
                    }
                })
                .show();
    }
}
