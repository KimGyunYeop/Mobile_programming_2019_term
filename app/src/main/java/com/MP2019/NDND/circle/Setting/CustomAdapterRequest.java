package com.MP2019.NDND.circle.Setting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.MP2019.NDND.R;
import com.MP2019.NDND.circleList.CircleInfoForDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterRequest extends BaseSwipeAdapter {
    final Context context;
    LayoutInflater inflter;
    ArrayList<String> uid;
    String circleName, memberAuth = "";
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childRef = ref.child("user");

    public CustomAdapterRequest(Context applicationContext, ArrayList<String> uid , String circleName) {
        Log.d("errorDectection",applicationContext.toString());
        this.context = applicationContext;
        Log.d("errorDectection",context.toString());
        this.uid = uid;
        this.circleName = circleName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(ArrayList<String> uid) {
        Log.d("errorDectection",context.toString());
        this.uid = uid;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_join_request;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        Log.d("errorDectection","fillValues"+context.toString());
        return LayoutInflater.from(context).inflate(R.layout.listview_join_request, null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView tv_stdID = convertView.findViewById(R.id.TV_listVIewMemberStdID_request);
        TextView tv_name = convertView.findViewById(R.id.TV_listVIewMemberName_request);
        LinearLayout delete_join_request = convertView.findViewById(R.id.delete_join_request);
        Log.d("errorDectection","fillValues"+context.toString());
        SwipeLayout swipeLayout= convertView.findViewById(R.id.swipe_join_request);

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

        childRef.child(uid.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tv_name.setText(dataSnapshot.child("name").getValue().toString());
                tv_stdID.setText(dataSnapshot.child("studentId").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delete_join_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("circle/"+circleName+"/request/"+uid.get(position)).removeValue();
            }
        });
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
        return position;
    }

    public Context getContext() {
        return context;
    }
}
