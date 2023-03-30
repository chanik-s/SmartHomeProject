package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class tempActivity extends AppCompatActivity {

    private ListView ListTemp;
    ArrayList<Sensor> arrayListTemp; //받아와야함
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);


        Intent intent=getIntent();

        arrayListTemp = (ArrayList<Sensor>) intent.getSerializableExtra("sensor list");

        //Sensor 클래스 안에 멤버 변수들은 연속된 메모리에 할당되지 않아 직렬화 객체가 될 수 없어 변경해줘야함
        ListTemp=findViewById(R.id.listTemp);
        ArrayList<String> data= new ArrayList<>();


        //연결을 위한 adapter
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);

        ListTemp.setAdapter(adapter);
        //연결완료
        if(arrayListTemp!=null) {
            for (int i = 0; i < arrayListTemp.size(); i++) {
                data.add("Temp " + arrayListTemp.get(i).getTemperature() + "℃");
            }
        }

        adapter.notifyDataSetChanged(); //저장
    }
}

