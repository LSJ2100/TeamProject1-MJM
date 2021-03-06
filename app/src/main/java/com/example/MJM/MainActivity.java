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

    private GoogleMap mMap; // ?????? ??? ??????
    private Marker currentMarker = null;
    private UiSettings mUiSettings;  //UI ?????????
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;
    //onRequestPermissionResult ????????? ???????????? ActivityCompat.requestPermission??? ????????? ?????? ????????? ???????????? ?????? ???????????? ??????
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    //??? ????????? ?????? ????????? ????????? ??????
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Location mCurrentLocation; //?????? ??????
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout; //Snackbar????????? ?????? View ??????

    Button searchButton; // ?????? ??????
    String room_num; // ????????? ??????
    
    Button convBtn;    //????????? ??????
    Button cafeBtn;  //?????? ??????
    Button printerBtn;    //????????? ??????
    Button loc; //???????????? ??????

    Data data = new Data();  //????????? ????????? ?????? ??????

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
        //???????????? ?????? xml??? ??????.

        data.list = new ArrayList<String>(); //????????? ??????
        data.settingList(); //???????????? ?????????(???????????????)??? ????????????
        //????????? ????????????,????????? ????????? ??????

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        //AutoCompleteTextView : ???????????? ?????? (?????? ?????? ????????? ?????? ???????????? ????????? ????????? ????????? ?????????????????? ?????? ???)
        autoCompleteTextView.setAdapter((new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, data.list)));
        //AutoCompleteTextView??? adapter ??????

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                room_num = ((TextView)view).getText().toString(); // ?????? Textview ??? ????????????
            }
        });

        searchButton = (Button)findViewById(R.id.search_button);
        //"??????" ?????? ??????

        searchButton.setOnClickListener(new Button.OnClickListener() {
            //?????? ?????? ????????? ?????? ??????
            @Override
            public void onClick(View v) {
                if (room_num.equals(data.list.get(0))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(1???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(1))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(1???) ????????? ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(2))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(1???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(3))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(2???)  CAD ??? ????????? ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(4))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(2???) ???3???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(5))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(2???)  ?????????????????????(??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(6))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(3???) ??????????????????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(7))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(3???) ????????? ????????? ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(8))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(3???) ????????? ????????? ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(9))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(4???) ??????????????? ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(10))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(5???) PC???"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(11))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(5???) ????????????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(12))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(5???) ????????????????????? ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(13))) {
                    LatLng room_marker = new LatLng(data.latitude[2], data.longitude[2]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1?????????").snippet("(6???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(14))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 1???) ????????????????????? ?????????1"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(15))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 2???) ????????????????????? ?????????2"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(16))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 2???) ????????????????????? ?????????3"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(17))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 3???) ??????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(18))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 3???) ????????????(??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(19))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 4???) ?????????11(??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(20))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 4???) ?????????10(??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(21))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 5???) ???????????????(??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(22))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 5???) ?????????/PC???"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(23))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 6???) BK21???????????? ?????????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(24))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 6???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(25))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 7???) ?????????9"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(26))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 7???) ????????????9"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(27))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 8???) ?????????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(28))) {
                    LatLng room_marker = new LatLng(data.latitude[1], data.longitude[1]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 2?????????").snippet("(?????????????????? 8???) E2FTC Membrane Lab"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(29))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 3?????????").snippet("???????????? ??????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(30))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 3?????????").snippet("???????????? ??????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(31))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 3?????????").snippet("???????????? ??????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(32))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 3?????????").snippet("???????????? ??????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(33))) {
                    LatLng room_marker = new LatLng(data.latitude[8], data.longitude[8]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 3?????????").snippet("???????????? ??????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }

                if (room_num.equals(data.list.get(34))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(1???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(35))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(1???) PC???"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(36))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(1???) ????????????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(37))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(2???) ?????????????????????II"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(38))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(2???) ?????????????????????II"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(39))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(2???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(40))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(3???) ?????????(??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(41))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(3???) ????????????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(42))) {
                    LatLng room_marker = new LatLng(data.latitude[9], data.longitude[9]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 4?????????").snippet("(3???) ?????????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(43))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(1???) ?????????????????????A"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(44))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(1???) CAD/CAM???"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(45))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(2???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(46))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(2???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(47))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(3???) ???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(48))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(3???) ??????????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(49))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(3???) ???????????? ???4?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(50))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(4???) PC???"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(51))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(4???) ?????????(PC??? ??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(52))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(5???) ??????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(53))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(6???) ???????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(54))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(6???)  ??????(???????????????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(55))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(7???) ????????????(????????????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(56))) {
                    LatLng room_marker = new LatLng(data.latitude[12], data.longitude[12]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5?????????").snippet("(7???) ??????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(57))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(58))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ?????????2"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(59))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(2???) ?????????1"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(60))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(2???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(61))) {
                    LatLng room_marker = new LatLng(data.latitude[11], data.longitude[11]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(2???) ???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(62))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(1???) ???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(63))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(1???) ??????????????? ?????????A"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(64))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(1???) ??????????????? ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(65))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(2???) ???????????????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(66))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(2???) CG???"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(67))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(3???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(68))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(1???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(69))) {
                    LatLng room_marker = new LatLng(data.latitude[7], data.longitude[7]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ????????????").snippet("(1???) ?????????????????????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(70))) {
                    LatLng room_marker = new LatLng(data.latitude[21], data.longitude[21]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("????????? (GS25)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(71))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ????????????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(72))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(73))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ?????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(74))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(3???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(75))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(4???) ???????????????(??????)  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(76))) {
                    LatLng room_marker = new LatLng(data.latitude[15], data.longitude[15]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(4???) ????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(77))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(?????? 1???) ???????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(78))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(?????? 1???) ???????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(79))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ??????????????????1  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(80))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(2???) ???????????????  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(81))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(2???) ??????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(82))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(2???) ??????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(83))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(3???) ???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }  if (room_num.equals(data.list.get(84))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(3???) ???????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }  if (room_num.equals(data.list.get(85))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(4???) ??????????????????2  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(86))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(4???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(87))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(5???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(88))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(6???) ??????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(89))) {
                    LatLng room_marker = new LatLng(data.latitude[4], data.longitude[4]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ??????").snippet("(?????? 1???) ????????? GS25 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(90))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(4???) ????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(91))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ????????? ?????? ?????????1 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(92))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ????????? ?????? ?????????2 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(93))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ?????????2(?????????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(94))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ?????????1(?????????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(95))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(96))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ??????????????? ???????????? ????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(97))) {
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(98))) {
                    LatLng room_marker = new LatLng(data.latitude[16], data.longitude[16]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("(1???) ???????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(99))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????????").snippet("(1???) ????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(100))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????????").snippet("(1???) ???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(101))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????????").snippet("(1???) ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(102))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????????").snippet("(1???) ????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(103))) {
                    LatLng room_marker = new LatLng(data.latitude[14], data.longitude[14]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????????").snippet("(1???) ???????????? ?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(104))) {
                    LatLng room_marker = new LatLng(data.latitude[23], data.longitude[23]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????????").snippet("(1???) ???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(105))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ??????????????????1  "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(106))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ??????????????????2"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(107))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(1???) ???????????????????????????(??????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(108))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(3???) ????????????????????????????????? "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(109))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(3???) ???????????????(??????????????????)"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                }
                if (room_num.equals(data.list.get(110))) {
                    LatLng room_marker = new LatLng(data.latitude[6], data.longitude[6]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("?????????").snippet("(3???) ????????????????????? "));
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


    
        convBtn.setOnClickListener(new Button.OnClickListener() { //?????????
            int numClicked = 0;  //????????? ?????? ????????? ???????????? ?????? ??????

            @Override
            public void onClick(View view) {
                if(numClicked == 0){   //????????? ?????? ????????? ?????? ?????? ?????? ????????? ??????????????? ??????
                    LatLng room_marker = new LatLng(data.latitude[17], data.longitude[17]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? ?????? (GS25) ").snippet("(?????? 1???) ????????? GS25 "));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    LatLng room_marker = new LatLng(data.latitude[18], data.longitude[18]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 3????????? (CU)").snippet("????????? CU"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker,16));
                    numClicked = 2;
                } else if(numClicked == 2) {
                    LatLng room_marker = new LatLng(data.latitude[19], data.longitude[19]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? (???????????????)").snippet("????????? ???????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker,16));
                    numClicked = 3;
                }
                else {
                    LatLng room_marker = new LatLng(data.latitude[20], data.longitude[20]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? (GS25)").snippet("????????? GS25"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 0;
                }
            }
        });
        
        cafeBtn.setOnClickListener(new Button.OnClickListener(){ //??????
            int numClicked = 0;  //????????? ?????? ????????? ???????????? ?????? ??????

            @Override
            public void onClick(View view){
                if(numClicked == 0){   //????????? ?????? ????????? ?????? ?????? ?????? ????????? ??????????????? ??????
                    LatLng room_marker = new LatLng(data.latitude[21], data.longitude[21]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????????").snippet("HOLLYS"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    LatLng room_marker = new LatLng(data.latitude[22], data.longitude[22]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? 1???").snippet("????????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 2;
                } else {
                    LatLng room_marker = new LatLng(data.latitude[23], data.longitude[23]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("???????????? ??????").snippet("??????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 0;
                }
            }
        });
        printerBtn.setOnClickListener(new Button.OnClickListener(){ //
            int numClicked = 0;  //????????? ?????? ????????? ???????????? ?????? ??????

            @Override
            public void onClick(View view){
                if(numClicked == 0){   //????????? ?????? ????????? ?????? ?????? ?????? ????????? ??????????????? ??????
                    LatLng room_marker = new LatLng(data.latitude[24], data.longitude[24]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("????????? 4???").snippet("?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 1;
                } else if(numClicked == 1) {
                    LatLng room_marker = new LatLng(data.latitude[25], data.longitude[25]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 3????????? ??????").snippet("?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 2;
                } else if(numClicked == 2) {
                    LatLng room_marker = new LatLng(data.latitude[26], data.longitude[26]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 1????????? 1???").snippet("?????????"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(room_marker, 16));
                    numClicked = 3;
                }
                else {
                    LatLng room_marker = new LatLng(data.latitude[27], data.longitude[27]);
                    mMap.addMarker(new MarkerOptions().position(room_marker).title("??? 5????????? 1???").snippet("?????????"));
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
          //?????? ??????
        LatLng building = new LatLng( 37.22246007484329, 127.18715734722015);
        googleMap.addMarker(new MarkerOptions().position(building).title("??? 1?????????").snippet("????????????: Y"));

        building = new LatLng(  37.22146, 127.18687);
        googleMap.addMarker(new MarkerOptions().position(building).title("??? 2?????????").snippet("????????????: Y8"));

        building = new LatLng(  37.21920, 127.18296);
        googleMap.addMarker(new MarkerOptions().position(building).title("??? 3?????????").snippet("????????????: Y19"));

        building = new LatLng( 37.21891,127.18375);
        googleMap.addMarker(new MarkerOptions().position(building).title("??? 4?????????").snippet("????????????: Y13"));

        building = new LatLng(  37.22202, 127.18758);
        googleMap.addMarker(new MarkerOptions().position(building).title("??? 5?????????").snippet("????????????: Y5"));

        building = new LatLng(37.22475,127.18768);
        googleMap.addMarker(new MarkerOptions().position(building).title("??????????????? ???????????????").snippet("???????????????"));

        building = new LatLng(37.22146,127.18684);
        googleMap.addMarker(new MarkerOptions().position(building).title("???????????????").snippet("????????????: Y2"));

        building = new LatLng(37.22216,127.18850);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("????????????: Y3"));

        building = new LatLng(37.22214,127.19080);
        googleMap.addMarker(new MarkerOptions().position(building).title("????????????").snippet("????????????: Y6"));

        building = new LatLng(37.22118, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("????????????: Y9"));

        building = new LatLng(37.22039, 127.18534);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????????????????").snippet("????????????: Y12"));

        building = new LatLng(37.22138,127.18926);
        googleMap.addMarker(new MarkerOptions().position(building).title("??????????????????").snippet("????????????: Y23"));

        building = new LatLng(37.22379, 127.18687);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("????????????: Y22"));

        building = new LatLng( 37.22397, 127.18752);
        googleMap.addMarker(new MarkerOptions().position(building).title("???????????? ?????????").snippet("???????????? ????????? ?????????"));

        building = new LatLng(37.22379, 127.18687);
        googleMap.addMarker(new MarkerOptions().position(building).title("????????????").snippet("????????????: Y1"));

        building = new LatLng(37.22396, 127.18183);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("????????????: Y31"));

        building = new LatLng( 37.22305, 127.186883);
        googleMap.addMarker(new MarkerOptions().position(building).title("???????????????(??????)").snippet("????????????"));

        building = new LatLng(37.22210, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("????????? ??????(GS25)").snippet("????????? (GS25)"));

        building = new LatLng(37.22113,127.18862);
        googleMap.addMarker(new MarkerOptions().position(building).title("??? 3????????? (CU)").snippet("????????? (CU)"));

        building = new LatLng( 37.22393,  127.18186);
        googleMap.addMarker(new MarkerOptions().position(building).title("????????? (???????????????)").snippet("????????? (???????????????"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("????????? (GS25)").snippet("????????? (GS25)"));

        building = new LatLng(37.22210, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("???????????????").snippet("HOLLYS ??????"));

        building = new LatLng( 37.22393,  127.18186);
        googleMap.addMarker(new MarkerOptions().position(building).title("????????? 1???").snippet("????????????"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("???????????? ??????").snippet("??????"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("????????? 4???"));

        building = new LatLng(37.22210, 127.18851);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("??? 3????????? ??????"));

        building = new LatLng( 37.22393,  127.18186);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("??? 1????????? 1???"));

        building = new LatLng(37.22511,127.187171);
        googleMap.addMarker(new MarkerOptions().position(building).title("?????????").snippet("??? 5????????? 1???"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(building));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(building, 16));

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                Snackbar.make(mLayout, "?????? ????????? ???????????? ?????? ?????? ????????? ???????????????.", Snackbar.LENGTH_INDEFINITE).setAction("??????", new View.OnClickListener() {
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

     
        mUiSettings.setZoomControlsEnabled(true);   //?????? ?????? ?????? ?????????
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
                String markerSnippet = "??????: " + String.valueOf(location.getLatitude()) + " ??????: " + String.valueOf(location.getLongitude());
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
                Log.d(TAG, "startLocationUpdates: ????????? ???????????? ??????");
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
            Toast.makeText(this, "???????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????????";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this, "????????? ??????", Toast.LENGTH_LONG).show();
            return "????????? ??????";
        }

        if(addresses == null || addresses.size() == 0){
            Toast.makeText(this, "?????? ??????", Toast.LENGTH_LONG).show();
            return "?????? ??????";
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
        String markerTitle = "???????????? ??????";
        String markerSnippet = "?????? ?????? ?????? ????????? GPS ????????? ????????? ??????????????????.";

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
                    Snackbar.make(mLayout, "????????? ?????????????????????. ?????? ????????? ??? ????????? ??????????????????.", Snackbar.LENGTH_INDEFINITE).setAction("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }else{
                    Snackbar.make(mLayout, "????????? ?????????????????????. ????????? ???????????? ?????? ????????? ???????????????.", Snackbar.LENGTH_INDEFINITE).setAction("??????", new View.OnClickListener() {
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
        builder.setTitle("?????? ????????? ???????????????");
        builder.setMessage("?????? ????????? ???????????? ?????? ???????????? ???????????????. \n ?????? ???????????? ?????????????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
                        Log.d(TAG, "onActivityResult: GPS ????????????");
                        needRequest = true;
                        return;
                    }
                }
                break;
        }
    }

}
































