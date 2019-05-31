package com.example.udong_mp2019.circle.Setting;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.udong_mp2019.R;
import com.example.udong_mp2019.circle.CircleMainActivity;
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

public class CustomAdapterMember extends BaseSwipeAdapter {
    final Context context;
    LayoutInflater inflter;
    ArrayList<String> uid,autority;
    String circleName, memberAuth = "";
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childRef = ref.child("user");

    public CustomAdapterMember(Context applicationContext, ArrayList<String> uid , ArrayList<String> autority, String circleName) {
        Log.d("errorDectection",applicationContext.toString());
        this.context = applicationContext;
        Log.d("errorDectection",context.toString());
        this.uid = uid;
        this.autority = autority;
        this.circleName = circleName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(ArrayList<String> uid , ArrayList<String> autority) {
        Log.d("errorDectection",context.toString());
        this.uid = uid;
        this.autority = autority;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_member;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        Log.d("errorDectection","fillValues"+context.toString());
        return LayoutInflater.from(context).inflate(R.layout.listview_member, null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView tv_stdID = convertView.findViewById(R.id.TV_listVIewMemberStdID);
        TextView tv_name = convertView.findViewById(R.id.TV_listVIewMemberName);
        LinearLayout autority_change = convertView.findViewById(R.id.change_autority_member);
        LinearLayout delete_member = convertView.findViewById(R.id.delete_member);
        Log.d("errorDectection","fillValues"+context.toString());

        SwipeLayout swipeLayout= convertView.findViewById(R.id.swipe_member);

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

        autority_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);

                ad.setTitle("권한 변경");
                if(autority.get(position).equals("manager")) {
                    ad.setMessage("일반회원로 변경시키겠습니까?");
                }
                else {       // 제목 설정
                    ad.setMessage("매니저으로 변경시키겠습니까?");   // 내용 설정
                }

                // 확인 버튼 설정
                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("test_autority_change",autority.get(position));
                        if(autority.get(position).equals("member")) {
                            ref.child("circle/" + circleName + "/member/" + uid.get(position)+"/autority").setValue("manager");
                            Log.d("test_autority_change","circle/" + circleName + "/member/" + uid.get(position)+"/autority");
                            Toast.makeText(convertView.getContext(), tv_name.getText()+"님이 매니저로 변경되었습니다.", Toast.LENGTH_LONG).show();
                        }else {
                            ref.child("circle/" + circleName + "/member/" + uid.get(position)+"/autority").setValue("member");
                            Toast.makeText(convertView.getContext(), tv_name.getText()+"님이 일반회원으로 변경되었습니다.", Toast.LENGTH_LONG).show();
                        }
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

                AlertDialog dialog = ad.create();
                // 창 띄우기
                dialog.show();
            }
        });

        delete_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);

                ad.setTitle("강퇴");       // 제목 설정
                ad.setMessage("강퇴 시키겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(autority.get(position).equals("manager")) {
                            Toast.makeText(convertView.getContext(), "매니저는 강퇴할 수 없습니다", Toast.LENGTH_LONG).show();
                        }
                        else {
                            ref.child("circle/" + circleName + "/member/" + uid.get(position)).removeValue();
                            Toast.makeText(convertView.getContext(), "강퇴 시켰습니다 ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();     //닫기
                        }
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

                AlertDialog dialog = ad.create();
                // 창 띄우기
                dialog.show();
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
