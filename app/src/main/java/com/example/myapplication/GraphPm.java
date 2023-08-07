package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GraphPm extends AppCompatActivity {

    private int startIndex = 0; //데이터 시작 인덱스
    private ArrayList<Sensor> arrayListPm;
    private LineChart mchart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_pm);

        mchart = findViewById(R.id.PmChart);
        // 버튼 클릭 리스너 설정
        Button nextButton = findViewById(R.id.nextBtnP);
        Button prevButton = findViewById(R.id.prevBtnP);

        Intent intent = getIntent();
        arrayListPm = (ArrayList<Sensor>) intent.getSerializableExtra("forpmgraph");
        // ArrayList<Sensor> arrayListHumid=MainActivity.sensorArrayList; //메인 액티비티에서 바로 가져오기

        // 초기 데이터 그래프 표시
        updateChart();
        //다음 10개
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startIndex + 10 < arrayListPm.size()) {
                    startIndex += 10;
                    updateChart();
                }
            }
        });
        //이전 10개
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startIndex >= 10) {
                    startIndex -= 10;
                    updateChart();
                }
            }
        });



    }

    //test용 시작
    private void updateChart() {
        ArrayList<Entry> entries = new ArrayList<>();
        //String baseDate = arrayListHumid.get(0).getTime(); // 첫 번째 입력 데이터의 날짜를 기준으로 설정
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        // startIndex부터 10개 데이터 추출하여 entries에 추가
        for (int i = startIndex; i < startIndex + 10 && i < arrayListPm.size(); i++) {
            String dateStr = arrayListPm.get(i).getTime();


            float value = Float.parseFloat(arrayListPm.get(i).getTemperature());
            try {
                // 문자열 형태의 날짜를 Date 객체로 변환
                Date date = format.parse(dateStr);
                // Date 객체에서 시간 값을 밀리초로 가져오기
                assert date != null;
                long timeInMillis = date.getTime();

                // entries.add(new Entry(timeInMillis, value));
                entries.add(new Entry(i, value)); //###인덱스로 x축 표시###

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Error parsing date", e);
            }

        }

        LineDataSet dataSet = new LineDataSet(entries, "Humidity");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setDrawValues(false);
        LineData lineData = new LineData(dataSet);

        // LineChart에 데이터 설정
        mchart.setDescription(null);
        mchart.setData(lineData);
        //x축 설정
        XAxis xAxis = mchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x축을 그래프 아래로
        xAxis.setGranularity(3600);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-45);
        // xAxis.setValueFormatter(new DateAxisValueFormatter("MM-dd HH:mm:ss")); //삭제해도 될 듯
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < arrayListPm.size()) {
                    String dateStr = arrayListPm.get(index).getTime();
                    try {
                        Date date = format.parse(dateStr);
                        assert date != null;
                        return new SimpleDateFormat("MM-dd HH:mm:ss").format(date); //x축 인덱스로 얻고 그 값만 표시
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error parsing date", e);
                    }
                }
                return "";
            }
        });

        xAxis.setLabelCount(10, true);

        // Y축 설정
        YAxis yAxisLeft = mchart.getAxisLeft();
        yAxisLeft.setGranularity(1f);//눈금 1단위

        yAxisLeft.setAxisMinimum(0); // Y축 최소값 설정
        yAxisLeft.setAxisMaximum(100f); // Y축 최대값 설정

        mchart.invalidate(); // 차트 업데이트
    }


    //test용 끝

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
