package com.example.udong_mp2019.circle.Finance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.udong_mp2019.R;
import com.example.udong_mp2019.login.UserInfoForDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CheckPaymentActivity extends AppCompatActivity {
    ListView LV_members;
    CustomAdapterFinanceChange cafc_aad;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");
    private DatabaseReference userRef = mRootRef.child("user");

    ArrayList<String> uid = new ArrayList<>();
    ArrayList<Boolean> check = new ArrayList<>();
    String circleName, planName, due;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);

        Intent receive =getIntent();
        circleName=receive.getStringExtra("circleName");
        planName = receive.getStringExtra("planName");
        due = receive.getStringExtra("date");

        LV_members=(ListView) findViewById(R.id.LV_memberList);

        cafc_aad= new CustomAdapterFinanceChange(getApplicationContext(),uid,check,circleName,due,planName);
        LV_members.setAdapter(cafc_aad);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/schedule/tosend/"+due).child(planName+"/member").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid.clear();
                check.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    uid.add(postSnapshot.getKey());
                    check.add((Boolean)postSnapshot.getValue());
                }
                cafc_aad.reset(getApplicationContext(),uid,check);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // 유저이름 반환
    public void getFirebaseDatabase1(final String str){
        Query query = userRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            UserInfoForDB get;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("circlefinder",dataSnapshot.toString());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Log.d("circlefinder",postSnapshot.toString());
                    if(key.equals(str)){
                        get = postSnapshot.child("name").getValue(UserInfoForDB.class);
                        user=get.getName();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
