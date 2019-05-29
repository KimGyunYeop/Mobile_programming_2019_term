package com.example.udong_mp2019.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.udong_mp2019.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    Button signUp;
    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,10}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userInfoRef= mDatabase.child("user");

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener joinListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SignUpActivity join= dataSnapshot.getValue(SignUpActivity.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    // 이메일, 비밀번호, 이름, 학교, 학번
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextSchool;
    private EditText editTextStudentId;

    private String email = "";
    private String password = "";
    private String name="";
    private String school="";
    private String studentId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);
        editTextName=findViewById(R.id.et_name);
        editTextSchool=findViewById(R.id.et_school);
        editTextStudentId=findViewById(R.id.et_studentId);
        signUp= findViewById(R.id.btn_signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                name= editTextName.getText().toString();
                school=editTextSchool.getText().toString();
                studentId=editTextStudentId.getText().toString();

                if(!isValidEmail()){
                    Toast.makeText(SignUpActivity.this,"유효하지 않은 이메일 형식입니다.",Toast.LENGTH_LONG).show();
                }
                else if(!isValidPasswd()){
                    Toast.makeText(SignUpActivity.this,"유효하지 않은 비밀번호 형식입니다.",Toast.LENGTH_LONG).show();
                }
                // 사용자 등록
                else{
                    createUser(email, password);
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

    // 회원가입
    private void createUser(final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user=task.getResult().getUser();
                            UserInfoForDB userInfoForDB = new UserInfoForDB(email, password, name, school, studentId);
                            userInfoRef.child(user.getUid()).setValue(userInfoForDB);
                            // 회원가입 성공
                            Toast.makeText(SignUpActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
