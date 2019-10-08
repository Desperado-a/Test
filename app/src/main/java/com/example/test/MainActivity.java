package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.String;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static String TAG = "Main";
    TextView out;
    String str;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        out = findViewById(R.id.result);

        input  = findViewById(R.id.editText);

        Button b = findViewById(R.id.button);
        //b.setOnClickListener(this);
    }

    public void  onClick(View v){
        Log.i(TAG,"onClick: 11111");//日志输出
        float n,F;
        str= input.getText().toString();
        n = Float.parseFloat(str);
        F=n*1.8f+32;
        str="转为华氏摄氏度："+F;
        out.setText(str);
        //out.setText(getString(R.string.re)+String.format("%.2f",F));
    }
}
