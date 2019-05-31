package com.example.udong_mp2019.circle.Setting;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.udong_mp2019.R;
import com.example.udong_mp2019.circleList.CircleRegisterFormActivity;
import com.example.udong_mp2019.circleList.MemberInfoForDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Date;

public class SettingFragment extends Fragment {
    ListView LV_memberList;
    ListView LV_requestJoin;
    CustomAdapterRequest add_requestJoin;
    CustomAdapterMember add_memberList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef;
    String circleName;
    ArrayList<String> uid = new ArrayList<>();
    ArrayList<String> uid_requset = new ArrayList<>();
    ArrayList<String> autority = new ArrayList<>();
    int checked ;
    String  memberAuth;
    Button B_secession;


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            Bundle bundle=getArguments();
            String circle=bundle.getString("circleName");
            circleName = circle;
            Log.d("circleName!!!",circleName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup setting = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        add_memberList= new CustomAdapterMember(getActivity(),uid,autority,circleName);
        add_requestJoin= new CustomAdapterRequest(getActivity(),uid,circleName);

        LV_memberList = setting.findViewById(R.id.LV_memberList);
        LV_requestJoin = setting.findViewById(R.id.LV_requestJoin);

        LV_memberList.setAdapter(add_memberList);
        LV_requestJoin.setAdapter(add_requestJoin);
        B_secession=setting.findViewById(R.id.secession);

        FirebaseDatabase.getInstance().getReference().child("circle").child(circleName).child("member").child(user.getUid()).child("autority").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberAuth = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        B_secession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                Log.d("errorDectection",getActivity().toString());
                Log.d("errorDectection",getContext().toString());

                ad.setTitle("탈퇴");       // 제목 설정
                ad.setMessage(circleName+ " 동아리 탈퇴 하시겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        circleRef = mRootRef.child("circle").child(circleName).child("member");
                        circleRef.child(user.getUid()).child("autority").setValue("secession");
                        Toast.makeText(getContext(), circleName+" 동아리 탈퇴하였습니다 ", Toast.LENGTH_LONG).show();
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

        });

        LV_requestJoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (memberAuth.equals("manager")) {
                    checked = position;
                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

                    ad.setTitle("가입 승인");       // 제목 설정
                    ad.setMessage("가입 승인 시키겠습니까?");   // 내용 설정

                    // 확인 버튼 설정
                    ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CustomAdapterRequest item = (CustomAdapterRequest)parent.getAdapter();
                            String uid = item.uid.get(position);
                            mRootRef.child("circle/"+circleName+"/request").child(uid).removeValue();
                            MemberInfoForDB memberInfoForDB2 = new MemberInfoForDB("member", new Date().toString());
                            mRootRef.child("circle/"+circleName+"/member").child(uid).setValue(memberInfoForDB2);
                            Toast.makeText(getContext(), "동아리에 가입 시켰습니다 ", Toast.LENGTH_LONG).show();
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
            }
        });

        return setting;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRootRef.child("circle").child(circleName).child("member").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid.clear();
                autority.clear();
                Log.d("memberList",dataSnapshot.toString());
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    uid.add(postSnapshot.getKey());
                    autority.add(postSnapshot.child("autority").getValue().toString());

                }
                add_memberList.reset(uid,autority);
                add_memberList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRootRef.child("circle/"+circleName+"/request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid_requset.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                   uid_requset.add(postSnapshot.getKey());
                }
                add_requestJoin.reset(uid_requset);
                add_requestJoin.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}