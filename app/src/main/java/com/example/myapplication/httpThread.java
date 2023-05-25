package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


    public class httpThread extends Thread { //Json 파싱 클래스
        String TAG = "JsonParseTest"; //디버깅용
        String jsonString;
        //ArrayList<Sensor> sensorArrayList;
        Handler handler;
        ArrayList<Sensor> sensorArrayList;

        public httpThread(Handler handler){

            this.handler=handler;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void run(){
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


                    //InputStream으로 바이트 단위 데이터 얻기보단 Reader 거쳐 문자 읽어(한글 깨짐 해결)
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //버퍼를 통해 문자열 읽기(위에선 문자로 하나하나 처리임)

                    StringBuilder sb = new StringBuilder(); //변경 가능한 문자열 만들어.. String 합치는 작업시 대안 가능(ex> sb.append("문자열").append("연결");
                    String line = null;

                     while ((line = bufferedReader.readLine()) != null) {  //readLine() 입력 메소드   [한 줄(공백포함)전체 읽으므로 String으로 리턴 가능]
                        sb.append(line); //문자열 저장
                    }

                    bufferedReader.close();
                    Log.d(TAG, sb.toString().trim()); //디버깅 상태 확인

                    String result;
                    result=sb.toString().trim();

                    jsonString = result;
                    sensorArrayList = doParse(); //tmpSensorArray 값

                    //handler.obtainMessage(0,sensorArrayList).sendToTarget();
                    Message msg=handler.obtainMessage();
                    msg.obj=new ArrayList<>(sensorArrayList);
                    handler.sendMessage(msg);
                    //what -> 메세지 종류 구별
                   httpURLConnection.disconnect(); //꼭 할 필요 있나?????
                }
            } catch (Exception e) {
                Log.d(TAG, "InsertData:ERROR", e);
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



