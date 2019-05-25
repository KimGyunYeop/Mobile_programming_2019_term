package com.example.udong_mp2019;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udong_mp2019.AccountingFragment;
import com.example.udong_mp2019.R;

import static com.example.udong_mp2019.HomeFragment.*;

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
        tv=(TextView) findViewById(R.id.tv_main);

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
                            case R.id.menu_home:
                                com.example.udong_mp2019.HomeFragment Hfragment= new com.example.udong_mp2019.HomeFragment();
                                Hfragment.setArguments(bundle);
                                transaction.replace(R.id.frame_layout, Hfragment);
                                break;
                            case R.id.menu_schedule:
                                com.example.udong_mp2019.ScheduleFragment Sfragment= new com.example.udong_mp2019.ScheduleFragment();
                                Sfragment.setArguments(bundle);
                                transaction.replace(R.id.frame_layout, Sfragment);
                                break;
                            case R.id.menu_accounting:
                                com.example.udong_mp2019.AccountingFragment Afragment= new com.example.udong_mp2019.AccountingFragment();
                                Afragment.setArguments(bundle);
                                transaction.replace(R.id.frame_layout, Afragment);
                                break;
                            case R.id.menu_setting:
                                com.example.udong_mp2019.SettingFragment Efragment= new com.example.udong_mp2019.SettingFragment();
                                Efragment.setArguments(bundle);
                                transaction.replace(R.id.frame_layout, Efragment);
                                break;
                        }
                        transaction.commit();
                        return true;
                    }
                });
        // 첫 화면
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        com.example.udong_mp2019.HomeFragment fragment= new com.example.udong_mp2019.HomeFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
