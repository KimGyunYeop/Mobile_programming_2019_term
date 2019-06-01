package com.example.udong_mp2019.circle.Schedule;

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

public class CutomAdapterChedkAttendance extends BaseAdapter {
    final Context context;
    LayoutInflater inflter;
    String path;
    ArrayList<String> uid;
    private String circleName;

    public CutomAdapterChedkAttendance (Context applicationContext, ArrayList<String> uid, String circleName, String path) {
        this.context = applicationContext;
        this.uid = uid;
        this.circleName = circleName;
        this.path =path;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(ArrayList<String> uid) {
        this.uid = uid;
    }


    @Override
    public int getCount() {
        return uid.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.listview_check_attendance, null);
        TextView name = convertView.findViewById(R.id.TV_listVIewAttendanceName);
        TextView attendance = convertView.findViewById(R.id.TV_listVIewAttendanceCheck);

        FirebaseDatabase.getInstance().getReference().child("user/"+uid.get(position)+"/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    name.setText(dataSnapshot.getValue().toString());
                }catch (Exception e){
                    Log.d("error","CustomAdapterCheckAttendance");
                    name.setText("error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/schedule/plan/"+path+"/attendance/"+uid.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() == null || !(Boolean)dataSnapshot.getValue()){
                    attendance.setText("X");
                }else{
                    attendance.setText("O");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return convertView;
    }
}

