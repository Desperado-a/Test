package com.example.test;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyList extends ListActivity implements Runnable, AdapterView.OnItemClickListener {

    static Handler handler;
    ListView list;
    //private ArrayList<HashMap<String,String>> Listitems;
    private SimpleAdapter sadapter;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_my_list);
       // list = findViewById(R.id.mylist);

        Thread t = new Thread(this);
        t.start();

        /*handler= new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                   List<String> list2=( List<String> )msg.obj;
                   ListAdapter adapter = new ArrayAdapter<String>(MyList.this,android.R.layout.simple_list_item_1,list2);
                   list.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };*/
        handler= new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    ArrayList<HashMap<String,String>> list2= (ArrayList<HashMap<String, String>>) msg.obj;

                    /*Myadapter myAdapter = new Myadapter(MyList.this, R.layout.list_item,list2);
                    list.setAdapter(myAdapter);
                    Log.i("aaa","use myAdapter" );*/
                    Log.i("aaa","before simpleadapeter" );
                    sadapter = new SimpleAdapter(MyList.this,list2,R.layout.list_item,new String[]{"title","detail","date"},new int[]{R.id.item_title,R.id.item_detail,R.id.item_date});
                    setListAdapter(sadapter);
                    Log.i("aaa","use simpleadapeter" );
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
    }
    @Override
    public void run() {
        List<HashMap<String,String>> Listitems=new ArrayList<HashMap<String,String>>();;
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
                rate= String.valueOf(100f/Float.parseFloat(rate));

                Date now= Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
                String datestr = sd.format(now);

                HashMap<String,String> map =new  HashMap<String,String>();
                map.put("title",name);
                map.put("detail",rate);
                map.put("date",datestr);
                Listitems.add(map);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message msg= handler.obtainMessage(2);
        msg.obj=Listitems;
        handler.sendMessage(msg);
        Log.i("aaa","zi xiancheng over" );
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HashMap<String,String> map=(HashMap<String,String>)getListView().getItemAtPosition(i);
        String title = map.get("title");
        String detail = map.get("detail");
        Log.i("aaa","use map"+title );
        Log.i("aaa","use map"+detail );


       /*
        //通过View获取数据
        TextView title1=(TextView)view.findViewById(R.id.item_title);
        TextView detail1=(TextView)view.findViewById(R.id.item_detail);
        String title2=String.valueOf(title1.getText());
        String detail2=String.valueOf(detail1.getText());
        Log.i("aaa","use view"+title2 );
        Log.i("aaa","use view"+detail2 );*/

        Intent rate_one = new Intent(this,EachRate.class);
        rate_one.putExtra("title",title);
        rate_one.putExtra("detail",Float.parseFloat(detail));
        startActivity(rate_one);

    }
}
