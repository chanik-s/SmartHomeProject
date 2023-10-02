package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<Uri> imageUrls;

    private ArrayList<String> fileNames; // 파일 이름 목록 저장
    private ArrayList<String> imageTimes; //이미지 시간 정보 저장

    public ImageAdapter(Context context, ArrayList<Uri> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.fileNames = new ArrayList<>(); // 파일 이름 목록 초기화
        this.imageTimes =new ArrayList<>(); // 이미지 시간 목록 초기화
    }

    // 파일 이름 목록 설정 메서드 추가
    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
        notifyDataSetChanged(); // 데이터 변경 알림
    }
    public void setImageTimes(ArrayList<String> imageTimes) {
        this.imageTimes = imageTimes;
        notifyDataSetChanged(); // 데이터 변경 알림
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //뷰 연결
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        //각 아이템들 실제적 매칭
        Uri imageUrl = imageUrls.get(position);
        holder.fileNameTextView.setText(fileNames.get(position)); // 파일 이름 설정
        holder.timeTextView.setText(imageTimes.get(position)); //이미지 시간 설정

        Glide.with(context)
                .load(imageUrl)
                .override(200, Target.SIZE_ORIGINAL) // 여기서 200은 원하는 폭입니다.
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView fileNameTextView;
        public TextView timeTextView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            fileNameTextView=itemView.findViewById(R.id.fileNameTextView);
            timeTextView=itemView.findViewById(R.id.timeTextView);
        }
    }
}