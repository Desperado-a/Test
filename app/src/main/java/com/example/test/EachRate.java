package com.example.test;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EachRate extends AppCompatActivity {
    float rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_rate);
        String name=getIntent().getStringExtra("title");
        rate=getIntent().getFloatExtra("detail",0f);
        ((TextView)findViewById(R.id.chosen_rate)).setText(name);
        EditText input=((EditText)findViewById(R.id.calc));
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                TextView out =(TextView)EachRate.this.findViewById(R.id.out_print);
                if(editable.length()>0){
                    float val = Float.parseFloat(editable.toString());
                    out.setText(String.valueOf(rate*val));
                }
                else{
                    out.setText("");
                }
            }
        });
    }
}
