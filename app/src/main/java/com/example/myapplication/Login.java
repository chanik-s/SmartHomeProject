package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Login extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.emailedit);
        mPasswordEditText = findViewById(R.id.passedit);
        mLoginButton = findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                //파이어베이스 인증으로 로그인 처리
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            //AuthResult 객체를 반환하는데, 이 객체는 로그인 결과와 관련된 정보를 가짐
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    //로그인 성공 시
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    Toast.makeText(Login.this, currentUser.getEmail() + "님 접속하셨습니다.", Toast.LENGTH_SHORT).show();
                                    //파이어베이스 realtime db에 저장

                                    String uid = currentUser.getUid();

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 한국 시간대 설정
                                    String currentDateAndTime = sdf.format(new Date());

                                    User user = new User(email, uid, currentDateAndTime);

                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                    usersRef.child(uid).setValue(user);
                                    // 파이어베이스 끝

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent); //액티비티 이동 A->B (스택)
                                    finish(); //A 제거
                                }
                                else{
                                    Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //회원가입
        Button buttonReg = (Button) findViewById(R.id.register_button);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });



    }
}