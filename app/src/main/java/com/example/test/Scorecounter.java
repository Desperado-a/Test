package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Scorecounter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecounter);
    }

    public void  btn1(View v){
        Add(1);
    }
    public void  btn2(View v){
        Add(2);
    }
    public void  btn3(View v){
        Add(3);
    }
    public void  rest(View v){
        TextView out =(TextView)findViewById(R.id.score);
        out.setText("0");
    }

    void Add (int i){
        TextView out =(TextView)findViewById(R.id.score);
        String os= (String) out.getText();
        String ns=String.valueOf(Integer.parseInt(os)+i);
        out.setText(ns);

    }
}
