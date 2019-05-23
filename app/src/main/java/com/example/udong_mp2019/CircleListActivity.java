package com.example.udong_mp2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class CircleListActivity extends AppCompatActivity {

    ListView lv_searchResult, lv_myCircleList;
    ImageButton bu_createCircle, bu_search;
    ArrayAdapter<CircleInfoForDB> aad_searchResult;
    ArrayAdapter<CircleInfoForDB> aad_myCircleList;
    EditText et_searchCircle;
    FirebaseUser user;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");
    static ArrayList<CircleInfoForDB> arrayDataforsearch = new ArrayList<>();
    static ArrayList<CircleInfoForDB> arrayDataForMyCircleList = new ArrayList<>();
    static ArrayList<String> arrayIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_list);

        lv_searchResult = findViewById(R.id.LV_searchResult);
        lv_myCircleList = findViewById(R.id.LV_myCircleList);
        bu_search = findViewById(R.id.IB_searchCircle);
        bu_createCircle = findViewById(R.id.IB_createCircle);
        et_searchCircle = findViewById(R.id.ET_searchCircle);

        aad_myCircleList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lv_myCircleList.setAdapter(aad_myCircleList);
        //해당 동아리 메뉴 페이지로 이동
        lv_myCircleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CircleListActivity.this, "동아리 메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), com.example.udong_mp2019.CircleMainActivity.class);

                String circleName=parent.getItemAtPosition(position).toString();
                Toast.makeText(CircleListActivity.this, circleName, Toast.LENGTH_LONG).show();
                //동아리 이름 넘김
                intent.putExtra("circle",circleName);
                // 회원 id 넘김
                intent.putExtra("user",user.getUid());
                startActivity(intent);
            }
        });


        aad_searchResult = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lv_searchResult.setAdapter(aad_searchResult);

        bu_createCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.udong_mp2019.CircleRegisterFormActivity.class);
                startActivity(intent);
            }
        });

        bu_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFirebaseDatabase(et_searchCircle.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("circlefinder","start");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = circleRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            CircleInfoForDB get;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayDataForMyCircleList.clear();
                Log.d("circlefinder",dataSnapshot.toString());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("circlefinder",postSnapshot.toString());
                    Log.d("circlefinder",postSnapshot.child("member/"+user.getUid()).toString());
                    if(postSnapshot.child("member/"+user.getUid()).hasChildren()) {
                        get = postSnapshot.child("info").getValue(CircleInfoForDB.class);
                        arrayDataForMyCircleList.add(get);
                    }
                }

                aad_myCircleList.clear();
                aad_myCircleList.addAll(arrayDataForMyCircleList);
                aad_myCircleList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFirebaseDatabase(final String str){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayDataforsearch.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    CircleInfoForDB get = postSnapshot.child("info").getValue(CircleInfoForDB.class);
                    if(get.getSchool().toLowerCase().contains(str)||get.getName().toLowerCase().contains(str)) {
                        arrayDataforsearch.add(get);
                        arrayIndex.add(key);
                    }
                }
                aad_searchResult.clear();
                aad_searchResult.addAll(arrayDataforsearch);
                aad_searchResult.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("circle").orderByKey();
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
