package com.example.myapplication;

import static android.content.ContentValues.TAG;

import static com.example.myapplication.Login.loginIdN;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Record extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private  FirebaseDatabase database;
   // private  String receivedLoginIdN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TextView mEmail = findViewById(R.id.email_profile);
        TextView mUid = findViewById(R.id.uid_profile);
        TextView mTime = findViewById(R.id.time_profile);
       // receivedLoginIdN = getIntent().getStringExtra("loginIdNreco");


        if (currentUser != null) {

            String uid = currentUser.getUid();
          //  Log.d("Record","로그인한 계정: "+email+", UID: "+uid);

            //파이어베이스 가져오기 시작
            database=FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("users").child(uid);
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            String email = user.getEmail();
                            //String loginTime = user.getLoginTime();//기존
                            //String loginID1=userReference.child("loginHistory").getKey(); //추.. 로그인 아이디 가져옴 push 자동하위생성
                           //위에꺼 고쳐야함
                           // String loginTime = user.getLoginHistory().get(loginID1).getLoginTime();
                            String uid_user=user.getUid();

                            mEmail.setText(email);
                            mUid.setText(uid_user);
                           // mTime.setText(loginTime);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                    Log.w(TAG, "Failed to read User value.", databaseError.toException());
                }
            });

            if (loginIdN != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference loginReference = databaseReference.child("users").child(uid).child("loginHistory")
                        .child(loginIdN); //loginIdN static으로 공유해서 문제생길수도 있음
                loginReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        LoginRecord loginRecord = snapshot.getValue(LoginRecord.class);
                        if (loginRecord != null) {
                            String loginTime = loginRecord.getLoginTime();
                            mTime.setText(loginTime);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "Failed to read LoginRecord value.", error.toException());
                    }
                });


                //파이어베이스 가져오기 끝
            }
            else {
                // receivedLoginIdN값이 전달되지 않았을 경우 처리
                Log.w(TAG, "Fail receivedLoginIdN ");
            }



        }else {
            // 사용자가 로그인하지 않은 상태일 때의 처리
            mEmail.setText("로그인이 필요합니다.");
            mUid.setText("");
            mTime.setText("");
        }

    }
}