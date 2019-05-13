package com.example.udongmp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CircleRegisterFormActivity extends AppCompatActivity {
    Button bu_submit;
    EditText et_name,et_school;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circlrInfoRef = mRootRef.child("circle");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_register_form);

        bu_submit = findViewById(R.id.Bu_submit);
        et_school = findViewById(R.id.ET_school);
        et_name = findViewById(R.id.ET_name);

        bu_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circlrInfoRef.child(et_school.getText().toString()+":"+et_name.getText().toString()+"/info").setValue(new CircleInfoForDB(et_name.getText().toString(),et_school.getText().toString(),new Date().toString()));
                finish();
            }
        });
    }
}
