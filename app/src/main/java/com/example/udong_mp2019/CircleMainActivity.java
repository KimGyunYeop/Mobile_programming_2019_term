package com.example.udong_mp2019;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_main);

        Intent receive= new Intent();
        String circleName=receive.getStringExtra("circleName");
        String userID=receive.getStringExtra("userName");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        tv=(TextView) findViewById(R.id.tv_main);
        //navigation bar click listener
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.HomeFragment());
                                break;
                            case R.id.menu_schedule:
                                transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.ScheduleFragment());
                                break;
                            case R.id.menu_accounting:
                                transaction.replace(R.id.frame_layout, new AccountingFragment());
                                break;
                            case R.id.menu_setting:
                                transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.SettingFragment());
                                break;
                        }
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.HomeFragment());
        transaction.commit();
    }
}
