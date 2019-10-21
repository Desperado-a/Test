package com.example.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//这个activity没啥用！！！！！！！！！
public class Ratelist extends ListActivity implements Runnable{
    static Handler handler;
    private String logdate="";
    ListView list;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ratelist);

        Log.i("on","ON RateList");
        //获取上次更新时间
        SharedPreferences sp = getSharedPreferences("rate", Activity.MODE_PRIVATE);
        logdate=sp.getString("update","");
        Log.i("on","上次更新时间："+logdate);

        Thread t = new Thread(this);
        t.start();

        handler= new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    List<String> list2=( List<String> )msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(Ratelist.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        List<String> relist=new ArrayList<String>();

        //获取当前时间
        Date now= Calendar.getInstance().getTime();
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        String datestr = sd.format(now);

        if(datestr.equals(logdate)){
            Log.i("on","时间相等！！不需要从网上更新");
            RateManager manager=new RateManager(this);
            for(RateItem i:manager.listAll()){
                relist.add(i.getCurName() + ":" + i.getCurRate());
            }
        }
        else {
            Log.i("on","时间不等 需要从网上更新");
            String url = "http://www.usd-cny.com/bankofchina.htm";
            Document doc = null;
            try {
                List<RateItem> db=new ArrayList<RateItem>();
                doc = Jsoup.connect(url).get();
                Elements tables = doc.getElementsByTag("table");
                Element ts = tables.get(0);
                Elements td = ts.getElementsByTag("td");

                for (int i = 0; i < td.size(); i += 6) {
                    Element td1 = td.get(i);
                    Element td2 = td.get(i + 5);
                    String name = td1.text();
                    String rate = td2.text();
                    relist.add(name + ":" + rate);
                    db.add(new RateItem(name,rate));
                }
                RateManager manager = new RateManager(this);
                manager.deleteAll();
                manager.addAll(db);

                SharedPreferences data = getSharedPreferences("rate", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=  data.edit();
                editor.putString("update",datestr);
                editor.apply();
                Log.i("on","更新日期结束");


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Message msg= handler.obtainMessage(2);
        msg.obj=relist;
        handler.sendMessage(msg);
    }
}
