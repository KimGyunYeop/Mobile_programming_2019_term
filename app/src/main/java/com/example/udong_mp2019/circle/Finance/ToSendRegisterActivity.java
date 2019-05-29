package com.example.udong_mp2019.circle.Finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.udong_mp2019.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ToSendRegisterActivity extends AppCompatActivity {
    Button btn_register;

    EditText et_name;
    EditText et_amount;

    DatePicker dp;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");

    private DatabaseReference toSendRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_send_register);

        et_name = (EditText) findViewById(R.id.et_name);
        et_amount = (EditText) findViewById(R.id.et_amount);
        dp=(DatePicker) findViewById(R.id.datepicker);

        Intent intent = getIntent();
        String circlename = ((Intent) intent).getStringExtra("circlename");

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSendRef = mRootRef.child("circle/" + circlename + "/schedule/tosend");
                String date=dp.getYear()+"-"+dp.getMonth()+"-"+dp.getDayOfMonth();
                toSendRef.child(date).child("name").setValue(et_name.getText().toString());
                toSendRef.child(date).child("amount").setValue(et_amount.getText().toString());
                setCheckFinanceFirebaseDatabase(date);
                finish();
            }

            void setCheckFinanceFirebaseDatabase(String date){
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            toSendRef.child(date+"/member/"+key).setValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                Query qurey = FirebaseDatabase.getInstance().getReference().child("circle/"+circlename+"/member").orderByKey();
                qurey.addListenerForSingleValueEvent(postListener);
            }
        });
    }
}
