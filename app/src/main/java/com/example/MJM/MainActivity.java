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
    Button loc; //현재위치 버튼

    Data data = new Data();  //데이터 클래스 참조 변수

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
        loc = (Button)findViewById(R.id.loc);
        //편의시설 버튼 xml과 연결.

        data.list = new ArrayList<String>(); //리스트 생성
        data.settingList(); //리스트에 데이터(강의실번호)를 추가한다
        //리스트 생성하고,검색될 데이터 추가

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        //AutoCompleteTextView : 자동완성 기능 (검색 창에 입력시 밑에 리스트에 넣어둔 강의실 번호가 자동완성으로 뜨게 됨)
        autoCompleteTextView.setAdapter((new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, data.list)));
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
                if (room_num.equals(data.list.get(0))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(1층) 학회실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(1))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(1층) 공과대 학생회실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(2))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(1층) 복사실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(3))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(2층)  CAD 및 컴퓨터 실습실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(4))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(2층) 제3통합교학팀"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(5))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(2층)  캡스톤디자인실(기계)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(6))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(3층) 에너지환경촉매연구실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(7))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(3층) 지능형 시스템 연구실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(8))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(3층) 지능형 시스템 연구실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(9))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(4층) 고분자재료 연구실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(10))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(5층) PC실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(11))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(5층) 신학컨소시엄센터"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(12))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(5층) 물류정보시스템 연구소"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(13))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관").snippet("(6층) 학회실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(14))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 1층) 기업인재개발원 강의실1"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(15))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 2층) 기업인재개발원 강의실2"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(16))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 2층) 기업인재개발원 강의실3"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(17))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 3층) 통합교학팀 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(18))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 3층) 학생회실(생명)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(19))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 4층) 연구실11(생명)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(20))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 4층) 연구실10(생명)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(21))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 5층) 공동기기실(생명)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(22))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 5층) 회의실/PC실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(23))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 6층) BK21연구센터 연구교수실험실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(24))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 6층) 세미나실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(25))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 7층) 연구실9"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(26))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 7층) 대학원실9"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(27))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 8층) 한국재단연구실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(28))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 2공학관").snippet("(에코바이오관 8층) E2FTC Membrane Lab"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(29))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 3공학관").snippet("위치정보 없음"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(30))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 3공학관").snippet("위치정보 없음"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(31))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 3공학관").snippet("위치정보 없음"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(32))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 3공학관").snippet("위치정보 없음"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(33))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 3공학관").snippet("위치정보 없음"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }

                if (room_num.equals(data.list.get(34))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(1층) 학회실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(35))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(1층) PC실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(36))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(1층) 공과대학보유공간"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(37))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(2층) 콘크리트연구실II"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(38))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(2층) 콘크리트연구실II"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(39))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(2층) 세미나실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(40))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(3층) 연구실(구조)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(41))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(3층) 토목연구정보센터"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(42))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 4공학관").snippet("(3층) 워크스테이션실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(43))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(1층) 디자인스튜디오A"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(44))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(1층) CAD/CAM실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(45))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(2층) 시청각실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(46))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(2층) 학생회실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(47))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(3층) 교통정보실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(48))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(3층) 캡스톤설계실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(49))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(3층) 공과대학 제4교학팀"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(50))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(4층) PC실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(51))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(4층) 강의실(PC실 겸용)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(52))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(5층) 인증자료실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(53))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(6층) 세미나실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(54))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(6층)  실습(정보통신공)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(55))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(7층) 학생회실(컴퓨터공)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(56))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관").snippet("(7층) 인증자료실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(57))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("채플관").snippet("(1층) 소연주실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(58))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("채플관").snippet("(1층) 성도석2"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(59))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("채플관").snippet("(2층) 성도석1"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(60))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("채플관").snippet("(2층) 교목실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(61))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("채플관").snippet("(2층) 음향조정실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(62))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(1층) 모형제작실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(63))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(1층) 공간디자인 실습실A"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(64))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(1층) 공간디자인 학회실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(65))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(2층) 공업디자인실습실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(66))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(2층) CG실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(67))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(3층) 학생회실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(68))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(1층) 교수실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(69))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("디자인 조형센터").snippet("(1층) 공간행테디자인연구실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(70))) {
                    LatLng room_marker = new LatLng(data.latitude[21], data.longitude[21]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관").snippet("편의점 (GS25)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(71))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관").snippet("(1층) 사생자치회의실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(72))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관").snippet("(1층) 예배실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(73))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관").snippet("(1층) 매점 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(74))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관").snippet("(3층) 부사감실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(75))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관").snippet("(4층) 관장숙직실(온돌)  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(76))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관").snippet("(4층) 복사실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(77))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(지하 1층) 학생식당 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(78))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(지하 1층) 보존서고 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(79))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(1층) 노트북열람실1  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(80))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(2층) 자료열람실  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(81))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(2층) 전자도서관 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(82))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(2층) 영상자료실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(83))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(3층) 자료열람실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }  if (room_num.equals(data.list.get(84))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(3층) 보존서고 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }  if (room_num.equals(data.list.get(85))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(4층) 노트북열람실2  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(86))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(4층) 열람실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(87))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(5층) 열람실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(88))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(6층) 고고미술실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(89))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당 지하").snippet("(지하 1층) 편의점 GS25 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(90))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당").snippet("(4층) 복사실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(91))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("창조예술관").snippet("(1층) 뮤지컬 보컬 실습실1 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(92))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("창조예술관").snippet("(1층) 뮤지컬 보컬 실습실2 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(93))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("창조예술관").snippet("(1층) 강의실2(뮤지컬)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(94))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("창조예술관").snippet("(1층) 강의실1(뮤지컬)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(95))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("창조예술관").snippet("(1층) 교수실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(96))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("창조예술관").snippet("(1층) 교육지원처 자연학사 지원팀 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(97))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("창조예술관").snippet("(1층) 강의실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(98))) {
                    LatLng room_marker = new LatLng(data.latitude[16], data.longitude[16]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생복지관").snippet("(1층) 하나은행 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(99))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생회관").snippet("(1층) 진료실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(100))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생회관").snippet("(1층) 진료대기실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(101))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생회관").snippet("(1층) 우체국"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(102))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생회관").snippet("(1층) 학생식당"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(103))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생회관").snippet("(1층) 학생회관 회의실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(104))) {
                    LatLng room_marker = new LatLng(data.latitude[23], data.longitude[23]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생회관").snippet("(1층) 커피전문점"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(105))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("함박관").snippet("(1층) 멀티미디어실1  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(106))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("함박관").snippet("(1층) 멀티미디어실2"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(107))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("함박관").snippet("(1층) 영어회화전용강의실(교양)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(108))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("함박관").snippet("(3층) 방목기초교육대학교학팀 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(109))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("함박관").snippet("(3층) 공대회의실(공대학장보실)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(110))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("함박관").snippet("(3층) 영어회화교수실 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }

            
            }
        });

        loc.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 18);
                mMap.moveCamera(cameraUpdate);
            }
        });


    
        convBtn.setOnClickListener(new Button.OnClickListener() { //편의점
            int numClicked = 0;  //버튼이 눌린 횟수를 확인하기 위한 변수

            @Override
            public void onClick(View view) {
                if(numClicked == 0){   //버튼이 눌린 횟수에 따라 각각 다른 위치의 편의시설을 표시
                    LatLng room_marker = new LatLng(data.latitude[17], data.longitude[17]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당 지하 (GS25) ").snippet("(지하 1층) 편의점 GS25 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    LatLng room_marker = new LatLng(data.latitude[18], data.longitude[18]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 3공학관 (CU)").snippet("편의점 CU"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker,16));
                    numClicked = 2;
                } else if(numClicked == 2) {
                    LatLng room_marker = new LatLng(data.latitude[19], data.longitude[19]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("함박관 (세븐일레븐)").snippet("편의점 세븐일레븐"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker,16));
                    numClicked = 3;
                }
                else {
                    LatLng room_marker = new LatLng(data.latitude[20], data.longitude[20]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관 (GS25)").snippet("편의점 GS25"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 0;
                }
            }
        });
        cafeBtn.setOnClickListener(new Button.OnClickListener(){ //카페
            int numClicked = 0;  //버튼이 눌린 횟수를 확인하기 위한 변수

            @Override
            public void onClick(View view){
                if(numClicked == 0){   //버튼이 눌린 횟수에 따라 각각 다른 위치의 편의시설을 표시
                    LatLng room_marker = new LatLng(data.latitude[21], data.longitude[21]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("할리스커피").snippet("HOLLYS"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    LatLng room_marker = new LatLng(data.latitude[22], data.longitude[22]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명덕관 1층").snippet("명덕카페"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 2;
                } else {
                    LatLng room_marker = new LatLng(data.latitude[23], data.longitude[23]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("학생회관 카페").snippet("카페"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 0;
                }
            }
        });
        printerBtn.setOnClickListener(new Button.OnClickListener(){ //
            int numClicked = 0;  //버튼이 눌린 횟수를 확인하기 위한 변수

            @Override
            public void onClick(View view){
                if(numClicked == 0){   //버튼이 눌린 횟수에 따라 각각 다른 위치의 편의시설을 표시
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("명진당 4층").snippet("복사실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    LatLng room_marker = new LatLng(data.latitude[25], data.longitude[25]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 3공학관 지하").snippet("복사실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 2;
                } else if(numClicked == 2) {
                    LatLng room_marker = new LatLng(data.latitude[26], data.longitude[26]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 1공학관 1층").snippet("복사실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 3;
                }
                else {
                    LatLng room_marker = new LatLng(data.latitude[27], data.longitude[27]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("제 5공학관 1층").snippet("복사실"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
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

    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");

        mMap = googleMap;
        setDefaultLocation();
        mUiSettings = mMap.getUiSettings();
          //건물 마커
        LatLng building = new LatLng( 37.22246007484329, 127.18715734722015);
        googleMap.addMarker(new MarkerOptions().position(building).title("제 1공학관").snippet("건물번호: Y"));

        building = new LatLng(  37.22146, 127.18687);
        googleMap.addMarker(new MarkerOptions().position(building).title("제 2공학관").snippet("건물번호: Y8"));

        building = new LatLng(  37.21920, 127.18296);
        googleMap.addMarker(new MarkerOptions().position(building).title("제 3공학관").snippet("건물번호: Y19"));

        building = new LatLng( 37.21891,127.18375);
        googleMap.addMarker(new MarkerOptions().position(building).title("제 4공학관").snippet("건물번호: Y13"));

        building = new LatLng(  37.22202, 127.18758);
        googleMap.addMarker(new MarkerOptions().position(building).title("제 5공학관").snippet("건물번호: Y5"));

        building = new LatLng(37.22475,127.18768);
        googleMap.addMarker(new MarkerOptions().position(building).title("명지대학교 자연캠퍼스").snippet("정문입니다"));

        building = new LatLng(37.22146,127.18684);
        googleMap.addMarker(new MarkerOptions().position(building).title("창조예술관").snippet("건물번호: Y2"));

        building = new LatLng(37.22216,127.18850);
        googleMap.addMarker(new MarkerOptions().position(building).title("명진당").snippet("건물번호: Y3"));

        building = new LatLng(37.22214,127.19080);
        googleMap.addMarker(new MarkerOptions().position(building).title("예체능관").snippet("건물번호: Y6"));

        building = new LatLng(37.22118, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("함박관").snippet("건물번호: Y9"));

        building = new LatLng(37.22039, 127.18534);
        googleMap.addMarker(new MarkerOptions().position(building).title("디자인조형센터").snippet("건물번호: Y12"));

        building = new LatLng(37.22138,127.18926);
        googleMap.addMarker(new MarkerOptions().position(building).title("차세대과학관").snippet("건물번호: Y23"));

        building = new LatLng(37.22379, 127.18687);
        googleMap.addMarker(new MarkerOptions().position(building).title("채플관").snippet("건물번호: Y22"));

        building = new LatLng( 37.22397, 127.18752);
        googleMap.addMarker(new MarkerOptions().position(building).title("통학버스 승강장").snippet("통학버스 승강장 입니다"));

        building = new LatLng(37.22379, 127.18687);
        googleMap.addMarker(new MarkerOptions().position(building).title("학생회관").snippet("건물번호: Y1"));

        building = new LatLng(37.22396, 127.18183);
        googleMap.addMarker(new MarkerOptions().position(building).title("명덕관").snippet("건물번호: Y31"));

        building = new LatLng( 37.22305, 127.186883);
        googleMap.addMarker(new MarkerOptions().position(building).title("학생복지관(은행)").snippet("하나은행"));

        building = new LatLng(37.22210, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("명진당 지하(GS25)").snippet("편의점 (GS25)"));

        building = new LatLng(37.22113,127.18862);
        googleMap.addMarker(new MarkerOptions().position(building).title("제 3공학관 (CU)").snippet("편의점 (CU)"));

        building = new LatLng( 37.22393,  127.18186);
        googleMap.addMarker(new MarkerOptions().position(building).title("함박관 (세븐일레븐)").snippet("편의점 (세븐일레븐"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("명덕관 (GS25)").snippet("편의점 (GS25)"));

        building = new LatLng(37.22210, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("할리스커피").snippet("HOLLYS 카페"));

        building = new LatLng( 37.22393,  127.18186);
        googleMap.addMarker(new MarkerOptions().position(building).title("명덕관 1층").snippet("명덕카페"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("학생회관 카페").snippet("카페"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("복사실").snippet("명진당 4층"));

        building = new LatLng(37.22210, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("복사실").snippet("제 3곻학관 지하"));

        building = new LatLng( 37.22393,  127.18186);
        googleMap.addMarker(new MarkerOptions().position(building).title("복사실").snippet("제 1공학관 1층"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("복사실").snippet("제 5공학관 1층"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(building));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(building, 16));

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

       // for(int i = 0; i < 28; i++){
       //     MarkerOptions MJU = new MarkerOptions();
       //     MJU
       //             .position(new LatLng(data.latitude[i], data.longitude[i]))
       //             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
       //             .alpha(0.5f)
       //             .title(data.title[i])
       //             .snippet(data.memo[i]);
       //     mMap.addMarker(MJU);
       // }
        //마커 작업, 21개의 강의실만 되어있다.
        //카페 3곳 추가
        //복사실 4곳 추가
        
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]), 18f));
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
        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 18);
        //mMap.moveCamera(cameraUpdate);
    }

    public void setDefaultLocation(){
        LatLng DEFAULT_LOCATION = new LatLng(data.latitude[0], data.longitude[0]);
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
































