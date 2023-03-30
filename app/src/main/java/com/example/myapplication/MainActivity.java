package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.TimePicker;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    final String TAG = "TAG+MainActivity";
   // String[] sensorData = {"0", "0", "0"}; //안써도 될 듯?
    private final String DEFAULT="DEFAULT";
    private TextView dustText;
    private TextView tempText;
    private TextView humText;

    private Button pmbu;
    private Button hubu;
    private Button tmbu;

    private DrawerLayout drawerLayout; //상단 메뉴바
    private View drawerView; //상단 메뉴바
    NotificationManager notificationManager;
    ArrayList<Sensor> sensorArrayList;
    NotificationCompat.Builder builder;
    httpThread httpThread; //쓰레드 http 통신 위한 객체

    // ArrayList<Sensor> sensorArrayList; //센서 정보들 을  저장할 arraylist클래스(가변배열)    객체 타입은 Sensor 클래스



    //Activity는 Context를 상속받은 하위 클래스이다. this라는 키워드로 접근
    //Activity클래스엔 Application Context를 가져오는 getApplicationContext()라는 메소드도 있다.
    // Application Context는 백그라운드 작업 또는 데이터 액세스와 같이 Activity의 라이프사이클에 국한되지 않고 유지되어야 하는 작업 을 할 때 사용된다.


    //알림 서비스?
    void createNotificationChannel(String CHANNEL_ID,String channelName,int importance){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,channelName,importance));
        }
    }
    void createNotification(String CHANNEL_ID,int id,String title,String text)
    {
        builder=new NotificationCompat.Builder(this, CHANNEL_ID)
        //NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);

       // builder.setContentText("온도 = " + sensorArrayList.get(size-1).getTemperature() + " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity() + " % 미세먼지 농도 = 보통 (" + sensorArrayList.get(size-1).getPM() + "㎍/㎥)");
       notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id,builder.build());
    }

    void destroyNotification(int id)
    {
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
    //알람 기능

    @Override
    protected void onCreate(Bundle savedInstanceState) { //초기 생성단계 한번 실행되는 메소드
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //알림
        createNotificationChannel(DEFAULT,"default channel",NotificationManager.IMPORTANCE_HIGH);
        createNotification(DEFAULT,1,"실내 환경 데이터","온도 = 0 습도 = 0 미세먼지 = ");
       // context = this; //
        dustText = (TextView) findViewById(R.id.dustText); //미세먼지
        tempText = (TextView) findViewById(R.id.tempText); //온도
        humText = (TextView) findViewById(R.id.humText);   //습도

        //상단메뉴바
        pmbu=findViewById(R.id.pmbu);
        hubu=findViewById(R.id.hubu);
        tmbu=findViewById(R.id.tmbu);

        Button btn1=findViewById(R.id.btn1); //pm
        Button btn2=findViewById(R.id.btn2); //humid
        Button btn3=findViewById(R.id.btn3); //temp
        Button btn_cam=findViewById(R.id.btn_cam); //홈 캠 구현시

        //미세먼지
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,pmActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        //습도
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),humidActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        //온도
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),tempActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
       //홈 캠
        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),homeCameraActivity.class);
                startActivity(intent);
            }
        });

        //상단메뉴 리스너
        pmbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),pmActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        hubu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),humidActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        tmbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),tempActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        // JsonParse jsonParse = new JsonParse(); // 아래에 따로 만든 JsonParse 클래스 동적 할당
        // jsonParse.start(); // AsyncTask 실행

        //************************************************//
        //메뉴바 관련
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);  //메뉴바 생성
        drawerView = (View) findViewById(R.id.drawer);

        Button btn_menu = (Button)findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        //drawerLayout.setDrawerListener(listener);
        drawerLayout.addDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        //메뉴바 끝

        //http통신 핸들러
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

              //  String tmp = msg.obj.toString();
             // String tmp=msg.getData().toString();
               // sensorData = tmp.split(",");
                sensorArrayList= (ArrayList<Sensor>) msg.obj;
                int size=sensorArrayList.size();
               // Intent intent=new Intent(getApplicationContext(),pmActivity.class);

              //  intent.putExtra("sensorlist",sensorArrayList);
               // startActivity(intent);
                //tempText.setText(sensorData[0] + " ℃");
                //humText.setText(sensorData[1] + " %");
                tempText.setText(sensorArrayList.get(size-1).getTemperature() + " ℃");
                humText.setText(sensorArrayList.get(size-1).getHumidity() + " %");
                //int dust = Integer.parseInt(sensorData[2]);
                //int dust=40;
                //int dust= Integer.parseInt(sensorArrayList.get(0).getPM());
                int  dust=0;
                try{
                    dust=Integer.parseInt(sensorArrayList.get(size-1).getPM());
                }catch(NumberFormatException e){
                    dust=999; //에러 숫자
                }
                //널문자때메 오류나지만 실제로 미세먼지 센서 달으면 안날꺼임
                //미세먼지 조건
               // int dust= sensorArrayList.get(0).getPM();
                if (dust >= 0 && dust <= 30) {
                    dustText.setTextColor(Color.BLUE);
                    //dustText.setText("좋음 \n(" + sensorData[2] + " ㎍/㎥)");
                    dustText.setText("좋음 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                   builder.setContentText("온도 = " + sensorArrayList.get(size-1).getTemperature() + " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity() + " % 미세먼지 농도 = 좋음 (" + sensorArrayList.get(size-1).getPM() + "㎍/㎥)");
                } else if (dust >= 31 && dust <= 80) {
                    dustText.setTextColor(Color.GREEN);
                    dustText.setText("보통 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                   builder.setContentText("온도 = " + sensorArrayList.get(size-1).getTemperature() + " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity() + " % 미세먼지 농도 = 보통 (" + sensorArrayList.get(size-1).getPM() + "㎍/㎥)");
                } else if (dust >= 81 && dust <= 150) {
                    dustText.setTextColor(Color.parseColor("#FF7F00"));
                    dustText.setText("나쁨 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                    builder.setContentText("온도 = " +sensorArrayList.get(size-1).getTemperature() + " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity()  + " % 미세먼지 농도 = 나쁨 (" + sensorArrayList.get(size-1).getPM() + "㎍/㎥)");
                } else if (dust >= 151) {
                    dustText.setTextColor(Color.RED);
                    dustText.setText("매우나쁨 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                   builder.setContentText("온도 = " + sensorArrayList.get(size-1).getTemperature()+ " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity()  + " % 미세먼지 농도 = 매우나쁨 (" +sensorArrayList.get(size-1).getPM()+ "㎍/㎥)");
                } // 미세먼지 등급에 따라 등급과 글자색 변경
                notificationManager.notify(1,builder.build());
                return false;
            }
        });

        httpThread = new httpThread(handler);
        httpThread.start();
    }

    //상단바 리스너 만들기
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


        @Override
        public void onBackPressed() {
         //앱의 화면 전환을 위한 intent 사용
            Intent intent = new Intent(Intent.ACTION_MAIN); //액티비티 액션 -시작하는 액티비티(메인) 지정
            intent.addCategory(Intent.CATEGORY_HOME);  // 카테고리 - 홈화면을 보여주는 액티비티
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //플래그 - 새 테슽크 생성후 안에 액티비티 추가
            startActivity(intent); //홈화면으로 전환 시작
        } // 뒤로가기 버튼 클릭했을 때 홈으로 이동하기



}
    //생명주기 필요하다면

/*
    //스레드 핸들러 구조 (json 파싱 따로 만들어야?)
    public class JsonParse extends Thread { //Json 파싱 클래스
        String TAG = "JsonParseTest"; //? 디버깅용 문자열같음

        Handler handler;

        public JsonParse(Handler handler){
            this.handler=handler;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void run(){ //execute 매개 변수 받아와 [strings[0],strings[1]..등등] 위의 경우 url주소 하나뿐이므로 strings[0]만 존재
            // 스레드에 의해 처리될 내용을 담기 위한 함수 [스레드의  run() 함수]
            String url ="http://192.168.25.19:80/connect.php";
            try {
                URL serverURL = new URL(url); //라즈베리파이 서버의 url 정보 담는 객체 생성

                HttpURLConnection httpURLConnection = (HttpURLConnection) serverURL.openConnection(); //http 통신을 위한 객체(httpURLConnection) 선언   실제 네트워크 연결 설정x(url 가져와)

                if(httpURLConnection!=null) {
                    httpURLConnection.setReadTimeout(5000); //읽기 타임 아웃
                    httpURLConnection.setConnectTimeout(5000);//연결 타임 아웃
                    //setRequsetMethod 별도 설정안할시 GET 방식
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect(); //http요청 실시

                    int responseStatusCode = httpURLConnection.getResponseCode(); //getResponseCode() => 서버에서 보낸 http 상태 코드 반환

                    InputStream inputStream; //입력 데이터 통로
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) { //http 상태 코드 200:정상
                        inputStream = httpURLConnection.getInputStream(); //입력 스트림 얻기
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); //InputStream으로 바이트 단위 데이터 얻기보단 Reader 거쳐 문자 읽어(한글 깨짐 해결)
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //버퍼를 통해 문자열 읽기(위에선 문자로 하나하나 처리임)

                    StringBuilder sb = new StringBuilder(); //변경 가능한 문자열 만들어.. String 합치는 작업시 대안 가능(ex> sb.append("문자열").append("연결");
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {  //readLine() 입력 메소드   [한 줄(공백포함)전체 읽으므로 String으로 리턴 가능]
                        sb.append(line); //문자열 저장
                    }

                    bufferedReader.close();
                    Log.d(TAG, sb.toString().trim()); //디버깅 상태 확인

                    String fromdoIn;
                    fromdoIn=sb.toString().trim();
                 //  if (fromdoIn == null)
                  //     textView.setText("error");
                 //   else {
                        jsonString = fromdoIn;
                        sensorArrayList = doParse(); //tmpSensorArray 값
                        Log.d(TAG, sensorArrayList.get(0).getHumidity()); //디버깅용

                        //앱 화면에 보여지는 구간
                    dustText.setText(sensorArrayList.get(0).getTemperature() + sensorArrayList.get(0).getHumidity());
                     //   for (int i = 0; i < sensorArrayList.size(); i++){
                         //   textView.setText(sensorArrayList.get(i).getTemperature() + sensorArrayList.get(i).getHumidity());
                        //   textView.setText(sensorArrayList.get(i).getTemperature());
                       //    textView.setText(sensorArrayList.get(i).getHumidity()); //get(int index) 인덱스 위치에 있는 객체 리턴   (만약 전부 다 볼려면 for문 써야하나???)
                        //    textView.setText(sensorArrayList.get(i).getTime());
                        //    textView.setText(sensorArrayList.get(i).getPM());
                  //      }
                 //   }
                    httpURLConnection.disconnect(); //?
                }
            } catch (Exception e) {
                Log.d(TAG, "InsertData:ERROR", e); // 디버깅 에러보고
                e.printStackTrace();
            }
        }



        private ArrayList<Sensor> doParse() { //파싱하기
            ArrayList<Sensor> tmpSensorArray = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString); //JSON에서 key(ex>"sensor")-value(ex>"{temp:30,humi:20}") 쌍으로 데이터 표현     json에서 {}으로 감싸진 것
                JSONArray jsonArray = jsonObject.getJSONArray("Sensor");  // json에서 []으로 감싸진 것  "Sensor"는 php문에서 db 테이블명
                // php문 array()에서

                for (int i = 0; i < jsonArray.length(); i++) {
                    Sensor tmpSensor = new Sensor();
                    JSONObject item = jsonArray.getJSONObject(i);
                    tmpSensor.setTemperature(item.getString("temp"));
                    tmpSensor.setHumidity(item.getString("humi"));
                    tmpSensor.setTime(item.getString("time"));
                    tmpSensor.setPM(item.getString("pm"));

                    tmpSensorArray.add(tmpSensor);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tmpSensorArray; //json 가공해 arraylist에 넣음
        }

    }

 */
