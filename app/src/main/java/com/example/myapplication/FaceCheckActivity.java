package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FaceCheckActivity extends AppCompatActivity {

    private  RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ArrayList<Uri> imageUrls;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facecheck);

        Intent intent=getIntent(); // from 메인액티비티

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageUrls = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageUrls);
        recyclerView.setAdapter(imageAdapter);



        //Cloud Storage 버킷에 액세스하는 첫 단계
        FirebaseStorage storage=FirebaseStorage.getInstance("gs://my-application-6fa3b.appspot.com");
        //참조 만들기
        StorageReference storageRef=storage.getReference();//폴더 만들어서 경로 설정해줄 필요있음



        // "images" 폴더 안의 모든 파일 목록을 가져옴
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                ArrayList<String> fileNames = new ArrayList<>(); // 파일 이름 목록 초기화
                ArrayList<String> imageTimes = new ArrayList<>(); // 이미지 시간 목록 초기화
                for (StorageReference item : listResult.getItems()) {
                    // 각 이미지를 Glide를 사용하여 ImageView에 표시

                    // 각 이미지를 ArrayList에 추가
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // 파일 이름 가져오기
                            String fileName = item.getName();
                            // 이미지 시간 정보 가져오기 (예: 이미지 이름에서 추출)
                            String imageTime = extractTimeFromImageName(fileName);
                            imageTimes.add(imageTime); // 이미지 시간 목록에 추가

                            imageUrls.add(uri);
                            fileNames.add(fileName);

                            imageAdapter.setFileNames(fileNames); // 파일 이름 목록을 어댑터에 설정
                            imageAdapter.setImageTimes(imageTimes); // 이미지 시간 정보 설정
                            imageAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경을 알림
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 이미지 다운로드 실패 처리
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 파일 목록 가져오기 실패 처리
            }
        });


    }
    private String extractTimeFromImageName(String fileName){
        //String extractedNum=fileName.replaceAll("^[0-9]","");

        // 파일 이름에서 "unknown_"를 제거하여 "1695918735.jpg" 부분을 얻습니다.
        String tmp = fileName.replace("unknown_", "");
        // 파일 이름에서 ".jpg" 확장자 부분을 제거하여 "1695918735" 부분을 얻습니다.
        String extractedNum = tmp.replace(".jpg", "");

        // 날짜 및 시간 형식 지정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US);

        // 숫자 문자열을 long 타입으로 변환
        long timestamp = Long.parseLong(extractedNum);

        // Epoch 시간(밀리초)을 Date 객체로 변환
        Date date = new Date(timestamp * 1000); // 밀리초로 변환하기 위해 1000을 곱함

        // 형식에 따라 문자열로 변환
        String formattedDateTime = sdf.format(date);

        return formattedDateTime;
    }

}
