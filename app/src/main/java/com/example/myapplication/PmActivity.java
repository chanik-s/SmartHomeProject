package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PmActivity extends AppCompatActivity {

    private ListView listPm;
    ArrayList<Sensor> arrayListPm; //받아와야함
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);


        Intent intent=getIntent();
        arrayListPm=(ArrayList<Sensor>) intent.getSerializableExtra("sensor list");
        //Sensor 클래스 안에 멤버 변수들은 연속된 메모리에 할당되지 않아 직렬화 객체가 될 수 없어 변경해줘야함
        listPm=findViewById(R.id.listPm);
        ArrayList<String> data= new ArrayList<>();
        Button btnPmGraph = findViewById(R.id.btnPmGraph); //그래프 버튼

        //연결을 위한 adapter
        ArrayAdapter<String>adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        listPm.setAdapter(adapter);
        //연결완료
        if(arrayListPm!=null) {
            for (int i = 0; i < arrayListPm.size(); i++) {
                data.add("PM " + arrayListPm.get(i).getPM() + "㎍/㎥\n" + " Time " + arrayListPm.get(i).getTime());
            }
        }
        //더 해볼 것..>simple_list_item_1을 2개 칸으로 나눠서 미세먼지 정보 + 타임 스태프 정보 나누기!


        adapter.notifyDataSetChanged(); //저장
        btnPmGraph.setOnClickListener(new View.OnClickListener() { //그래프 버튼 클릭시
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PmActivity.this,GraphPm.class);
                intent.putExtra("forpmgraph",arrayListPm);
                startActivity(intent);
            }
        });
    }
}
