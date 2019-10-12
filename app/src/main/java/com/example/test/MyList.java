package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyList extends AppCompatActivity implements Runnable{

    static Handler handler;
    ListView list;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        list = findViewById(R.id.mylist);
        String data[]={"111","222","333"};
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        list.setAdapter(adapter);

        Thread t = new Thread(this);
        t.start();

        handler= new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    List<String> list2=( List<String> )msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(MyList.this,android.R.layout.simple_list_item_1,list2);
                    list.setAdapter(adapter);
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
