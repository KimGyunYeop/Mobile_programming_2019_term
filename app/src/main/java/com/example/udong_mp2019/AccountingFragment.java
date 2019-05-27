package com.example.udong_mp2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class AccountingFragment extends Fragment {
    ListView LV_receipts, LV_toSend;
    ArrayAdapter<String> add_receipts;
    ArrayAdapter<String> add_toSend;
    FirebaseUser user;

    static ArrayList<String> arrayDataForReceipts = new ArrayList<>();
    static ArrayList<String> arrayDataForToSend = new ArrayList<>();

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");

    static ArrayList<String> arrayIndex = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    String memberAuth; // memeber인지 manager인지
    String circlename="", userid="";

    public static AccountingFragment newInstance() {
        AccountingFragment fragment = new AccountingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_accounting, container, false);
        LV_receipts=(ListView)v.findViewById(R.id.LV_receipts);
        LV_toSend=(ListView)v.findViewById(R.id.LV_toSend);
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle=getArguments();

        if(bundle!=null) {
            circlename = bundle.getString("circleName");
            userid = bundle.getString("userID");
        }

        add_receipts = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
        LV_receipts.setAdapter(add_receipts);
        //권한 읽어옴
        getFirebaseDatabase1(userid);

        //해당 동아리 메뉴 페이지로 이동
        LV_receipts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (memberAuth.equals("manager")) {
                    Toast.makeText(getActivity().getApplicationContext(), "납부여부 체크 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), com.example.udong_mp2019.CheckPaymentActivity.class);
                    String circleName = parent.getItemAtPosition(position).toString();
                    //동아리 이름 넘김
                    intent.putExtra("circleName", circleName);
                    startActivity(intent);
                }
            }
        });

        add_toSend = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
        LV_toSend.setAdapter(add_toSend);

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("circlefinder","start");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = circleRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key= postSnapshot.getKey();
                    if(key.equals(circlename)){
                            Query query= circleRef.child(key).child("schedule").child("receipt");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    arrayDataForReceipts.clear();
                                    arrayIndex.clear();
                                    for(DataSnapshot postSnapshot2: dataSnapshot.getChildren()){
                                        String key2= postSnapshot2.getKey();
                                        Log.d("key는"+key2,"start");
                                        String amount= postSnapshot2.child("amount").getValue().toString();
                                        String date=postSnapshot2.child("date").getValue().toString();
                                        String alert=date+" "+key2+" "+amount+"원";
                                        Log.d("alert는"+alert, "start");
                                        arrayDataForReceipts.add(alert);
                                        arrayIndex.add(key2);
                                    }
                                    add_receipts.clear();
                                    add_receipts.addAll(arrayDataForReceipts);
                                    add_receipts.notifyDataSetChanged();
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

        Query query3 = circleRef;
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key= postSnapshot.getKey();
                    Log.d("ㄴkey는"+key,"start");
                    if(key.equals(circlename)){
                        Log.d("찾았다","start");
                        Query query= circleRef.child(key).child("schedule").child("finance");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                arrayDataForToSend.clear();
                                arrayIndex.clear();
                                for(DataSnapshot postSnapshot2: dataSnapshot.getChildren()){
                                    String key2= postSnapshot2.getKey();
                                    Log.d("key는"+key2,"start");
                                    String amount= postSnapshot2.child("amount").getValue().toString();
                                    String date=postSnapshot2.child("date").getValue().toString();
                                    String alert=date+" "+key2+" "+amount+"원";
                                    Log.d("alert는"+alert, "start");
                                    arrayDataForToSend.add(alert);
                                    arrayIndex.add(key2);
                                }
                                add_toSend.clear();
                                add_toSend.addAll(arrayDataForToSend);
                                add_toSend.notifyDataSetChanged();
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
    // autority 반환
    public void getFirebaseDatabase1(final String str){
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = circleRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            CircleInfoForDB get;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("circlefinder",dataSnapshot.toString());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Log.d("circlefinder",postSnapshot.toString());
                    if(key.equals(circlename) && postSnapshot.child("member/"+user.getUid()).hasChildren()) {
                            memberAuth=postSnapshot.child("member/"+user.getUid()+"/autority").getValue().toString();
                            get = postSnapshot.child("info").getValue(CircleInfoForDB.class);
                            Log.d("권한은"+memberAuth,"start");
                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
