package com.sprout.clipcon.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sprout.clipcon.R;
import com.sprout.clipcon.fragment.HistoryFragment;
import com.sprout.clipcon.fragment.InfoFragment;
import com.sprout.clipcon.service.MyService;


/**
 * Created by Yongwon on 2017. 4. 17..
 */

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        System.out.println("GroupActivity === onCreate called =============");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity);

        initLayout();
        checkStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("GroupActivity === onDestroy called =============");
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        stopService(intent);
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
                Toast.makeText(getApplicationContext(), "설정으로 이동", Toast.LENGTH_SHORT).show();
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
                .content("그룹에서 나가며, 히스토리가 모두 삭제됩니다. 계속하시겠습니까?")
                .negativeText("NO")
                .positiveText("YES")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //// TODO: 2017. 4. 19. add delete group history action
                        //// TODO: 2017. 5. 11. notify server about participant leaving

                        Intent intent = new Intent(getApplicationContext(), MyService.class);
                        stopService(intent);
                        GroupActivity.super.onBackPressed();
                    }
                })
                .show();
    }


    // start MyService.class to float always on Top Button when clipbard changed
    public void checkStart() {
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);

        Toast toast = Toast.makeText(getApplicationContext(), "Start Clipboard Check", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    // make Toolbar, Tablayout, ViewPager
    private void initLayout() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.group_toolbar);
        toolbar.setTitle("Clipcon");
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("INFO"));
        tabLayout.addTab(tabLayout.newTab().setText("HISTORY"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
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
        private final int HISTORY= 1;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case INFO:
                    return new InfoFragment();
                case HISTORY:
                    return new HistoryFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
