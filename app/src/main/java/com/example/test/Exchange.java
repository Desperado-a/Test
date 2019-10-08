package com.example.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Exchange extends AppCompatActivity implements Runnable{
    private  final String TAG = "On_exchange";
    private float dollar_rate;
    private float euro_rate;
    private float won_rate;
    SharedPreferences data;
    static Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        data = getSharedPreferences("rate", Activity.MODE_PRIVATE);
        dollar_rate=data.getFloat("dollar_rate",0.1406f);
        euro_rate=data.getFloat("euro_rate",0.1278f);
        won_rate=data.getFloat("won_rate",172.378f);



        handler=new Handler(){
           public void handleMessage(Message msg) {
               if (msg.what == 1) {
                   String str = (String) msg.obj;
                   Log.i("Threat","get message:"+str);
               }
               super.handleMessage(msg);
           }
       };

        //开启子线程
        Thread t = new Thread(this);
        t.start();

    }

    public void  Ex_dollar(View v){
        EditText out =findViewById(R.id.money);
        TextView r =findViewById(R.id.money_out);
        String m= out.getText().toString();
        if(!(m.equals(""))){
            String d=String.valueOf(Float.parseFloat(m)*dollar_rate);
            r.setText(d+"$");
        }
    }
    public void  Ex_euro(View v){
        EditText out =findViewById(R.id.money);
        TextView r =findViewById(R.id.money_out);
        String m=  out.getText().toString();
        if(!(m.equals(""))) {
            String d = String.valueOf(Float.parseFloat(m) *euro_rate);
            r.setText(d+"€");
        }
    }
    public void  Ex_won(View v){
        EditText out =findViewById(R.id.money);
        TextView r =findViewById(R.id.money_out);
        String m=  out.getText().toString();
        if(!(m.equals(""))) {
            String d = String.valueOf(Float.parseFloat(m) * won_rate);
            r.setText(d+"₩");
        }
    }

    public void  Change(View v){
        openConfig();
    }
    public void  openConfig(){
        Intent to_conf = new Intent(this,Save.class);

        to_conf.putExtra("dollar_rate_key",dollar_rate);
        to_conf.putExtra("euro_rate_key",euro_rate);
        to_conf.putExtra("won_rate_key",won_rate);

        Log.i(TAG,"dollar_rate" + dollar_rate);
        Log.i(TAG,"euro_rate" + euro_rate);
        Log.i(TAG,"won_rate" + won_rate);

        startActivityForResult(to_conf,1);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }



   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==2){
           /*Bundle bd=data.getExtras();
            dollar_rate=bd.getFloat("new_dollar_rate",0.1406f);
            euro_rate=bd.getFloat("new_euro_rate",0.1278f);
            won_rate=bd.getFloat("new_won_rate",172.378f);*/

           SharedPreferences data2 = getSharedPreferences("rate", Activity.MODE_PRIVATE);
            dollar_rate=data2.getFloat("dollar_rate",0.0f);
            euro_rate=data2.getFloat("euro_rate",0.0f);
            won_rate=data2.getFloat("won_rate",0.0f);

            Log.i(TAG,"new_dollar_rate" + dollar_rate);
            Log.i(TAG,"new_euro_rate" + euro_rate);
            Log.i(TAG,"new_won_rate" + won_rate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /*@Override
    public void run() {
       //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);

            //获取message对象 用于返回主线程
            Message msg= handler.obtainMessage(1);
            msg.obj=html;
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public void run() {
        String url = "http://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        float dollar = 0,euro = 0,won = 0;
        try {
            doc = (Document) Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Elements tables = doc.getElementsByTagName("table");
        Element ts = tables.get(0);
        Elements td = ts.getElementsByTagName("td");
        for(int i= 0 ;i<td.size();i+=6){
            Element td1 = td.get(i);
            Element td2 = td.get(i+5);
            String name = td1.text();
            String rate = td2.text();
            switch(name) {
                case "美元" : dollar=Float.parseFloat(rate);break;
                case "欧元" : euro=Float.parseFloat(rate);break;
                case "韩元" : won=Float.parseFloat(rate);break;
                default : break;
            }
        }
        //System.out.println(ts);
    }

}
