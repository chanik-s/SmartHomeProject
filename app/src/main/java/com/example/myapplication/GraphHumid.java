package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GraphHumid extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_humid);

        LineChart mchart;
        mchart=findViewById(R.id.HumidChart);

        Intent intent=getIntent();
       // arrayListHumid=(ArrayList<Sensor>) intent.getSerializableExtra("sensor list");
        ArrayList<Sensor> arrayListHumid = (ArrayList<Sensor>) intent.getSerializableExtra("sensor list");

        String baseDate = arrayListHumid.get(0).getTime(); // 첫 번째 입력 데이터의 날짜를 기준으로 설정
        //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //현재 시간 구하기 (0525)
        long currentTimeMillis = System.currentTimeMillis();
        // 24시간(1일) 전의 시간 구하기(0525)
        long twentyFourHoursAgoMillis = currentTimeMillis - (24 * 60 * 60 * 1000);




        //x축 설정
        XAxis xAxis=mchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setGranularity(1000);
        //xAxis.setGranularity(60); // 1분 간격으로 눈금 표시
        xAxis.setGranularity(60); // 1초 간격으로 눈금 표시
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new DateAxisValueFormatter("MM-dd HH:mm:ss",baseDate));
        xAxis.setLabelCount(10,true);
        // Y축 설정
        YAxis yAxisLeft = mchart.getAxisLeft();
        YAxis yAxisRight = mchart.getAxisRight();
        yAxisLeft.setGranularity(1f);
        yAxisRight.setGranularity(1f);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREA);
            // 데이터 생성
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < arrayListHumid.size(); i++) {
                String dateStr = arrayListHumid.get(i).getTime();
                float value = Float.parseFloat(arrayListHumid.get(i).getHumidity());

               try {
                    // 문자열 형태의 날짜를 Date 객체로 변환
                    Date date = format.parse(dateStr);
                    // Date 객체에서 시간 값을 밀리초로 가져오기
                    long timeInMillis = date.getTime();

                   //현재 시간 기준 하루 전부터의 데이터만 추가(0525)
                    //if(timeInMillis>=twentyFourHoursAgoMillis ) {
                        entries.add(new Entry(timeInMillis, value));
                  //  }
                } catch (ParseException e) {
                  e.printStackTrace();
                   Log.e(TAG, "Error parsing date", e);
                }

        }

        // 데이터셋 생성
        LineDataSet dataSet = new LineDataSet(entries, "Humidity");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setDrawValues(false);

        // 데이터셋을 LineData 객체에 추가
        LineData lineData = new LineData(dataSet);

        // LineChart에 데이터 설정
        mchart.setData(lineData);
        mchart.invalidate();
        // pinch zoom 가능 (손가락으로 확대축소하는거)
        mchart.setPinchZoom(true);


    }

    static public class DateAxisValueFormatter extends ValueFormatter  {
        private final SimpleDateFormat mFormat;
        private long mBaseTimestamp; // 입력된 데이터의 연도를 기준으로 하는 타임스탬프 값
        public DateAxisValueFormatter(String format, String baseDate) {
            mFormat = new SimpleDateFormat(format,Locale.KOREA);
            try {
                // 입력된 데이터의 연도를 기준으로 하는 타임스탬프 값 계산

                Date base = mFormat.parse(baseDate);
                mBaseTimestamp = base.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
                mBaseTimestamp = 0; // 기본값: 1970년 1월 1일 00:00:00
            }
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            long timeInMillis = (long) value + mBaseTimestamp;
            Date date = new Date(timeInMillis);
            return mFormat.format(date);
        }
    }
}


