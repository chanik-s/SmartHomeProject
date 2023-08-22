package com.example.myapplication;

import static android.content.ContentValues.TAG;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TextView mEmail = findViewById(R.id.email_profile);
        TextView mUid = findViewById(R.id.uid_profile);
        TextView mTime = findViewById(R.id.time_profile);

        if (currentUser != null) {
            //String email = currentUser.getEmail();
            String uid = currentUser.getUid();
          //  Log.d("Record","로그인한 계정: "+email+", UID: "+uid);
           // mEmail.setText(email);
            //mUid.setText(uid);

            //파이어베이스 가져오기 시작
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            String email = user.getEmail();
                            String loginTime = user.getLoginTime();
                            String uid_user=user.getUid();

                            mEmail.setText(email);
                            mUid.setText(uid_user);
                            mTime.setText(loginTime);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                }
            });
            //파이어베이스 가져오기 끝

           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 한국 시간대 설정
            //String currentDateandTime = sdf.format(new Date());
            //Log.d("Record","로그인 시간: "+currentDateandTime);
            //mTime.setText(currentDateandTime);

        }else {
            // 사용자가 로그인하지 않은 상태일 때의 처리
            mEmail.setText("로그인이 필요합니다.");
            mUid.setText("");
            mTime.setText("");
        }

    }
}