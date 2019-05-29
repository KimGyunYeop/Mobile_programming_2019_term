package com.example.udong_mp2019.circle.Schedule;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.udong_mp2019.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

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
                    String attendance;
                    if((boolean)postSnapshot.getValue()){
                        attendance = "yes";
                    }else{
                        attendance = "no";
                    }
                    Log.d("attendanceCheck",uid+attendance);
                    Log.d("attendanceCheck","user/"+uid+"/name");
                    CountDownLatch done = new CountDownLatch(1);
                    FirebaseDatabase.getInstance().getReference().child("user/"+uid+"/name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("attendanceCheck","dataSnapshot="+dataSnapshot.toString());
                            String name = dataSnapshot.getValue().toString();
                            aad_displayAttendance.add(name+":"+attendance);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Log.d("attendanceCheck",arrayData.toString());
                Collections.sort(arrayData);
                //aad_displayAttendance.addAll(arrayData);
                aad_displayAttendance.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        String path = getIntent().getStringExtra("path");
        String circleName = getIntent().getStringExtra("circleName");
        Query qurey = FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/schedule/plan/"+path+"/attendance").orderByValue();
        Log.d("attendanceCheck","circle/"+circleName+"/schedule/plan/"+path+"/attendance");
        qurey.addListenerForSingleValueEvent(postListener);
    }
}
