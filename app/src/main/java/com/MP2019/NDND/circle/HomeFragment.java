package com.MP2019.NDND.circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.MP2019.NDND.R;
import com.MP2019.NDND.circleList.CircleInfoForDB;
import com.MP2019.NDND.login.UserInfoForDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = mRootRef.child("user");
    private DatabaseReference circleRef = mRootRef.child("circle");

    TextView TV_hello;
    TextView TV_we;
    TextView TV_circlename;
    TextView TV_circlemain;
    TextView TV_circleInfo;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        AtomicReference<String> circle= new AtomicReference<>();
        AtomicReference<String> user= new AtomicReference<>();
        String circlename="", userid="";

        TV_hello=(TextView)v.findViewById(R.id.tv_hello);
        TV_circlename=(TextView) v.findViewById(R.id.tv_circlename);
        TV_circleInfo=(TextView) v.findViewById(R.id.tv_circleIntro);
        TV_circlemain=(TextView)v.findViewById(R.id.tv_circlemain);
        TV_we=(TextView) v.findViewById(R.id.tv_we);

        Bundle bundle=getArguments();

        if(bundle!=null){

            circlename=bundle.getString("circleName");
            userid=bundle.getString("userID");
            TV_circlemain.setText("메인 페이지입니다.");
            TV_we.setText("우리는!");
            getFirebaseDatabase1(userid);
            getFirebaseDatabase2(circlename);

        }
        return v;
    }
    public void getFirebaseDatabase1(final String str){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    UserInfoForDB get = postSnapshot.getValue(UserInfoForDB.class);

                    if(key.equals(str)) {
                        TV_hello.setText(get.getName() + " 님 안녕하세요!");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("user").orderByKey();
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public void getFirebaseDatabase2(final String str){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    CircleInfoForDB get = postSnapshot.child("info").getValue(CircleInfoForDB.class);
                    if(key.equals(str)) {
                        TV_circlename.setText(str);
                        TV_circleInfo.setText(get.getIntroduction());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("circle").orderByKey();
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }
}