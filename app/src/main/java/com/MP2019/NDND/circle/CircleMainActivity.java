package com.MP2019.NDND.circle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.MP2019.NDND.circle.Finance.AccountingFragment;
import com.MP2019.NDND.R;
import com.MP2019.NDND.circle.Setting.SettingFragment;

public class CircleMainActivity extends AppCompatActivity {
    TextView tv;
    BottomNavigationView bottomNavigationView;
    String circleName;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_main);

        // 동아리명, userID 받아옴
        Intent receive= getIntent();
        circleName=receive.getStringExtra("circleName");
        userID=receive.getStringExtra("userID");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        final Bundle bundle= new Bundle(2);
        bundle.putString("circleName",circleName);
        bundle.putString("userID", userID);
        //navigation bar click listener
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.menu_schedule:
                                com.MP2019.NDND.ScheduleFragment Sfragment= new com.MP2019.NDND.ScheduleFragment();
                                Sfragment.setArguments(bundle);
                                transaction.replace(R.id.frame_layout, Sfragment);
                                break;
                            case R.id.menu_accounting:
                                AccountingFragment Afragment= new AccountingFragment();
                                Afragment.setArguments(bundle);
                                transaction.replace(R.id.frame_layout, Afragment);
                                break;
                            case R.id.menu_setting:
                                SettingFragment settingFragment=new SettingFragment();
                                settingFragment.setArguments(bundle);
                                transaction.replace(R.id.frame_layout, settingFragment);
                                break;
                        }
                        transaction.commit();
                        return true;
                    }
                });
        // 첫 화면
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        com.MP2019.NDND.ScheduleFragment fragment= new com.MP2019.NDND.ScheduleFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
