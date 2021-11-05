package com.example.MJM;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private List<String> list;
    //강의실 번호 데이터 넣은 리스트 변수

    double[] latitude = {37.22475, 37.22146, 37.22246007484329, 37.22146, 37.22216, 37.22214, 37.22118, 37.22039, 37.21920, 37.21891,
            37.22138, 37.22379, 37.22202, 37.22397, 37.22337, 37.22396, 37.22305, 37.22210, 37.21904, 37.22113, 37.22393};
    double[] longitude = {127.18768, 127.18684, 127.18715734722015, 127.18684, 127.18850, 127.19080, 127.18851, 127.18534, 127.18296, 127.18375,
            127.18926, 127.18687, 127.18758, 127.18752, 127.18716, 127.18183, 127.18688, 127.18851, 127.18250, 127.18862, 127.18186};
    String[] title = {"자연캠퍼스", "제2공학관", "제1공학관", "창조예술관", "명진당", "예체능관", "함박관", "디자인조형센터", "제3공학관", "제4공학관",
            "차세대과학관", "채플관", "제5공학관", "통학버스 승강장", "학생회관", "명덕관", "학생복지관(은행)", "명진당 지하(GS25)", "3공학관(CU)", "함박관(세븐일레븐)", "명덕관(GS25)"};
    String[] memo = {"정문입니다", "건물번호: Y8", "건물번호: Y", "건물번호: Y2", "건물번호: Y3", "건물번호: Y6", "건물번호: Y9", "건물번호: Y12", "건물번호: Y19", "건물번호: Y13",
            "건물번호: Y23", "건물번호: Y22", "건물번호: Y5", "통학버스 승강장입니다.", "건물번호: Y1", "건물번호: Y31", "은행", "GS25", "CU", "세븐일레븐" ,"GS25"};
    
    // 위 데이타를 따로 데이타 클래스로 구현 요망

///////////////////
    private GoogleMap mMap; // 구글 맵 참조
    Button searchButton; // 검색 버튼
    String room_num; // 강의실 번호
    
    Button convBtn;    //편의점 버튼
    Button toiletBtn;  //화장실 버튼
    Button bankBtn;    //은행 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convBtn = (Button)findViewById(R.id.convBtn);
        toiletBtn = (Button)findViewById(R.id.toiletBtn);
        bankBtn = (Button)findViewById(R.id.bankBtn);
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
            @Override
            public void onClick(View view) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[17], longitude[17])));
            }
        });
        toiletBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[1], longitude[1])));

            }
        });
        bankBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude[16], longitude[16])));

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
        mMap = googleMap;
//        double[] latitude = {37.22475, 37.22146, 37.22246007484329, 37.22146, 37.22216, 37.22214, 37.22118, 37.22039, 37.21920, 37.21891, //9
//                37.22138, 37.22379, 37.22202, 37.22397, 37.22337, 37.22396, 37.22305, 37.22210, 37.21904, 37.22113, 37.22393};
//        double[] longitude = {127.18768, 127.18684, 127.18715734722015, 127.18684, 127.18850, 127.19080, 127.18851, 127.18534, 127.18296, 127.18375, //9
//                127.18926, 127.18687, 127.18758, 127.18752, 127.18716, 127.18183, 127.18688, 127.18851, 127.18250, 127.18862, 127.18186};
//        String[] title = {"자연캠퍼스", "제2공학관", "제1공학관", "창조예술관", "명진당", "예체능관", "함박관", "디자인조형센터", "제3공학관", "제4공학관", //9
//                "차세대과학관", "채플관", "제5공학관", "통학버스 승강장", "학생회관", "명덕관", "학생복지관(은행)", "명진당 지하(GS25)", "3공학관(CU)", "함박관(세븐일레븐)", "명덕관(GS25)"};
//        String[] memo = {"정문입니다", "건물번호: Y8", "건물번호: Y", "건물번호: Y2", "건물번호: Y3", "건물번호: Y6", "건물번호: Y9", "건물번호: Y12", "건물번호: Y19", "건물번호: Y13", //9
//                "건물번호: Y23", "건물번호: Y22", "건물번호: Y5", "통학버스 승강장입니다.", "건물번호: Y1", "건물번호: Y31", "은행", "GS25", "CU", "세븐일레븐" ,"GS25"};

        for(int i = 0; i < 21; i++){
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
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]), 18f));
        // 맨 처음 화면, 로비(명지대학교 정문)
    }
}