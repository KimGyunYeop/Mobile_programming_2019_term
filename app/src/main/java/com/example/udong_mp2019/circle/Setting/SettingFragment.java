package com.example.udong_mp2019.circle.Setting;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.example.udong_mp2019.R;
import com.example.udong_mp2019.circleList.MemberInfoForDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

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
    ArrayAdapter<String> add_requestJoin;
    FirebaseUser user;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef;
    private DatabaseReference userRef = mRootRef.child("user");
    //UserInfoForDB get;
    String getName;
    String getStudentId;
    String getKey;
    String circleName;
    String[] memberUid=new String[150];
    int memberCount;
    int requestorCount;
    String[] requestorUid=new String[150];
    Query query;
    Query query2;
    int count, checked ;

    static ArrayList<String> arrayMemberList = new ArrayList<>();
    static ArrayList<String> arrayrequestList = new ArrayList<>();
    String userUid;
    Activity root=getActivity();
    private FirebaseAuth firebaseAuth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            Bundle bundle=getArguments();
            String circle=bundle.getString("circleName");
            circleName = circle;
            Log.d("circleName!!!",circleName);

        }


        user = FirebaseAuth.getInstance().getCurrentUser();
        circleRef = mRootRef.child("circle").child(circleName).child("member");
        query = circleRef;


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                memberCount=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String findMember = postSnapshot.getKey().toString();
                    Log.d("첫번째 listfinder", findMember);
                    memberUid[memberCount] = findMember;
                    memberCount++;

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        circleRef = mRootRef.child("circle").child(circleName).child("request");
        query = circleRef;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                requestorCount=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String findrequest = postSnapshot.getKey().toString();
                    if( !postSnapshot.getValue().equals("가입 승인")) {
                        Log.d("두번째 listfinder", findrequest);
                        requestorUid[requestorCount] = findrequest;
                        requestorCount++;
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        query2 = userRef;
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayMemberList.clear();
                arrayrequestList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for(int i=0;i<memberCount;i++) {
                        String findMember=postSnapshot.getKey().toString();
                        Log.d("listfinder",findMember);

                        if(findMember.equals(memberUid[i])) {
                            getName = postSnapshot.child("/name/").getValue().toString();
                            getStudentId = postSnapshot.child("/studentId/").getValue().toString();
                            arrayMemberList.add(getStudentId + " " + getName);
                            Log.d("member listfinder",findMember);
                        }
                    }

                    for(int i=0;i<requestorCount;i++) {
                        String find1 = postSnapshot.getKey().toString();
                        Log.d("listfinder", find1);
                        if (find1.equals(requestorUid[i])) {
                            getName = postSnapshot.child("/name/").getValue().toString();
                            getStudentId = postSnapshot.child("/studentId/").getValue().toString();
                            arrayrequestList.add(getStudentId + " " + getName);
                            Log.d("request listfinder", find1);

                        }
                    }
                }
                add_memberList.clear();
                add_memberList.addAll(arrayMemberList);
                add_memberList.notifyDataSetChanged();
                add_requestJoin.clear();
                add_requestJoin.addAll(arrayrequestList);
                add_requestJoin.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup setting = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        add_memberList= new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);
        add_requestJoin= new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);

        LV_memberList = setting.findViewById(R.id.LV_memberList);
        LV_requestJoin = setting.findViewById(R.id.LV_requestJoin);

        LV_memberList.setAdapter(add_memberList);
        LV_requestJoin.setAdapter(add_requestJoin);

        LV_memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checked=position;
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

                ad.setTitle("강퇴");       // 제목 설정
                ad.setMessage("강퇴 시키겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.v(TAG,"Yes Btn Click");
                        Log.d("체크ㅡㅡ",""+checked+arrayMemberList);
                        arrayMemberList.remove(checked);
                        Log.d("체크ㅡㅡ",""+checked+arrayMemberList);

                        LV_memberList.clearChoices();

                        // listview 갱신.
                        add_memberList.notifyDataSetChanged();
                        LV_memberList.setAdapter(add_memberList);

                        circleRef = mRootRef.child("circle").child(circleName).child("member");
                        circleRef.child(memberUid[checked]).child("autority").setValue("secession");
                        Toast.makeText(getContext(),"강퇴 시켰습니다 ", Toast.LENGTH_LONG).show();
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

                checked=position;
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

                ad.setTitle("가입 승인");       // 제목 설정
                ad.setMessage("가입 승인 시키겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.v(TAG,"Yes Btn Click");

                        count = add_requestJoin.getCount() ;

                        arrayrequestList.remove(checked);

                        // listview 선택 초기화.
                        LV_requestJoin.clearChoices();

                        // listview 갱신.
                        add_requestJoin.notifyDataSetChanged();

                        LV_requestJoin.setAdapter(add_requestJoin);

                        MemberInfoForDB memberInfoForDB= new MemberInfoForDB("member",new Date().toString());

                        Log.d("가입 시키자",requestorUid[checked]);
                        mRootRef.child("circle").child(circleName).child("request").child(requestorUid[checked]).setValue("가입 승인");
                        MemberInfoForDB memberInfoForDB2= new MemberInfoForDB("member",new Date().toString());
                        circleRef = mRootRef.child("circle").child(circleName).child("member");
                        circleRef.child(requestorUid[checked]).setValue(memberInfoForDB2);
                        Toast.makeText(getContext(),"동아리에 가입 시켰습니다 ", Toast.LENGTH_LONG).show();
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

        return setting;
    }



}