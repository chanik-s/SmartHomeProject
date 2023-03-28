package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class humidActivity extends AppCompatActivity {
    private ListView ListHumid;
    ArrayList<Sensor> arrayListHumid; //받아와야함
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humid);

        ListHumid=findViewById(R.id.listHumid);
        Intent intent=getIntent();
        arrayListHumid=(ArrayList<Sensor>) intent.getSerializableExtra("sensor list");
        //Sensor 클래스 안에 멤버 변수들은 연속된 메모리에 할당되지 않아 직렬화 객체가 될 수 없어 변경해줘야함

        ArrayList<String> data= new ArrayList<>();

        //연결을 위한 adapter
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);

        ListHumid.setAdapter(adapter);
        //연결완료


        for(int i=0;i<arrayListHumid.size();i++){
            data.add("Humid "+arrayListHumid.get(i).getHumidity()+"%");
        }
        adapter.notifyDataSetChanged(); //저장
    }
}
