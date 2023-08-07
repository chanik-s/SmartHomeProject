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

public class HumidActivity extends AppCompatActivity {

    private static ArrayList<Sensor> arrayListHumid; //받아와야함

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humid);

        Button btnHumiGraph = findViewById(R.id.btnHumiGraph); //그래프 버튼
        ListView listHumid = findViewById(R.id.listHumid); //리스트뷰로 나타낼 습도정보

        Intent intent=getIntent(); //putextra없이 intent

        arrayListHumid=(ArrayList<Sensor>) intent.getSerializableExtra("sensor list");
        //Sensor 클래스 안에 멤버 변수들은 연속된 메모리에 할당되지 않아 직렬화 객체가 될 수 없어 변경해줘야함

        //arrayListHumid=MainActivity.sensorArrayList; //전역변수 메인에서 받아와야함
        //데이터 arraylist
        ArrayList<String> data = new ArrayList<>();

        //연결을 위한 adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listHumid.setAdapter(adapter);
        //연결완료

        if(arrayListHumid!=null) { //"null 개체 참조" 오류를 방지하고 상황을 보다 원활하게 처리
            for (int i = 0; i < arrayListHumid.size(); i++) {
                data.add("Humid " + arrayListHumid.get(i).getHumidity() + "%\n"+" Time " + arrayListHumid.get(i).getTime());
            }
        }
        adapter.notifyDataSetChanged(); //저장

        btnHumiGraph.setOnClickListener(new View.OnClickListener() { //그래프 버튼 클릭시
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HumidActivity.this,GraphHumid.class);
                intent.putExtra("forhumigraph",arrayListHumid);
                startActivity(intent);
            }
        });

    }

    /*
    // MainActivity에서 ArrayList 업데이트 시 호출될 메소드 (수정 필요)
    public  void updateArraylist(ArrayList<Sensor> updatedArrayList) {
        //data= new ArrayList<>(); // data 변수 초기화
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        //ListView listHumid = findViewById(R.id.listHumid); //리스트뷰로 나타낼 습도정보
        //listHumid.setAdapter(adapter);
        data.clear();
        if (updatedArrayList != null) {
            for (int i = 0; i < updatedArrayList.size(); i++) {
                data.add("Humid " + updatedArrayList.get(i).getHumidity() + "%\n"+" Time " + updatedArrayList.get(i).getTime());
            }
        }

        adapter.notifyDataSetChanged(); // 어댑터 데이터 변경 알림
        // 업데이트된 ArrayList 값 출력
        if(updatedArrayList != null) {
            Log.d("humidActivity", "Updated Arraylist: " + updatedArrayList.toString());
        }
        else{
            Log.d("humidActivity", "Updated Arraylist is null.");
        }
    }

    public void updateListView(){
        adapter.notifyDataSetChanged(); // 어댑터 데이터 변경 알림
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // 새로운 인텐트를 현재 인텐트로 설정하여 업데이트된 데이터를 받을 수 있도록 함
    }
    //MainActivity에서 ArrayList 업데이트 시 호출될 메소드 (수정 필요) 끝


    @Override
    protected void onResume() {
        super.onResume();
        updateArraylist(arrayListHumid); //데이터 갱신
        updateListView(); //리스트뷰 업데이트

    }

*/


}
