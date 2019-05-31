package com.example.udong_mp2019.circle.Finance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.udong_mp2019.R;
import com.example.udong_mp2019.circleList.CircleInfoForDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountingFragment extends Fragment {
    ListView LV_receipts, LV_toSend;
    ImageButton btn_plusReceipt, btn_plustoSend;
    CustomAdapterReceipt add_receipts;
    CustomAdapterFinanceCheck add_toSend;
    FirebaseUser user;

    static ArrayList<String> cafc_name = new ArrayList<>();
    static ArrayList<String> cafc_due = new ArrayList<>();
    static ArrayList<String> cafc_amount = new ArrayList<>();
    static ArrayList<Boolean> cafc_check = new ArrayList<>();

    static ArrayList<String> car_name = new ArrayList<>();
    static ArrayList<String> car_due = new ArrayList<>();
    static ArrayList<String> car_amount = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    String memberAuth=""; // memeber인지 manager인지
    String circleName ="", userid="";

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
        btn_plusReceipt=(ImageButton)v.findViewById(R.id.btn_plusReceipt);
        btn_plustoSend=(ImageButton)v.findViewById(R.id.btn_plustoSend);

        Bundle bundle=getArguments();
        if(bundle!=null) {
            circleName = bundle.getString("circleName");
            userid = bundle.getString("userID");
        }

        getAutorityFirebase();

        add_toSend = new CustomAdapterFinanceCheck(getActivity().getApplicationContext(),cafc_due,cafc_amount,cafc_name,cafc_check, circleName);
        LV_toSend.setAdapter(add_toSend);

        LV_toSend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(memberAuth.equalsIgnoreCase("manager")) {
                    CustomAdapterFinanceCheck data = (CustomAdapterFinanceCheck) parent.getAdapter();
                    Intent intent = new Intent(getActivity().getApplicationContext(), CheckPaymentActivity.class);
                    intent.putExtra("circleName", circleName);
                    intent.putExtra("planName", data.name.get(position));
                    intent.putExtra("date", data.due.get(position));
                    startActivity(intent);
                }
            }
        });

        btn_plusReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReceiptRegisterActivity.class);
                intent.putExtra("circleName", circleName);
                startActivity(intent);
            }
        });
        btn_plustoSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ToSendRegisterActivity.class);
                intent.putExtra("circleName", circleName);
                startActivity(intent);
            }
        });


        add_receipts = new CustomAdapterReceipt(getActivity().getApplicationContext(),car_due,car_amount,car_name, circleName);
        LV_receipts.setAdapter(add_receipts);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("circlefinder","start");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference().child("circle/"+ circleName +"/schedule/receipt");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                car_due.clear();
                car_name.clear();
                car_amount.clear();
                Log.d("getReceipt",dataSnapshot.toString());
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("getReceipt",postSnapshot.toString());
                    String due = postSnapshot.getKey();
                    Log.d("getReceipt",postSnapshot.toString());
                    for(DataSnapshot childSnapshot: postSnapshot.getChildren()){
                        Log.d("getReceipt",childSnapshot.toString());
                        car_due.add(due);
                        car_name.add(childSnapshot.getKey());
                        car_amount.add(childSnapshot.child("amount").getValue().toString());
                        Log.d("AF_getToSend",childSnapshot.toString());
                    }
                }
                add_receipts.reset(getActivity(),car_due,car_amount,car_name);
                add_receipts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("circle/"+ circleName +"/schedule/tosend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                cafc_due.clear();
                cafc_amount.clear();
                cafc_name.clear();
                cafc_check.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String due = postSnapshot.getKey();
                    Log.d("AF_getToSend",postSnapshot.toString());
                    for(DataSnapshot childSnapshot: postSnapshot.getChildren()){
                        Boolean myCheck = (Boolean)childSnapshot.child("member").child(user.getUid()).getValue();
                        cafc_check.add(myCheck);
                        Log.d("nullTest",cafc_check.toString());
                        cafc_due.add(due);
                        cafc_name.add(childSnapshot.getKey());
                        cafc_amount.add(childSnapshot.child("amount").getValue().toString());
                        Log.d("AF_getToSend",cafc_check.toString());
                    }
                }

                Log.d("toSend",cafc_check.toString());
                add_toSend.reset(getContext(),cafc_due,cafc_amount,cafc_name,cafc_check);
                add_toSend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // autority 반환
    public void getAutorityFirebase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference().child("circle/"+ circleName +"/member/"+user.getUid()+"/autority");
        Log.d("circlefinder","circle/"+ circleName +"/member/"+user.getUid()+"/autority");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("circlefinder",dataSnapshot.toString());
                memberAuth = dataSnapshot.getValue().toString();
                Log.d("circlefinder",memberAuth );
                if(!memberAuth.equalsIgnoreCase("manager")) {
                    //납부회원 체크 페이지로 이동
                    btn_plusReceipt.setVisibility(View.INVISIBLE);
                    btn_plustoSend.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}