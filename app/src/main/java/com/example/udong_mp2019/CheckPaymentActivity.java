package com.example.udong_mp2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CheckPaymentActivity extends AppCompatActivity {
    ListView LV_members;
    ArrayList<String> arrayFormembers= new ArrayList<String>();
    ArrayList<String> arrayIndex = new ArrayList<>();
    ArrayAdapter<String> add_members;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");
    private DatabaseReference userRef = mRootRef.child("user");

    String circleName;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);

        Intent receive =getIntent();
        circleName=receive.getStringExtra("circleName");

        LV_members=(ListView) findViewById(R.id.LV_memberList);

        add_members= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayFormembers);
        LV_members.setAdapter(add_members);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Query query = circleRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(circleName,"name");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key= postSnapshot.getKey();
                    if(key.equals(circleName)){
                        Query query= circleRef.child(key).child("member");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                arrayFormembers.clear();
                                arrayIndex.clear();
                                for(DataSnapshot postSnapshot2: dataSnapshot.getChildren()){
                                    String key2= postSnapshot2.getKey();
                                    //           Log.d("key2는"+key2,"start");
//                                    String name=userRef.child(key2).child("name").toString();
                                    getFirebaseDatabase1(key2);
                                    Log.d("유저이름은 "+user,"이름");
                                    arrayFormembers.add(user);
                                    arrayIndex.add(key2);
                                }
                                add_members.clear();
                                add_members.addAll(arrayFormembers);
                                add_members.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
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

