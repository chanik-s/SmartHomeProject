package com.example.myapplication;

import static android.content.ContentValues.TAG;

import static com.example.myapplication.Login.loginIdN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GoogleMap extends AppCompatActivity implements OnMapReadyCallback{
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1 ;
    //, LocationListener, OnRequestPermissionsResultCallback {
    private boolean locationPermissionGranted;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Location lastKnownLocation;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private CameraPosition cameraPosition;

    private com.google.android.gms.maps.GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient; //위치 정보를 관리하고 요청하는데 사용되는 클래스

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
   // private FirebaseUser mUser;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    //private  String receivedLoginIdN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //receivedLoginIdN = getIntent().getStringExtra("loginIdN");

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);// 기기의 현재 위치와 위치 근처의 장소를 검색


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // 현재 컨텍스트(this)를 기반 객체 생성

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }



    @Override
    public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)  // <권한> fine=>정확한 정보 , coarse=>러프한 정보
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},  //String[] 형태로 권한을 전달하는 이유는 한 번에 여러 개의 권한을 요청할 수 있기 때문
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);  //권한 부여
        }

    }
    //권한 요청의 결과를 처리
    @Override      // 콜백을 재정의
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }


    private void updateLocationUI() { //지도에 위치 컨트롤을 설정
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void getDeviceLocation() { //마지막으로 알려진 기기 위치를 찾은 다음 해당 위치를 지도에 지정
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                              //          new LatLng(lastKnownLocation.getLatitude(),
                                //                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                updateLoginLocationFromFb(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());//파이어베이스 저장
                                //LatLng loginLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                readFirebaseAndMarker(mMap);
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }





    //여기서부터 예전 내 코드들
    private  void readFirebaseAndMarker(@NonNull com.google.android.gms.maps.GoogleMap mMap){
        //데이터베이스 가져오기(읽기)
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userReference = databaseReference.child("users").child(userId);

            //파이어베이스 데이터베이스에서 값 읽기
            //loginIdN static으로 공유해서 문제생길수도 있음

           // userReference.child("loginHistory").child(loginIdN)
            userReference.child("loginHistory")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // if (dataSnapshot.exists()) {
                    for(DataSnapshot loginSnapshot : dataSnapshot.getChildren()) {
                       // Double latitudeValue = dataSnapshot.child("latitude").getValue(Double.class);
                       // Double longitudeValue = dataSnapshot.child("longitude").getValue(Double.class);
                        Double latitudeValue = loginSnapshot.child("latitude").getValue(Double.class);
                        Double longitudeValue = loginSnapshot.child("longitude").getValue(Double.class);

                        if (latitudeValue != null && longitudeValue != null) {
                            double latitude = latitudeValue;
                            double longitude = longitudeValue;

                            LatLng loginLocation = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(loginLocation).title("로그인 위치"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loginLocation, 16));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MapActivity", "Error retrieving login location from Firebase", databaseError.toException());
                }
            });
        }
    }

    private void updateLoginLocationFromFb(double latitude, double longitude) {

        //데이터베이스 가져오기(쓰기)
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userReference = databaseReference.child("users").child(userId);

           // HashMap<String, Object> locationData = new HashMap<>();//새로운 객체말고 Login.java에서 정보 가져와야..
           // locationData.put("latitude", latitude);
            //locationData.put("longitude", longitude);

          //  String receivedLoginIdN = getIntent().getStringExtra("loginIdN");
            //loginIdN static으로 공유해서 문제생길수도 있음
            userReference.child("loginHistory").child(loginIdN).child("latitude").setValue(latitude)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("MapActivity", "Login location updated in Firebase");
                            } else {
                                Log.e("MapActivity", "Failed to update login location in Firebase", task.getException());
                            }
                        }
                    });
            userReference.child("loginHistory").child(loginIdN).child("longitude").setValue(longitude)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("MapActivity", "Login location updated in Firebase");
                            } else {
                                Log.e("MapActivity", "Failed to update login location in Firebase", task.getException());
                            }
                        }
                    });

        }
        else{
            Log.d("MapActivity", "No User is signed in");
        }

    }

    /*
    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation() //위치 가져와
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            updateLoginLocationFromFb(location.getLatitude(), location.getLongitude());
                        }
                    });
        }

    }*/


}
