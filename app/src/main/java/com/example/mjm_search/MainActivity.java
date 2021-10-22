package com.example.mjm_search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> list;
    //데이터 넣을 리스트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<String>();
        settingList();
        //리스트 생성하고,검색될 데이터 추가
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter((new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list)));
        //AutoCompleteTextView에 adapter 연결

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Click_button.class );
                startActivity(intent);
            }
        });
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
}