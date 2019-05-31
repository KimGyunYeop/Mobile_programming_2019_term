package com.example.udong_mp2019.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.udong_mp2019.R;
import com.example.udong_mp2019.circleList.CircleListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    Button signup;
    Button signin;
    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup= findViewById(R.id.btn_signUp);
        signin=findViewById(R.id.btn_signin);

        editTextEmail=findViewById(R.id.et_email);
        editTextPassword=findViewById(R.id.et_password);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

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
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(isValidEmail() && isValidPasswd()) {
                    loginUser(email, password);
                }
                else{
                    Toast.makeText(MainActivity.this, "잘못된 형식의 아이디 또는 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }
    // 로그인
    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // 로그인 성공
                    Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    String uid = user.getUid();
                    Intent intent = new Intent(getApplicationContext(), CircleListActivity.class);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                    finish();
                    //동아리 목록
                } else {
                    // 로그인 실패
                    Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}