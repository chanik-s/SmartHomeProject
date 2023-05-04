package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ControlDeviceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_dev);
        Intent intent=getIntent();
        Button btnDevOn=findViewById(R.id.btnDevOn);
        Button btnDevOff=findViewById(R.id.btnDevOff);

        //String url = "http://192.168.25.19/post.php"; //라즈베리파이 ip주소
        //on버튼 누를시
        btnDevOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String on_url = "http://192.168.25.19/post.php"; //라즈베리파이 ip주소

                // Volley 라이브러리를 사용하여 HTTP POST 요청을 처리하기 위한 객체
                StringRequest stringRequest = new StringRequest(Request.Method.POST, on_url,

                        new Response.Listener<String>() {//요청이 성공했을 때 호출될 콜백 함수
                            @Override
                            public void onResponse(String response) {
                                Log.d("POST Response", response);
                            }
                        }, new Response.ErrorListener() { //요청이 실패했을 때 호출될 콜백 함수
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("POST Error", error.toString());
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() { //POST 요청에 담을 데이터를 정의
                        Map<String, String> params = new HashMap<>();
                        params.put("input", "a"); // "input"이라는 키값으로 "a"보냄

                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                //요청을 보낼 RequestQueue를 생성하고, 생성한 StringRequest 객체를 추가하여 요청을 보냅니다.
                // 이때 "this"는 현재 액티비티를 의미

            }
        });

        //off버튼 누를시
        btnDevOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String off_url = "http://192.168.25.19/post.php"; //라즈베리파이 ip주소

                // Volley 라이브러리를 사용하여 HTTP POST 요청을 처리하기 위한 객체
                StringRequest stringRequest = new StringRequest(Request.Method.POST, off_url,

                        new Response.Listener<String>() {//요청이 성공했을 때 호출될 콜백 함수
                            @Override
                            public void onResponse(String response) {
                                Log.d("POST Response", response);
                            }
                        }, new Response.ErrorListener() { //요청이 실패했을 때 호출될 콜백 함수
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("POST Error", error.toString());
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() { //POST 요청에 담을 데이터를 정의
                        Map<String, String> params = new HashMap<>();
                        params.put("input", "b"); // b보내 off일때
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                //요청을 보낼 RequestQueue를 생성하고, 생성한 StringRequest 객체를 추가하여 요청을 보냅니다.
                // 이때 "this"는 현재 액티비티를 의미
            }
        });


    }
}
