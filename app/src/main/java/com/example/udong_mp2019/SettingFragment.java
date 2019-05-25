package com.example.udong_mp2019;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

public class SettingFragment extends Fragment {
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    ListView LV_memberList;
    ListView LV_requestJoin;
    ArrayAdapter<String> add_memberList;
    ArrayAdapter<CircleInfoForDB> add_requestJoin;
    FirebaseUser user;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");
    private DatabaseReference userRef = mRootRef.child("user");
    //UserInfoForDB get;
    String getName;
    String getStudentId;
    String getKey;

    static ArrayList<String> arrayMemberList = new ArrayList<>();
    String userUid;
    Activity root=getActivity();
    private FirebaseAuth firebaseAuth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.udong_mp2019.CircleListActivity CircleName= new com.example.udong_mp2019.CircleListActivity();
        final String circleName=CircleName.CircleName();
        Toast.makeText(getContext(),"동아리"+circleName, Toast.LENGTH_LONG).show();
        add_memberList= new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = circleRef;
      /*  query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayMemberList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("listfinder",postSnapshot.toString());
                    userUid=postSnapshot.child(circleName+"/member/").getKey().toString();

                    if(!postSnapshot.child(circleName+"/member/uid/autority").getValue().toString().equals("requestor") ){
                        getKey = postSnapshot.child(circleName+"/member/uid").getKey().toString();
                        arrayMemberList.add(getKey);
                    }
                    getKey= postSnapshot.child("/member/").getKey().toString();
                    arrayMemberList.add(getKey);
                }
                add_memberList.clear();
                add_memberList.addAll(arrayMemberList);
                add_memberList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

       Query query2 = userRef;
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayMemberList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("listfinder",postSnapshot.toString());

                /* if(!postSnapshot.child(circleName+"/member/uid/autority").getValue().toString().equals("requestor") ){
                       get = postSnapshot.child(circleName+"/member/uid").getKey().toString();
                       arrayMemberList.add(get);
                    }*/
                    getName= postSnapshot.child("/name/").getValue().toString();
                    getStudentId= postSnapshot.child("/studentId/").getValue().toString();
                    arrayMemberList.add(getStudentId+" "+getName);
                }
                add_memberList.clear();
                add_memberList.addAll(arrayMemberList);
                add_memberList.notifyDataSetChanged();
            }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });
    /* LV_memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                AlertDialog.Builder ad = new AlertDialog.Builder(root);

                ad.setTitle("강퇴");       // 제목 설정
                ad.setMessage("강퇴 시키겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.v(TAG,"Yes Btn Click");
                        Toast.makeText(root,"강퇴 시켰습니다 ", Toast.LENGTH_LONG).show();
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });


                // 취소 버튼 설정
                ad.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup setting = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        LV_memberList = setting.findViewById(R.id.LV_memberList);
        LV_requestJoin = setting.findViewById(R.id.LV_requestJoin);

        LV_memberList.setAdapter(add_memberList);




        return setting;
    }



}