package com.MP2019.NDND.circle.Finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.MP2019.NDND.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ReceiptRegisterActivity extends AppCompatActivity {
    Button btn_register;

    EditText et_name;
    EditText et_amount;

    DatePicker dp;
    FirebaseUser user;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");

    private static final Pattern INTEGER_PATTERN =  Pattern.compile("(^[0-9]*$)");

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
        String circlename= ((Intent) intent).getStringExtra("circleName");

        btn_register=(Button)findViewById(R.id.btn_receipt_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!INTEGER_PATTERN.matcher(et_amount.getText().toString()).matches() || et_amount.getText().toString().equals("")) {
                    Toast.makeText(ReceiptRegisterActivity.this, "금액은 숫자로 입력해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    receiptRef = mRootRef.child("circle/" + circlename + "/schedule/receipt");
                    Log.d("find receipt", "find");
                    int month= 1+dp.getMonth();
                    String date = dp.getYear() + "-" + month + "-" + dp.getDayOfMonth();
                    receiptRef.child(date).child(et_name.getText().toString()).child("amount").setValue(et_amount.getText().toString());
                    finish();
                }
            }
        });
    }
}