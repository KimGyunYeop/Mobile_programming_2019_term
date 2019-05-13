package com.example.udongmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lv_searchResult, lv_myCircleList;
    ImageButton bu_createCircle, bu_search;
    ArrayAdapter aad_searchResult, aad_myCircleList;
    EditText et_searchCircle;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circlrInfoRef = mRootRef.child("circle");
    static ArrayList<String> arrayData = new ArrayList<String>();
    static ArrayList<String> arrayIndex = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_searchResult = findViewById(R.id.LV_searchResult);
        lv_myCircleList = findViewById(R.id.LV_myCircleList);
        bu_search = findViewById(R.id.IB_searchCircle);
        bu_createCircle = findViewById(R.id.IB_createCircle);
        et_searchCircle = findViewById(R.id.ET_searchCircle);

        aad_myCircleList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 );
        lv_myCircleList.setAdapter(aad_myCircleList);

        aad_searchResult = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 );
        lv_searchResult.setAdapter(aad_searchResult);

        bu_createCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CircleRegisterFormActivity.class);
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

    public void getFirebaseDatabase(final String str){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    System.out.println(postSnapshot);
                    System.out.println("a");
                    String key = postSnapshot.getKey();
                    circleInfoForDB get = postSnapshot.child("info").getValue(circleInfoForDB.class);
                    String Result = get.getSchool()+":"+get.getName();
                    if(get.getSchool().toLowerCase().contains(str)||get.getName().toLowerCase().contains(str)) {
                        arrayData.add(Result);
                        arrayIndex.add(key);
                    }
                }
                aad_searchResult.clear();
                aad_searchResult.addAll(arrayData);
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
