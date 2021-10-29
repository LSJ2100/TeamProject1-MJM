package com.example.MJM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.MJM.databinding.ActivityMapsBinding;
import com.example.MJM.databinding.ActivitySearchBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback {
    private List<String> list;
    //데이터 넣을 리스트

///////////////////
    private GoogleMap mMap;
    private ActivitySearchBinding binding;
    ///////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        list = new ArrayList<String>();
        settingList();
        //리스트 생성하고,검색될 데이터 추가
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter((new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list)));
        //AutoCompleteTextView에 adapter 연결

        Button searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Click_button.class );
                startActivity(intent);
            }
        });
//
//        binding = ActivitySearchBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void settingList() {
        list.add("Y1234");
        list.add("Y2234");
        list.add("Y3234");
        list.add("Y4234");
        list.add("Y5234");
        list.add("Y6234");
        list.add("Y7234");
        list.add("Y8234");
        list.add("Y9234");
        list.add("Y10234");
        list.add("Y11234");
        list.add("Y12234");
        list.add("Y13234");
        list.add("Y14234");
        list.add("Y15234");
        list.add("Y1244");
        list.add("Y1254");
        list.add("Y1264");
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double[] latitude = {37.22475, 37.22146, 37.22246, 37.22146, 37.22216, 37.22214, 37.22118, 37.22039, 37.21920, 37.21891,
                37.22138, 37.22379, 37.22202, 37.22397, 37.22337, 37.22396, 37.22305, 37.22210, 37.21904, 37.22113, 37.22393};
        double[] longitude = {127.18768, 127.18684, 127.186712, 127.18684, 127.18850, 127.19080, 127.18851, 127.18534, 127.18296, 127.18375,
                127.18926, 127.18687, 127.18758, 127.18752, 127.18716, 127.18183, 127.18688, 127.18851, 127.18250, 127.18862, 127.18186};
        String[] title = {"자연캠퍼스", "제2공학관", "제1공학관", "창조예술관", "명진당", "예체능관", "함박관", "디자인조형센터", "제3공학관", "제4공학관",
                "차세대과학관", "채플관", "제5공학관", "통학버스 승강장", "학생회관", "명덕관", "학생복지관(은행)", "명진당 지하(GS25)", "3공학관(CU)", "함박관(세븐일레븐)", "명덕관(GS25)"};
        String[] memo = {"정문입니다", "건물번호: Y8", "건물번호: Y", "건물번호: Y2", "건물번호: Y3", "건물번호: Y6", "건물번호: Y9", "건물번호: Y12", "건물번호: Y19", "건물번호: Y13",
                "건물번호: Y23", "건물번호: Y22", "건물번호: Y5", "통학버스 승강장입니다.", "건물번호: Y1", "건물번호: Y31", "은행", "GS25", "CU", "세븐일레븐" ,"GS25"};

        for(int i = 0; i < 16; i++){
            MarkerOptions MJU = new MarkerOptions();
            MJU
                    .position(new LatLng(latitude[i], longitude[i]))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .alpha(0.5f)
                    .title(title[i])
                    .snippet(memo[i]);
            mMap.addMarker(MJU);
        }

        // Add a marker in Sydney and move the camera
        /*LatLng MJU = new LatLng(37.22475, 127.18768);
        mMap.addMarker(new MarkerOptions().position(MJU).title("명지대학교 정문"));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]), 18f));
    }
}