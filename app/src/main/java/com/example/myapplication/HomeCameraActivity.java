package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeCameraActivity extends AppCompatActivity  {

    //홈 캠 스트리밍
    WebView webView;
    WebSettings webSettings;

    //CCTV ON OFF BTN
    Button cctvOnButton,cctvOffButton;
    TextView callText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homecamera);


        webView=findViewById(R.id.cctvWeb);
        callText = (TextView) findViewById(R.id.callText); //신고하기
        cctvOnButton = (Button) findViewById(R.id.cctvOnButton);
        cctvOffButton = (Button) findViewById(R.id.cctvOffButton);

        //웹뷰설정내용 ->공부 필요
        webSettings = webView.getSettings(); //기본적으로 자바스크립트는 WebView에서 사용 중지
        webSettings.setJavaScriptEnabled(true);
        //zoom 기능--확대 축소
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        //on시
        cctvOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //webview.loadUrl 사용시 웹을 통째로 가져와서 불필요함
                webView.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} " +
                                "img{width:100%25;} div{overflow: hidden;} </style></head>" +
                                "<body><div><img src='http://" + "192.168.25.19" + ":8091/?action=stream/'/></div></body></html>",
                        "text/html", "UTF-8");
            }
        });
        //off시
        cctvOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //0405과제:""webview 끄는거 찾기!!!!!!!
                //webView를 담고 있는 액티비티를 종료하면 된다?
                finish(); //액티비티 종료
            }
        });
        /*
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    webView.reload(); //현재 웹뷰 새로고침
                }
                return true;
            }
        }); // WebView 터치 시 새로고침
            */
        //신고하기 기능
        callText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeCameraActivity.this);
                builder.setTitle("신고");
                builder.setMessage("신고하시겠습니까?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"));
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }); // 신고하기 버튼 클릭 시 112 전화걸기로 이동





    }


}
