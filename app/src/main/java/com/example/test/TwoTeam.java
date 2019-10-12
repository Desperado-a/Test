package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

public class TwoTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_team);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea=((TextView)findViewById(R.id.score)).getText().toString();
        String scoreb=((TextView)findViewById(R.id.score2)).getText().toString();

        outState.putString("teama",scorea);
        outState.putString("teamb",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea=savedInstanceState.getString("teama");
        String scoreb=savedInstanceState.getString("teamb");

        ((TextView)findViewById(R.id.score)).setText(scorea);
        ((TextView)findViewById(R.id.score2)).setText(scoreb);
    }

    public void  btn1(View v){
        Add1(1);
    }
    public void  btn2(View v){
        Add1(2);
    }
    public void  btn3(View v){
        Add1(3);
    }
    public void  btn21(View v){
        Add2(1);
    }
    public void  btn22(View v){
        Add2(2);
    }
    public void  btn23(View v){
        Add2(3);
    }

    void Add1 (int i){
        TextView out =(TextView)findViewById(R.id.score);
        String os= (String) out.getText();
        String ns=String.valueOf(Integer.parseInt(os)+i);
        out.setText(ns);

    }
    void Add2 (int i){
        TextView out =(TextView)findViewById(R.id.score2);
        String os= (String) out.getText();
        String ns=String.valueOf(Integer.parseInt(os)+i);
        out.setText(ns);

    }
    public void  rest(View v){
        TextView out =(TextView)findViewById(R.id.score);
        TextView out2 =(TextView)findViewById(R.id.score2);
        out.setText("0");
        out2.setText("0");
    }
}
