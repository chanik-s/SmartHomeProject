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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GraphHumid extends AppCompatActivity {

    private int startIndex=0; //데이터 시작 인덱스
    private ArrayList<Sensor> arrayListHumid;
    private LineChart mchart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_humid);

        mchart=findViewById(R.id.HumidChart);

        Intent intent=getIntent();
        arrayListHumid = (ArrayList<Sensor>) intent.getSerializableExtra("forhumigraph");
       // ArrayList<Sensor> arrayListHumid=MainActivity.sensorArrayList; //메인 액티비티에서 바로 가져오기

        // 초기 데이터 그래프 표시
        updateChart();
        // 버튼 클릭 리스너 설정
        Button nextButton = findViewById(R.id.nextBtn);
        Button prevButton = findViewById(R.id.prevBtnH);
        //다음 10개
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startIndex + 10 < arrayListHumid.size()) {
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



       /*
        String baseDate = arrayListHumid.get(0).getTime(); // 첫 번째 입력 데이터의 날짜를 기준으로 설정
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        // 데이터 생성
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = arrayListHumid.size() - 6; i < arrayListHumid.size(); i++) {

                String dateStr = arrayListHumid.get(i).getTime();

                float value = Float.parseFloat(arrayListHumid.get(i).getHumidity());

                try {
                    // 문자열 형태의 날짜를 Date 객체로 변환
                    Date date = format.parse(dateStr);
                    // Date 객체에서 시간 값을 밀리초로 가져오기
                    assert date != null;
                    long timeInMillis = date.getTime();

                    entries.add(new Entry(timeInMillis, value));

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
        mchart.setDescription(null);
        mchart.setData(lineData);

        //x축 설정
        XAxis xAxis = mchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x축을 그래프 아래로
        xAxis.setGranularity(3600);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new DateAxisValueFormatter("MM-dd HH:mm:ss", baseDate));
        xAxis.setLabelCount(6, true);
        // Y축 설정
        YAxis yAxisLeft = mchart.getAxisLeft();
        //YAxis yAxisRight = mchart.getAxisRight();
        yAxisLeft.setGranularity(1f);//눈금 1단위
        //yAxisRight.setGranularity(1f);



        mchart.invalidate();
        // pinch zoom 가능 (손가락으로 확대축소하는거)
        mchart.setPinchZoom(true);
    */

    }

    //test용 시작
    private void updateChart() {
        ArrayList<Entry> entries = new ArrayList<>();
        //String baseDate = arrayListHumid.get(0).getTime(); // 첫 번째 입력 데이터의 날짜를 기준으로 설정
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        // startIndex부터 10개 데이터 추출하여 entries에 추가
        for (int i = startIndex; i < startIndex + 10 && i < arrayListHumid.size(); i++) {
            String dateStr = arrayListHumid.get(i).getTime();


            float value=Float.parseFloat(arrayListHumid.get(i).getHumidity() );
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
                if (index >= 0 && index < arrayListHumid.size()) {
                    String dateStr = arrayListHumid.get(index).getTime();
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
        yAxisLeft.setAxisMinimum(0f); // Y축 최소값 설정
        yAxisLeft.setAxisMaximum(100f); // Y축 최대값 설정

        mchart.invalidate(); // 차트 업데이트
    }



    //test용 끝

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //없어도 될 듯----
    static public class DateAxisValueFormatter extends ValueFormatter  {
        private final SimpleDateFormat mFormat;
        /*
        private long mBaseTimestamp; // 입력된 데이터의 연도를 기준으로 하는 타임스탬프 값
        public DateAxisValueFormatter(String format, String baseDate) {
            mFormat = new SimpleDateFormat(format,Locale.KOREA);
            try {
                // 입력된 데이터의 연도를 기준으로 하는 타임스탬프 값 계산

                Date base = mFormat.parse(baseDate);
                assert base != null;
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
    */
    public DateAxisValueFormatter(String format) {
        mFormat = new SimpleDateFormat(format, Locale.KOREA);

    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        Date date = new Date((long) value); // value는 밀리초 단위의 시간
        return mFormat.format(date);
    }
}
}


