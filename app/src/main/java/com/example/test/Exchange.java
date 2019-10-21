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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Exchange extends AppCompatActivity implements Runnable{
    private  final String TAG = "On_exchange";
    private float dollar_rate;
    private float euro_rate;
    private float won_rate;
    private String update;
    private String datestr;
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
        update=data.getString("update","");

        //获取当前时间
        Date now=Calendar.getInstance().getTime();
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        datestr = sd.format(now);

        if(!datestr.equals(update)){
            Thread t = new Thread(this);
            t.start();
            Log.i(TAG,"需要更新" );
        }
        else Log.i(TAG,"不需要更新" );

        handler=new Handler(){
           public void handleMessage(Message msg) {
               if (msg.what == 1) {
                   float rate[] = (float[]) msg.obj;
                   Log.i("Threat","get message:"+rate);
                   dollar_rate=rate[0];
                   euro_rate=rate[1];
                   won_rate=rate[2];
                   Log.i(TAG,"zhu xian cheng" + dollar_rate);


                   //保存汇率及更新日期
                   SharedPreferences share = getSharedPreferences("rate", Activity.MODE_PRIVATE);
                   SharedPreferences.Editor editor=  share.edit();
                   editor.putFloat("dollar_rate",dollar_rate);
                   editor.putFloat("euro_rate",euro_rate);
                   editor.putFloat("won_rate",won_rate);
                   editor.putString("update",datestr);
                   editor.apply();

               }
               super.handleMessage(msg);
           }
       };

        //开启子线程
        //Thread t = new Thread(this);
        //t.start();
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
    public void  GetFromInter(View v){
        Thread t = new Thread(this);
        t.start();
        openConfig();
    }

    public void  openlist(View v){
       //Intent to_rate = new Intent(this,Ratelist.class);  //简单布局
        Intent to_rate = new Intent(this,MyList.class);  //自定义
        startActivity(to_rate);
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
         // openConfig();
            Log.i(TAG,"onOptionsItemSelected" );
           Intent to_rate = new Intent(this,Ratelist.class);  //简单布局
            startActivity(to_rate);


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



   /* @Override
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

    @Override
    public void run() {
        String url = "http://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        float d = 0,e1 = 0,w= 0;
        float[] rate3 = new float[3];
        try {
            doc = Jsoup.connect(url).get();
            Elements tables  = doc.getElementsByTag("table");
            Element ts = tables.get(0);
            Elements td = ts.getElementsByTag("td");

             for(int i= 0 ;i<td.size();i+=6){
                 Element td1 = td.get(i);
                 Element td2 = td.get(i+5);
                 String name = td1.text();
                 String rate = td2.text();
                switch(name) {
                   case "美元" : d=100f/Float.parseFloat(rate);break;
                   case "欧元" : e1=100f/Float.parseFloat(rate);break;
                  case "韩元" : w=100f/Float.parseFloat(rate);break;
                   default : break;
                }
             }
             rate3[0]=d;
             rate3[1]=e1;
             rate3[2]=w;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Message msg= handler.obtainMessage(1);
        msg.obj=rate3;
        handler.sendMessage(msg);
        Log.i(TAG,"zi xian cheng" + dollar_rate);

        //System.out.println(ts);
    }

}
