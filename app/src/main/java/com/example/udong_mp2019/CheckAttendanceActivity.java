package com.example.udong_mp2019;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CheckAttendanceActivity extends AppCompatActivity {

    ArrayList<String> arrayData = new ArrayList<>();
    ArrayAdapter<String> aad_displayAttendance;
    public boolean syncronization = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
        aad_displayAttendance = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView lv_displayAttendance = findViewById(R.id.LV_checkAttendance);
        lv_displayAttendance.setAdapter(aad_displayAttendance);
        getFirebaseDatabase();
    }

    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayData.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getKey();
                    Boolean attendance = (Boolean)postSnapshot.getValue();
                    Log.d("attendanceCheck",uid+attendance);
                    Log.d("attendanceCheck","user/"+uid+"/name");
                    arrayData.add(uid+":"+attendance);
                }
                Log.d("attendanceCheck",arrayData.toString());
                Collections.sort(arrayData);
                aad_displayAttendance.addAll(arrayData);
                aad_displayAttendance.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        String path = getIntent().getStringExtra("path");
        Query qurey = FirebaseDatabase.getInstance().getReference().child("circle/"+"가천대학교:하눌신폭"+"/schedule/plan/"+path+"/attendance").orderByValue();
        Log.d("attendanceCheck","circle/"+"가천대학교:하눌신폭"+"/schedule/plan/"+path+"/attendance");
        qurey.addListenerForSingleValueEvent(postListener);
    }
}
