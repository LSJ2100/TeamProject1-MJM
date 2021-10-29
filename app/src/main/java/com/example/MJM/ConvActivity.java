package com.example.MJM;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ConvActivity extends AppCompatActivity {

    Button convBtn;    //편의점 버튼
    Button toiletBtn;  //화장실 버튼
    Button bankBtn;    //은행 버튼
    ImageView building;   //건물1
    ImageView building2;  //건물2
    ImageView building3;  //건물3
    ImageView building4;  //건물4
    ImageView building5;  //건물5
    boolean isPushedConv = true;    //편의점 버튼이 눌렸는지 확인하기 위한 변수
    boolean isPushedtoilet = true;  //화장실 버튼이 눌렸는지 확인하기 위한 변수
    boolean isPushedbank = true;    //은행 버튼이 눌렸는지 확인하기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv);

        //아이디 연결
        convBtn = findViewById(R.id.convBtn);
        toiletBtn = findViewById(R.id.toiletBtn);
        bankBtn = findViewById(R.id.bankBtn);
        building = findViewById(R.id.imageView);
        building2 = findViewById(R.id.imageView2);
        building3 = findViewById(R.id.imageView3);
        building4 = findViewById(R.id.imageView4);
        building5 = findViewById(R.id.imageView5);

        convBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){


                if(isPushedConv == true){
                    building.setImageResource(R.drawable.yellow);
                    building4.setImageResource(R.drawable.yellow);
                    isPushedConv = false;
                }
                else {
                    building.setImageResource(R.drawable.black);
                    building4.setImageResource(R.drawable.black);
                    //convBtn.setBackgroundColor(Color.LTGRAY);
                    isPushedConv = true;
                }
            }
        });

        toiletBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(isPushedtoilet == true){
                    building2.setImageResource(R.drawable.yellow);
                    building3.setImageResource(R.drawable.yellow);
                    isPushedtoilet = false;
                }
                else {
                    building2.setImageResource(R.drawable.black);
                    building3.setImageResource(R.drawable.black);
                    //convBtn.setBackgroundColor(Color.LTGRAY);
                    isPushedtoilet = true;
                }
            }
        });

        bankBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(isPushedbank == true){
                    building5.setImageResource(R.drawable.yellow);
                    isPushedbank = false;
                }
                else {
                    building5.setImageResource(R.drawable.black);
                    //convBtn.setBackgroundColor(Color.LTGRAY);
                    isPushedbank = true;
                }
            }
        });

    }
}