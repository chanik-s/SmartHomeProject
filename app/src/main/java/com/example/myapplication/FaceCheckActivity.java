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

import java.util.ArrayList;

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

                for (StorageReference item : listResult.getItems()) {
                    // 각 이미지를 Glide를 사용하여 ImageView에 표시

                    // 각 이미지를 ArrayList에 추가
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                          //  Glide.with(getApplicationContext()).load(uri).into(imageView);

                            // 파일 이름 가져오기
                            String fileName = item.getName();

                            imageUrls.add(uri);
                            fileNames.add(fileName);

                            // 파일 이름 목록을 어댑터에 설정
                            imageAdapter.setFileNames(fileNames);

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
}
