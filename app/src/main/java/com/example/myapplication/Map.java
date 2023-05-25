package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.os.LocaleListCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Map extends AppCompatActivity implements OnMapReadyCallback, LocationListener, OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //구글맵 초기화


        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();
        //파이어베이스 실시간 데이터베이스 객체 초기화
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        setupMap();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없는 경우의 요청
            ActivityCompat.requestPermissions(Map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //권한이 있는 경우 위치 정보 가져오기
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,this);
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //비동기적으로 GoogleMap 객체 가져오기
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng currentPosition = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentPosition).title("접속 위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,15));

    }






    /*
        //사용자의 현재 위치 가져오기
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);


        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //권한이 없는 경우의 요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //권한이 있는 경우 위치 정보 가져오기
                Location location = locationManager.getLastKnownLocation(provider);

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    //사용자의 현재 위치를 지도에 표시하기

                    currentPosition = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(currentPosition).title("접속 위치"));


                    //mMap.addMarker(new MarkerOptions().position(currentPosition).title("접속 위치"));


                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));


            }
        }*/
    //접속위치 파이어베이스에 저장





}