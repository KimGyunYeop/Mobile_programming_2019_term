package com.example.udong_mp2019.circle.Finance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.udong_mp2019.R;
import com.example.udong_mp2019.circleList.CircleInfoForDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterReceipt extends BaseSwipeAdapter {
    final Context context;
    LayoutInflater inflter;
    ArrayList<String> due, amount, name;
    String circleName, memberAuth = "";

    public CustomAdapterReceipt(Context applicationContext, ArrayList<String> due , ArrayList<String> amount, ArrayList<String> name, String circleName) {
        this.context = applicationContext;
        this.due = due;
        this.amount = amount;
        this.name = name;
        this.circleName =circleName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(Context applicationContext, ArrayList<String> due , ArrayList<String> amount, ArrayList<String> name) {
        this.due = due;
        this.amount = amount;
        this.name = name;
    }

    @Override
    public int getCount() {
        return due.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_receipt;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return  LayoutInflater.from(context).inflate(R.layout.listview_receipt, null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        int i = position;
        View view = convertView;
        TextView tv_due = view.findViewById(R.id.TV_listVIewReceiptDate);
        TextView tv_amount = view.findViewById(R.id.TV_listVIewReceiptAmount);
        TextView tv_name = view.findViewById(R.id.TV_listVIewReceiptPlanName);
        LinearLayout delete_receipt = view.findViewById(R.id.delete_receipt);
        SwipeLayout swipeLayout= view.findViewById(R.id.swipe_receipt);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(memberAuth.equalsIgnoreCase("")) {
            Query query = FirebaseDatabase.getInstance().getReference().child("circle/" + circleName + "/member/" + user.getUid() + "/autority");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                CircleInfoForDB get;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("circlefinder", dataSnapshot.toString());
                    memberAuth = dataSnapshot.getValue().toString();
                    if (memberAuth.equalsIgnoreCase("member")) {
                        swipeLayout.setRightSwipeEnabled(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else{
            if (memberAuth.equalsIgnoreCase("member")) {
                swipeLayout.setRightSwipeEnabled(false);
            }
        }

        delete_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/schedule/receipt/"+tv_due.getText().toString()).child(tv_name.getText().toString()).removeValue();
                Log.d("delete_receipt","circle/"+circleName+"/schedule/receipt/"+tv_due.getText().toString()+"/"+(tv_name.getText().toString()));
            }
        });

        tv_due.setText(due.get(i));
        tv_amount.setText(amount.get(i));
        tv_name.setText(name.get(i));
    }
}