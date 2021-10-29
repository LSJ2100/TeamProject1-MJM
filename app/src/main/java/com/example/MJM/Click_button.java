package com.example.MJM;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Click_button extends AppCompatActivity {
    protected void onCreat(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_button);
        //setContentView(R.layout.click_button); 를 통해서 click_button.xml로 이동
        //click_button.xml이 다음 지도 구현 되는 곳으로 넘어가면 될 것 같아요
    }
}
