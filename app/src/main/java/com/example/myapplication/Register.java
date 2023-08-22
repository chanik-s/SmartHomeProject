package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private EditText mUsername;
    private Button mRegiButton;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailEditText = findViewById(R.id.register_id);
        mPasswordEditText = findViewById(R.id.register_ps);
        mUsername = findViewById(R.id.register_name);
        mRegiButton = findViewById(R.id.register_button);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        mRegiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String username = mUsername.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)   //파이어베이스 인증 통한 회원가입 처리
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //회원가입 성공시
                                    FirebaseUser user = mAuth.getCurrentUser();
                                   // Toast.makeText(Register.this, user.getEmail()+ "님 회원가입 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Register.this, username+ "님 회원가입 성공하셨습니다.",Toast.LENGTH_SHORT).show();

                                    mAuth.signInWithEmailAndPassword( email, password)
                                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()){
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        Toast.makeText(Register.this, user.getEmail() + "님 로그인 하셨습니다.",Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else{
                                                        Toast.makeText(Register.this, "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }else{
                                    Toast.makeText(Register.this,"회원가입에 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}