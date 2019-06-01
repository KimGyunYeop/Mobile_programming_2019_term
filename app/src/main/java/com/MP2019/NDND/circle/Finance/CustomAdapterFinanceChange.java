package com.MP2019.NDND.circle.Finance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.MP2019.NDND.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterFinanceChange extends BaseAdapter {
    final Context context;
    LayoutInflater inflter;
    ArrayList<String> uid;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String circleName, date,planName;

    public CustomAdapterFinanceChange(Context applicationContext, ArrayList<String> UID, String circleName, String date, String planName) {
        this.context = applicationContext;
        this.uid =UID;
        this.circleName = circleName;
        this.planName =planName;
        this.date = date;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(Context applicationContext, ArrayList<String> UID) {
        this.uid =UID;
    }

    @Override
    public int getCount() {
        return uid.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.listview_finance_change, null);
        TextView tv_name = view.findViewById(R.id.TV_listVIewFinanceChangeName);
        TextView tv_stdID = view.findViewById(R.id.TV_listVIewFinanceChangeStdID);
        RadioButton yes = (RadioButton) view.findViewById(R.id.RB_financeChangeYes);
        RadioButton no = (RadioButton) view.findViewById(R.id.RB_financeChangeNo);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseDatabase.getInstance().getReference().child("user/"+uid.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("AdapterFinanceChange",dataSnapshot.toString());
                tv_name.setText(dataSnapshot.child("name").getValue().toString());
                tv_stdID.setText(dataSnapshot.child("studentId").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/schedule/tosend/"+date).child(planName+"/member/"+uid.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("AdapterFinanceChange",dataSnapshot.toString());
                Boolean check = (Boolean)dataSnapshot.getValue();
                if(check != null) {
                    if (check) {
                        yes.setChecked(true);
                    } else {
                        no.setChecked(true);
                    }
                }else{
                    no.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ref.child("circle/"+circleName+"/schedule/tosend/"+date+"/"+planName+"/member/"+uid.get(i)).setValue(true);
                }
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ref.child("circle/"+circleName+"/schedule/tosend/"+date+"/"+planName+"/member/"+uid.get(i)).setValue(false);
                }
            }
        });
        return view;
    }
}