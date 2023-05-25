package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class humidActivity extends AppCompatActivity {
    private static ArrayList<Sensor> arrayListHumid; //받아와야함
    private static ArrayAdapter<String> adapter;
    private static ArrayList<String> data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humid);

        Button btnHumiGraph = findViewById(R.id.btnHumiGraph); //그래프 버튼
        ListView listHumid = findViewById(R.id.listHumid); //리스트뷰로 나타낼 습도정보
        Intent intent=getIntent();
        arrayListHumid=(ArrayList<Sensor>) intent.getSerializableExtra("sensor list");
        //Sensor 클래스 안에 멤버 변수들은 연속된 메모리에 할당되지 않아 직렬화 객체가 될 수 없어 변경해줘야함

        //ArrayList<String> data= new ArrayList<>();
        data= new ArrayList<>();

        //연결을 위한 adapter
       // ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        listHumid.setAdapter(adapter);
        //연결완료

        if(arrayListHumid!=null) { //"null 개체 참조" 오류를 방지하고 상황을 보다 원활하게 처리
            for (int i = 0; i < arrayListHumid.size(); i++) {
                data.add("Humid " + arrayListHumid.get(i).getHumidity() + "%\n"+" Time " + arrayListHumid.get(i).getTime());
            }
        }
        adapter.notifyDataSetChanged(); //저장

        btnHumiGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(humidActivity.this,GraphHumid.class);
                intent.putExtra("sensor list",arrayListHumid);
                startActivity(intent);
            }
        });

    }

    // MainActivity에서 ArrayList 업데이트 시 호출될 메소드 (수정 필요)
    public static void updateArraylist(ArrayList<Sensor> updatedArrayList) {
        arrayListHumid= updatedArrayList;
        adapter.clear(); // 어댑터에 있는 데이터 모두 삭제
        if (arrayListHumid != null) {
            for (int i = 0; i < arrayListHumid.size(); i++) {
                data.add("Humid " + arrayListHumid.get(i).getHumidity() + "%\n"+" Time " + arrayListHumid.get(i).getTime());
            }
        }
        adapter.notifyDataSetChanged(); // 어댑터 데이터 변경 알림
        // 업데이트된 ArrayList 값 출력
        Log.d("humidActivity", "Updated Arraylist: " + arrayListHumid.toString());
    }

}
