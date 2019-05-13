package com.example.udongmp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button signup;
    Button signin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup= findViewById(R.id.btn_signUp);
        signin=findViewById(R.id.btn_signIn);

        //회원가입 버튼
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup= new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intentSignup);
            }
        });
        //로그인 버튼
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
            }
        });
    }
}
