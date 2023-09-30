package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity {


    final String TAG = "TAG+MainActivity";
    private FirebaseAuth mAuth;
    private TextView dustText;
    private TextView tempText;
    private TextView humText;
    private Timer timer;
    private HumidActivity hactivity; // humidActivity 인스턴스를 참조하는 변수

    private DrawerLayout drawerLayout; //상단 메뉴바
    private View drawerView; //상단 메뉴바
    NotificationManager notificationManager;
    public static ArrayList<Sensor> sensorArrayList; //전역변수-앱 전체에서 공유
    NotificationCompat.Builder builder;
    HttpThread httpThread; //쓰레드 http 통신 위한 객체



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
        String DEFAULT = "DEFAULT";
        createNotificationChannel(DEFAULT,"default channel",NotificationManager.IMPORTANCE_LOW);
        createNotification(DEFAULT,1,"실내 환경 데이터","온도 = 0 습도 = 0 미세먼지 = ");
       // context = this; //
        dustText = (TextView) findViewById(R.id.dustText); //미세먼지
        tempText = (TextView) findViewById(R.id.tempText); //온도
        humText = (TextView) findViewById(R.id.humText);   //습도

        mAuth=FirebaseAuth.getInstance();

        //상단메뉴바 버튼
        Button pmbu=findViewById(R.id.pmbu);
        Button hubu=findViewById(R.id.hubu);
        Button tmbu=findViewById(R.id.tmbu);
        Button faceCheck=findViewById(R.id.faceCheck);
        Button logOut=findViewById(R.id.logOut);
        Button loginfile=findViewById(R.id.loginfile);


        //메인 쓰레드 버튼
        Button btn1 = findViewById(R.id.btn1); //pm
        Button btn2 = findViewById(R.id.btn2); //humid
        Button btn3 = findViewById(R.id.btn3); //temp
        Button btn_cam=findViewById(R.id.btn_cam); //홈 캠 구현시
        Button contrl_dev=findViewById(R.id.contrl_dev); //기기 제어

        //미세먼지
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, PmActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });

        //습도
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), HumidActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });

        //온도
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TempActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
       //홈 캠
        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), HomeCameraActivity.class);
                startActivity(intent);
            }
        });
        //기기 제어
        contrl_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ControlDeviceActivity.class);
                startActivity(intent);
            }
        });

        //상단메뉴들 버튼 인텐트

        //먼지 농도 눌렀을때
        pmbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PmActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        //습도 눌렀을때
        hubu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HumidActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        //온도 눌렀을때
        tmbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TempActivity.class);
                intent.putExtra("sensor list",sensorArrayList);
                startActivity(intent);
            }
        });
        //얼굴 인식 눌렀을때
        faceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FaceCheckActivity.class);
                startActivity(intent);
            }
        });
        //생활 팁 눌렀을때
       logOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               signOut();

           }
       });
        //로그인 정보 및 기록
        loginfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), AfterLogin.class);
                startActivity(intent);
            }
        });



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

                sensorArrayList= (ArrayList<Sensor>) msg.obj;
                int size=sensorArrayList.size();
                tempText.setText(sensorArrayList.get(size-1).getTemperature() + " ℃");
                humText.setText(sensorArrayList.get(size-1).getHumidity() + " %");

                double  dust; //double로인해 해결
                try{
                   dust=Double.parseDouble((sensorArrayList.get(size - 1).getPM())); //string->double 형변환
                }catch(NumberFormatException e){
                    dust=-1; //에러 숫자
                }

                //미세먼지 조건
                if (dust >= 0 && dust <= 30.0) {
                    dustText.setTextColor(Color.BLUE);
                    //dustText.setText("좋음 \n(" + sensorData[2] + " ㎍/㎥)");
                    dustText.setText("좋음 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                   builder.setContentText("온도 = " + sensorArrayList.get(size-1).getTemperature() + " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity() + " % 미세먼지 농도 = 좋음 (" + sensorArrayList.get(size-1).getPM() + "㎍/㎥)");
                } else if (dust >= 31.0 && dust <= 80.0) {
                    dustText.setTextColor(Color.GREEN);
                    dustText.setText("보통 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                   builder.setContentText("온도 = " + sensorArrayList.get(size-1).getTemperature() + " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity() + " % 미세먼지 농도 = 보통 (" + sensorArrayList.get(size-1).getPM() + "㎍/㎥)");
                } else if (dust >= 81.0 && dust <= 150.0) {
                    dustText.setTextColor(Color.parseColor("#FF7F00"));
                    dustText.setText("나쁨 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                    builder.setContentText("온도 = " +sensorArrayList.get(size-1).getTemperature() + " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity()  + " % 미세먼지 농도 = 나쁨 (" + sensorArrayList.get(size-1).getPM() + "㎍/㎥)");
                } else if (dust >= 151.0) {
                    dustText.setTextColor(Color.RED);
                    dustText.setText("매우나쁨 \n(" + sensorArrayList.get(size-1).getPM() + " ㎍/㎥)");
                   builder.setContentText("온도 = " + sensorArrayList.get(size-1).getTemperature()+ " ℃ 습도 = " + sensorArrayList.get(size-1).getHumidity()  + " % 미세먼지 농도 = 매우나쁨 (" +sensorArrayList.get(size-1).getPM()+ "㎍/㎥)");
                }
                notificationManager.notify(1,builder.build()); //builder에 새로운 값 넣어줌
                return false;
            }
        });

        httpThread = new HttpThread(handler);
        httpThread.start();

        //humidActivity 인스턴스 얻기
        //hactivity=new humidActivity();

        //타이머 스케줄
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // http 요청 보내는 코드 작성

                // 예를 들어 httpThread 클래스의 인스턴스를 생성하여 실행하는 코드
                HttpThread thread = new HttpThread(handler);
                thread.start();
                //  handleSensorDataUpdate(sensorArrayList);
            /*
                //습도 정보 업데이트 기능? 재확인 필요함
                if (hactivity != null) {
                    hactivity.updateArraylist(sensorArrayList); //데이터 업데이트 해주지만 ui 업데이트는 메인에서
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 리스트뷰 업데이트
                            hactivity.updateListView(); //ui업데이트 메인액티비티에서 해줘야함
                        }
                    });
                }
                */
            }
        }, 0, 5000); // 0초 후에 시작하고 5초마다 반복 실행
    }
/*
    //작동 되는데 계속 겹치는 문제점 있는 메소드
    private void handleSensorDataUpdate(ArrayList<Sensor> updatedArrayList) {
        Intent intent = new Intent(this, humidActivity.class);
        intent.putExtra("sensor list", updatedArrayList);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // 이미 열려있는 경우 새로운 인스턴스를 생성하지 않고 기존 인스턴스를 사용
        startActivity(intent);
    }

*/

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    private void signOut() {
        mAuth.signOut();

        //로그아웃 후 앱 시작 화면으로 이동
        Intent intent = new Intent(getApplicationContext(),Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}

