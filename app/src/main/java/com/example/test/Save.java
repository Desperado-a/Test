package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Save extends AppCompatActivity {

    private  final String TAG = "On_save";
    private EditText dollarText;
    private EditText euroText;
    private EditText wonText;
    SharedPreferences data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

       final Intent config = getIntent();
        float dollar2=config.getFloatExtra("dollar_rate_key",1.2f);
        float euro2=config.getFloatExtra("euro_rate_key",0.0f);
        float won2=config.getFloatExtra("won_rate_key",0.0f);

        dollarText=findViewById(R.id.dr);
        euroText=findViewById(R.id.er);
        wonText=findViewById(R.id.wr);

        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

        Log.i(TAG,"save_dollar："+dollar2);
        Log.i(TAG,"save_euro："+euro2);
        Log.i(TAG,"save_won："+won2);
    }

    public void  save(View v){
        float new_dollar2 = Float.parseFloat(dollarText.getText().toString());
        float new_euro2 = Float.parseFloat(euroText.getText().toString());
        float new_won2 = Float.parseFloat(wonText.getText().toString());

        Intent intent=getIntent();

        /*Bundle bd = new Bundle();
        bd.putFloat("new_dollar_rate", new_dollar2);
        bd.putFloat("new_euro_rate", new_euro2);
        bd.putFloat("new_won_rate", new_won2);
        intent.putExtras(bd);*/

        data = getSharedPreferences("rate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=  data.edit();
        editor.putFloat("dollar_rate",new_dollar2);
        editor.putFloat("euro_rate",new_euro2);
        editor.putFloat("won_rate",new_won2);
        editor.apply();

        Log.i(TAG,"new_save_dollar："+new_dollar2);
        Log.i(TAG,"new_save_euro："+new_euro2);
        Log.i(TAG,"new_save_won："+new_won2);

       setResult(2,intent);
        finish();//回到调用页面
    }
}
