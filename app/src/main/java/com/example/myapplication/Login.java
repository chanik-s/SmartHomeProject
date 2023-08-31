package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Login extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference usersRef;
    //public static String loginIdN; //loginIdN static으로 공유해서 문제생길수도 있음
    public static String loginIdN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.emailedit);
        mPasswordEditText = findViewById(R.id.passedit);
        mLoginButton = findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        databaseReference=FirebaseDatabase.getInstance().getReference();
        usersRef=FirebaseDatabase.getInstance().getReference("users");
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


                                    String uid = currentUser.getUid();
                                    User user = new User(email, uid);
                                    //현재 시간 구하기
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 한국 시간대 설정
                                    String currentDateAndTime = sdf.format(new Date());



                                    loginIdN=databaseReference.child("users").child(uid).child("loginHistory")
                                            .push().getKey(); //추.. 로그인 아이디 가져옴 push 자동하위생성
                                   // LoginRecord loginRecord=new LoginRecord(0,0,currentDateAndTime);//추..이땐 위치 디폴트

                                    //새 로그인 기록 리스트로 저장방법!
                                    double latitude = 0.0;
                                    double longitude = 0.0;
                                    String loginTime = currentDateAndTime; // 예시: 2023-08-31 12:34:56

                                    // 로그인 기록을 리스트로 저장
                                    Map<String, Object> loginRecord = new HashMap<>();
                                    loginRecord.put("latitude", latitude);
                                    loginRecord.put("longitude", longitude);
                                    loginRecord.put("loginTime", loginTime);

                                    // 로그인 기록을 파이어베이스에 저장
                                    usersRef.child(uid).child("loginHistory").child(loginIdN).setValue(loginRecord);


                                    //파이어베이스 realtime db에 저장
                                    //usersRef = databaseReference.child("users");
                                    //usersRef.child(uid).setValue(user);
                                    //assert loginIdN != null;
                                   // usersRef.child(uid).child("loginHistory").child(loginIdN)
                                     //       .child("latitude").setValue(loginRecord.getLatitude()); //이땐 위치 디폴트
                                    //usersRef.child(uid).child("loginHistory").child(loginIdN)
                                    //        .child("longitude").setValue(loginRecord.getLongitude()); //이땐 위치 디폴트
                                    //usersRef.child(uid).child("loginHistory").child(loginIdN)
                                    //      .child("loginTime").setValue(loginRecord.getLoginTime());
                                    // 파이어베이스 끝

                                    //인텐트로 전달? 문제 발생! 여기서부터 해결 필요 0824
                                   // Intent intentToRecord=new Intent(Login.this, Record.class);
                                   // intentToRecord.putExtra("loginIdNreco",loginIdN);
                                   // startActivity(intentToRecord);
                                   // Log.d("receivedLoginIdN",loginIdN);

                                   // Intent intentToMap=new Intent(Login.this, GoogleMap.class);
                                    //intentToMap.putExtra("loginIdN",loginIdN);
                                   // startActivity(intentToMap);

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent); //액티비티 이동 A->B (스택)
                                   // finish(); //A 제거
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