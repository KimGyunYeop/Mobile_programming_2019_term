package com.example.udong_mp2019;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.udong_mp2019.CircleInfoForDB;
import com.example.udong_mp2019.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CircleRegisterFormActivity extends AppCompatActivity {
    Button bu_submit;
    EditText et_name,et_school;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circlrInfoRef = mRootRef.child("circle");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_register_form);

        bu_submit = findViewById(R.id.Bu_submit);
        et_school = findViewById(R.id.ET_school);
        et_name = findViewById(R.id.ET_name);

        firebaseAuth = FirebaseAuth.getInstance();

        bu_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                circlrInfoRef.child(et_school.getText().toString()+":"+et_name.getText().toString()+"/info").setValue(new CircleInfoForDB(et_name.getText().toString(),et_school.getText().toString(),new Date().toString()));
                // 생성한 user manager로 등록
                com.example.udong_mp2019.MemberInfoForDB memberInfoForDB= new com.example.udong_mp2019.MemberInfoForDB("manager",new Date().toString());
                circlrInfoRef.child(et_school.getText().toString()+":"+et_name.getText().toString()+"/member").child(user.getUid()).setValue(memberInfoForDB);
                Toast.makeText(CircleRegisterFormActivity.this, "동아리가 생성되었습니다!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
