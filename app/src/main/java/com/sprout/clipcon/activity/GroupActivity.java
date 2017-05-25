package com.sprout.clipcon.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sprout.clipcon.R;
import com.sprout.clipcon.fragment.HistoryFragment;
import com.sprout.clipcon.fragment.InfoFragment;
import com.sprout.clipcon.model.Message;
import com.sprout.clipcon.server.EndpointInBackGround;
import com.sprout.clipcon.service.ClipboardService;
import com.sprout.clipcon.service.NotificationService;


/**
 * Created by Yongwon on 2017. 4. 17..
 */

public class GroupActivity extends AppCompatActivity {
    private Fragment infoFragment;
    private Fragment historyFragment;

    private TabLayout tabLayout;

    Sensor accelerometer;
    SensorManager sm;
    String checkType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity);

        initLayout();
        checkStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if(intent != null) {
            Log.d("Choi", "Type Check 1 = "+checkType);
            if(checkType != null) {
                Log.d("Choi", "Type Check 2 = "+checkType);
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
            }
        }

        LocalBroadcastManager.getInstance(GroupActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("NOW"));
    }

    @Override
    protected void onDestroy() {
        Log.d("delf", "GroupActivity is destroyed.");
        super.onDestroy();
        Intent clipIntent = new Intent(getApplicationContext(), ClipboardService.class);
        stopService(clipIntent);

        Intent notiIntent = new Intent(getApplicationContext(), NotificationService.class);
        stopService(notiIntent);
    }

    // create menu in Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    // event when settings icon pressed in Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.group_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // back button control
    @Override
    public void onBackPressed() {

        new MaterialDialog.Builder(this)
                .content(R.string.exitAlert)
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new EndpointInBackGround().execute(Message.REQUEST_EXIT_GROUP);
                        Intent intent = new Intent(getApplicationContext(), ClipboardService.class);
                        stopService(intent);
                        GroupActivity.super.onBackPressed();
                    }
                })
                .show();
    }


    // start ClipboardService.class to float always on Top Button when clipboard changed
    public void checkStart() {
        Intent clipIntent = new Intent(getApplicationContext(), ClipboardService.class);
        startService(clipIntent);

        Intent notiIntent = new Intent(getApplicationContext(), NotificationService.class);
        startService(notiIntent);
    }

    private void initLayout() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.group_toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.title_logo);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.info));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.history));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // adapter for ViewPager to create two view (INFO, HISTORY)
    public class MainPagerAdapter extends FragmentStatePagerAdapter {

        private final int TAB_COUNT = 2;
        private final int INFO = 0;
        private final int HISTORY = 1;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case INFO:
                    return new InfoFragment();
                case HISTORY:
                    historyFragment = new HistoryFragment();
                    return historyFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("History");  //get the type of message from MyGcmListenerService 1 - lock or 0 -Unlock
            sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Log.d("Choi", "Type Check 4 = "+type);
            if (type.equals("test")) // 1 == lock
            {
                checkType = type;
                Log.d("Choi", "Type Check 3 = "+checkType);
            }
        }
    };
}
