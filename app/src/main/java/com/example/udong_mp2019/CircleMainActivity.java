package com.example.udong_mp2019;

import android.app.Fragment;
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

//        Toast.makeText(CircleMainActivity.this, "홈화면입니다"+circleName+userID, Toast.LENGTH_LONG ).show();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        tv=(TextView) findViewById(R.id.tv_main);

        //navigation bar click listener
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                Bundle bundle= new Bundle(0b10);
                                bundle.putString("circleName",circleName);
                                bundle.putString("userID", userID);

                                com.example.udong_mp2019.HomeFragment fragment= new com.example.udong_mp2019.HomeFragment();
                                fragment.setArguments(bundle);
                                fm.beginTransaction().replace(R.id.frame_layout, fragment);
                                //transaction.replace(R.id.frame_layout, fragment);
                                break;
                            case R.id.menu_schedule:
                                transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.ScheduleFragment());
                                break;
                            case R.id.menu_accounting:
                                transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.AccountingFragment());
                                break;
                            case R.id.menu_setting:
                                transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.SettingFragment());
                                break;
                        }
                        transaction.commit();
                        return true;
                    }
                });

        //첫화면 지정(홈)
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new com.example.udong_mp2019.HomeFragment());
        transaction.commit();
    }
}
