package com.example.udong_mp2019.circle.Schedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.udong_mp2019.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ScheduleRegisterFormActivity extends AppCompatActivity {
    Button bu_submit;
    EditText et_name,et_descripsion;
    TimePicker tp_time;
    String circleName;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference planRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule_register_form);
        String circleName = getIntent().getStringExtra("circleName");
        planRef = mRootRef.child("circle/"+circleName+"/schedule/plan");
        bu_submit = findViewById(R.id.Bu_submit_schedule);
        tp_time = findViewById(R.id.TP_time);
        et_name = findViewById(R.id.ET_schedule_name);
        et_descripsion=findViewById(R.id.ET_descripsion);
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        bu_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                ScheduleInfoForDB data = new ScheduleInfoForDB(et_name.getText().toString(),et_descripsion.getText().toString(),tp_time.getCurrentHour()+":"+tp_time.getCurrentMinute(),intent.getStringExtra("date"));
                Log.d("date","dasf");
                Log.d("date","e"+data.toString());
                String path = data.toString();
                planRef.child(path+"/info").setValue(data);
                setAttendFirebaseDatabase(path);
                Toast.makeText(ScheduleRegisterFormActivity.this, "일정이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            public void setAttendFirebaseDatabase(String path){
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            planRef.child(path+"/attendance/"+key).setValue(false);
                            Log.d("date",key);
                            Log.d("date",path);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                Query qurey = FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/member").orderByKey();
                qurey.addListenerForSingleValueEvent(postListener);
            }
        });
    }


}
