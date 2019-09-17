package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static String TAG = "Main";
    TextView out;
    String str;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        out = findViewById(R.id.tv);
        out.setText("皇帝的新字");

        input  = findViewById(R.id.editText);

        Button b = findViewById(R.id.button);
        //b.setOnClickListener(this);
    }

    public void  onClick(View v){
        Log.i(TAG,"onClick: 11111");//日志输出
        str= input.getText().toString();
        out.setText(str);
    }
}
