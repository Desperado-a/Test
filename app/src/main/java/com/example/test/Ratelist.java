package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Ratelist extends ListActivity implements Runnable{
    //String data[]={"火锅","烤肉","拌饭"};
    //List<String> list1=new ArrayList<String>();
    static Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratelist);
       // ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
       // setListAdapter(adapter);

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
        String url = "http://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
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
                relist.add(name+":"+rate);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message msg= handler.obtainMessage(2);
        msg.obj=relist;
        handler.sendMessage(msg);
    }
}
