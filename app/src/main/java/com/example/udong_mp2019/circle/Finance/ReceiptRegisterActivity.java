package com.example.udong_mp2019.circle.Finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.udong_mp2019.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

public class ReceiptRegisterActivity extends AppCompatActivity {
    Button btn_register;

    EditText et_name;
    EditText et_amount;

    DatePicker dp;
    FirebaseUser user;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");

    private DatabaseReference receiptRef;
    int year, month, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_register);

        et_name=(EditText)findViewById(R.id.et_name);
        et_amount=(EditText)findViewById(R.id.et_amount);

        dp=(DatePicker)findViewById(R.id.datepicker);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent= getIntent();
        String circlename= ((Intent) intent).getStringExtra("circlename");

        btn_register=(Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                receiptRef=mRootRef.child("circle/"+circlename+"/schedule/receipt");
                Log.d("find receipt","find");
                String date=dp.getYear()+"-"+dp.getMonth()+"-"+dp.getDayOfMonth();
                receiptRef.child(date).child("name").child(et_name.getText().toString()).child("amount").setValue(et_amount.getText().toString());
                finish();
            }
        });
    }
}