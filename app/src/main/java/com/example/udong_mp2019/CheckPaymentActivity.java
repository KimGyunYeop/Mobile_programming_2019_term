package com.example.udong_mp2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckPaymentActivity extends AppCompatActivity {
ListView LV_memberList= (ListView)findViewById(R.id.LV_memberList);
    ArrayAdapter<String> add_memberList;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");
    private DatabaseReference userRef = mRootRef.child("user");
    static ArrayList<String> arrayIndex = new ArrayList<>();
    static ArrayList<String> arrayDataformember = new ArrayList<>();
    String  circleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);
        LV_memberList.setAdapter(add_memberList);

        Intent receive =getIntent();
        circleName=receive.getStringExtra("circleName");


    }

}