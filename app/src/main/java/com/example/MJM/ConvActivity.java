package com.example.MJM;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class ConvActivity extends AppCompatActivity {

    Button convBtn;    //편의점 버튼
    Button toiletBtn;  //화장실 버튼
    Button bankBtn;    //은행 버튼

    private final GoogleMap mMap;
    private final SearchActivity s;

    public ConvActivity(GoogleMap mMap, SearchActivity s) {
        this.mMap = mMap;
        this.s = s;
    }

//    boolean isPushedConv = true;    //편의점 버튼이 눌렸는지 확인하기 위한 변수
//    boolean isPushedtoilet = true;  //화장실 버튼이 눌렸는지 확인하기 위한 변수
//    boolean isPushedbank = true;    //은행 버튼이 눌렸는지 확인하기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv);

        //아이디 연결
        convBtn = findViewById(R.id.convBtn);
        toiletBtn = findViewById(R.id.toiletBtn);
        bankBtn = findViewById(R.id.bankBtn);

        convBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(s.latitude[17], s.longitude[17])));

//                if(isPushedConv == true){
//                    building.setImageResource(R.drawable.yellow);
//                    building4.setImageResource(R.drawable.yellow);
//                    isPushedConv = false;
//                }
//                else {
//                    building.setImageResource(R.drawable.black);
//                    building4.setImageResource(R.drawable.black);
//                    //convBtn.setBackgroundColor(Color.LTGRAY);
//                    isPushedConv = true;
//                }


            }
        });

        toiletBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(s.latitude[1], s.longitude[1])));

//                if(isPushedtoilet == true){
//                    building2.setImageResource(R.drawable.yellow);
//                    building3.setImageResource(R.drawable.yellow);
//                    isPushedtoilet = false;
//                }
//                else {
//                    building2.setImageResource(R.drawable.black);
//                    building3.setImageResource(R.drawable.black);
//                    //convBtn.setBackgroundColor(Color.LTGRAY);
//                    isPushedtoilet = true;
//                }
            }
        });

        bankBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(s.latitude[16], s.longitude[16])));

//                if(isPushedbank == true){
//                    building5.setImageResource(R.drawable.yellow);
//                    isPushedbank = false;
//                }
//                else {
//                    building5.setImageResource(R.drawable.black);
//                    //convBtn.setBackgroundColor(Color.LTGRAY);
//                    isPushedbank = true;
//                }
            }
        });
    }
}