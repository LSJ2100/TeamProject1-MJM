package com.example.MJM;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.IOException;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private List<String> list;
    //강의실 번호 데이터 넣은 리스트 변수

    double[] latitude = {37.22475, 37.22146, 37.22246007484329, 37.22146, 37.22216, 37.22214, 37.22118, 37.22039, 37.21920, 37.21891,
            37.22138, 37.22379, 37.22202, 37.22397, 37.22337, 37.22396, 37.22305, 37.22210, 37.21904, 37.22113, 37.22393, 37.22511,
            37.22381, 37.22335, 37.22238, 37.21943, 37.22233, 37.22165};
    double[] longitude = {127.18768, 127.18684, 127.18715734722015, 127.18684, 127.18850, 127.19080, 127.18851, 127.18534, 127.18296, 127.18375,
            127.18926, 127.18687, 127.18758, 127.18752, 127.18716, 127.18183, 127.18688, 127.18851, 127.18250, 127.18862, 127.18186, 127.18717,
            127.18166, 127.18747, 127.18851, 127.18260, 127.18673, 127.18776};
    String[] title = {"자연캠퍼스", "제2공학관", "제1공학관", "창조예술관", "명진당", "예체능관", "함박관", "디자인조형센터", "제3공학관", "제4공학관",
            "차세대과학관", "채플관", "제5공학관", "통학버스 승강장", "학생회관", "명덕관", "학생복지관(은행)", "명진당 지하(GS25)", "3공학관(CU)", "함박관(세븐일레븐)", "명덕관(GS25)",
            "할리스커피", "명덕관 1층", "학생회관 카페", "명진당 4층", "3공학관 지하", "1공학관 1층", "5공학관 1층"};
    String[] memo = {"정문입니다", "건물번호: Y8", "건물번호: Y", "건물번호: Y2", "건물번호: Y3", "건물번호: Y6", "건물번호: Y9", "건물번호: Y12", "건물번호: Y19", "건물번호: Y13",
            "건물번호: Y23", "건물번호: Y22", "건물번호: Y5", "통학버스 승강장입니다.", "건물번호: Y1", "건물번호: Y31", "은행", "GS25", "CU", "세븐일레븐" ,"GS25", "HOLLYS", "명덕카페",
            "카페", "복사실", "복사실", "복사실", "복사실"};
    
    // 위 데이타를 따로 데이타 클래스로 구현 요망

///////////////////
    private GoogleMap mMap; // 구글 맵 참조
    private Marker currentMarker = null;
    private UiSettings mUiSettings;  //UI 컨트롤
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;
    //onRequestPermissionResult 함수의 결과에서 ActivityCompat.requestPermission을 사용한 권한 요청을 구분하기 위해 사용되는 변수
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    //앱 실행을 위해 필요한 권한을 정의
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Location mCurrentLocation; //최근 위치
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout; //Snackbar이용을 위한 View 변수

    Button searchButton; // 검색 버튼
    String room_num; // 강의실 번호
    
    Button convBtn;    //편의점 버튼
    Button cafeBtn;  //카페 버튼
    Button printerBtn;    //복사실 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.layout_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        convBtn = (Button)findViewById(R.id.convBtn);
        cafeBtn = (Button)findViewById(R.id.toiletBtn);
        printerBtn = (Button)findViewById(R.id.bankBtn);
        //편의시설 버튼 xml과 연결.

        list = new ArrayList<String>(); //리스트 생성
        settingList(); //리스트에 데이터(강의실번호)를 추가한다
        //리스트 생성하고,검색될 데이터 추가

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        //AutoCompleteTextView : 자동완성 기능 (검색 창에 입력시 밑에 리스트에 넣어둔 강의실 번호가 자동완성으로 뜨게 됨)
        autoCompleteTextView.setAdapter((new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list)));
        //AutoCompleteTextView에 adapter 연결

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                room_num = ((TextView)view).getText().toString(); // 현재 Textview 값 가져오기
            }
        });

        searchButton = (Button)findViewById(R.id.search_button);
        //"검색" 버튼 구현

        searchButton.setOnClickListener(new Button.OnClickListener() {
            //검색 버튼 클릭시 화면 전환
            @Override
            public void onClick(View v) {

                if((room_num.equals(list.get(0)))||(room_num.equals(list.get(9)))||(room_num.equals(list.get(10)))||(room_num.equals(list.get(11)))||(room_num.equals(list.get(12)))
                        ||(room_num.equals(list.get(13)))||(room_num.equals(list.get(15)))||(room_num.equals(list.get(16)))||(room_num.equals(list.get(17)))){
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[2], longitude[2]))); //Y1~~~
                }
                if(room_num.equals(list.get(18))){
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[12], longitude[12]))); //Y5101
                }
            }
        });

        convBtn.setOnClickListener(new Button.OnClickListener() {
            int numClicked = 0;  //버튼이 눌린 횟수를 확인하기 위한 변수

            @Override
            public void onClick(View view) {
                if(numClicked == 0){   //버튼이 눌린 횟수에 따라 각각 다른 위치의 편의시설을 표시
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[17], longitude[17])));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[18], longitude[18])));
                    numClicked = 2;
                } else if(numClicked == 2) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[19], longitude[19])));
                    numClicked = 3;
                }
                else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[20], longitude[20])));
                    numClicked = 0;
                }
            }
        });
        cafeBtn.setOnClickListener(new Button.OnClickListener(){
            int numClicked = 0;  //버튼이 눌린 횟수를 확인하기 위한 변수

            @Override
            public void onClick(View view){
                if(numClicked == 0){   //버튼이 눌린 횟수에 따라 각각 다른 위치의 편의시설을 표시
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[21], longitude[21])));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[22], longitude[22])));
                    numClicked = 2;
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[23], longitude[23])));
                    numClicked = 0;
                }
            }
        });
        printerBtn.setOnClickListener(new Button.OnClickListener(){
            int numClicked = 0;  //버튼이 눌린 횟수를 확인하기 위한 변수

            @Override
            public void onClick(View view){
                if(numClicked == 0){   //버튼이 눌린 횟수에 따라 각각 다른 위치의 편의시설을 표시
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[24], longitude[24])));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[25], longitude[25])));
                    numClicked = 2;
                } else if(numClicked == 2) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[26], longitude[26])));
                    numClicked = 3;
                }
                else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[27], longitude[27])));
                    numClicked = 0;
                }
            }
        });

//        binding = ActivitySearchBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    private void InSearch(String room_num){
//        // 건물번호에 맞는 인덱스를 찾아야함 , 그 인덱스를 불러와서 좌표,건물이름,메모가져옴
//        if(room_num.equals(list.get(1))){
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[3], longitude[3])));
//        }
//        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[0], longitude[0])));
//    }

    public void settingList() { //검색에 사용될 데이터 리스트 추가
        list.add("Y1234"); // for testing
        list.add("Y2234");
        list.add("Y3234");
        list.add("Y4234");
        list.add("Y5234");
        list.add("Y6234");
        list.add("Y7234");
        list.add("Y8234");
        list.add("Y9234");
        list.add("Y10234"); // 9
        list.add("Y11234");
        list.add("Y12234");
        list.add("Y13234");
        list.add("Y14234");
        list.add("Y15234");
        list.add("Y1244");
        list.add("Y1254");
        list.add("Y1264"); //17th
        list.add("Y5101"); // test 2, 18th 12tude
        //리스트는 추가 가능
    }

    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");

        mMap = googleMap;
        setDefaultLocation();
        mUiSettings = mMap.getUiSettings();
        /*double[] latitude = {37.22475, 37.22146, 37.22246007484329, 37.22146, 37.22216, 37.22214, 37.22118, 37.22039, 37.21920, 37.21891, //9
                37.22138, 37.22379, 37.22202, 37.22397, 37.22337, 37.22396, 37.22305, 37.22210, 37.21904, 37.22113, 37.22393};
                127.18926, 127.18687, 127.18758, 127.18752, 127.18716, 127.18183, 127.18688, 127.18851, 127.18250, 127.18862, 127.18186};
        String[] title = {"자연캠퍼스", "제2공학관", "제1공학관", "창조예술관", "명진당", "예체능관", "함박관", "디자인조형센터", "제3공학관", "제4공학관", //9
                "차세대과학관", "채플관", "제5공학관", "통학버스 승강장", "학생회관", "명덕관", "학생복지관(은행)", "명진당 지하(GS25)", "3공학관(CU)", "함박관(세븐일레븐)", "명덕관(GS25)"};
        String[] memo = {"정문입니다", "건물번호: Y8", "건물번호: Y", "건물번호: Y2", "건물번호: Y3", "건물번호: Y6", "건물번호: Y9", "건물번호: Y12", "건물번호: Y19", "건물번호: Y13", //9
                "건물번호: Y23", "건물번호: Y22", "건물번호: Y5", "통학버스 승강장입니다.", "건물번호: Y1", "건물번호: Y31", "은행", "GS25", "CU", "세븐일레븐" ,"GS25"}; */

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                Snackbar.make(mLayout, "어플 실행을 위해서는 위치 정보 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            }else{
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.d(TAG, "onMapClick: ");
            }
        });

        for(int i = 0; i < 28; i++){
            MarkerOptions MJU = new MarkerOptions();
            MJU
                    .position(new LatLng(latitude[i], longitude[i]))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .alpha(0.5f)
                    .title(title[i])
                    .snippet(memo[i]);
            mMap.addMarker(MJU);
        }
        //마커 작업, 21개의 강의실만 되어있다.
        //카페 3곳 추가
        //복사실 4곳 추가
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]), 18f));
        // 맨 처음 화면, 로비(명지대학교 정문)

        mUiSettings.setZoomControlsEnabled(true);   //확대 축소 버튼 활성화
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();
            if(locationList.size() > 0){
                location = locationList.get(locationList.size() - 1);

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도: " + String.valueOf(location.getLatitude()) + " 경도: " + String.valueOf(location.getLongitude());
                Log.d(TAG, "onLocationResult: " + markerSnippet);

                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocation = location;
            }
        }
    };

    private void startLocationUpdates(){
        if(!checkLocationServicesStatus()){
            Log.d(TAG, "startLocationUpdates: call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else{
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "startLocationUpdates: 권한이 허용되지 않음");
                return;
            }
            Log.d(TAG, "startLocationUpdates: call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if(checkPermission()) mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");

        if(checkPermission()){
            Log.d(TAG, "onStart: call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if(mMap != null) mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mFusedLocationClient != null){
            Log.d(TAG, "onStop: call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public String getCurrentAddress(LatLng latlng){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        }catch (IOException ioException){
            Toast.makeText(this, "지오코더 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this, "잘못된 위치", Toast.LENGTH_LONG).show();
            return "잘못된 위치";
        }

        if(addresses == null || addresses.size() == 0){
            Toast.makeText(this, "위치 없음", Toast.LENGTH_LONG).show();
            return "위치 없음";
        }else{
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet){
        if(currentMarker != null) currentMarker.remove();
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker = mMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 18);
        mMap.moveCamera(cameraUpdate);
    }

    public void setDefaultLocation(){
        LatLng DEFAULT_LOCATION = new LatLng(latitude[0], longitude[0]);
        String markerTitle = "위치정보 없음";
        String markerSnippet = "위치 권한 허용 여부와 GPS 활성화 여부를 확인해주세요.";

        if(currentMarker != null) currentMarker.remove();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 18);
        mMap.moveCamera(cameraUpdate);
    }

    private boolean checkPermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            return true;
        }else return false;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults){
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if(permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length){
            boolean check_result = true;

            for(int result:grandResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    check_result = false;
                    break;
                }
            }

            if(check_result){
                startLocationUpdates();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])){
                    Snackbar.make(mLayout, "권한이 거부되었습니다. 어플 재실행 후 권한을 허용해주세요.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }else{
                    Snackbar.make(mLayout, "권한이 거부되었습니다. 권한을 허용해야 어플 사용이 가능합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화됨");
        builder.setMessage("어플 사용을 위해서는 위치 서비스가 필요합니다. \n 위치 서비스를 허용하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus()){
                    if(checkLocationServicesStatus()){
                        Log.d(TAG, "onActivityResult: GPS 활성화됨");
                        needRequest = true;
                        return;
                    }
                }
                break;
        }
    }

}
































