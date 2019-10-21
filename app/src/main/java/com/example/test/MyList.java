package com.example.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class MyList extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener ,AdapterView.OnItemLongClickListener{

    static Handler handler;
    ListView list;
    private String logdate="";
    private ArrayList<HashMap<String,String>> Listitems;
    private SimpleAdapter sadapter;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_my_list);
       list = findViewById(R.id.mylist);

        //获取上次更新时间
        SharedPreferences sp = getSharedPreferences("rate", Activity.MODE_PRIVATE);
        logdate=sp.getString("update","");
        Log.i("aaa","上次更新时间："+logdate);

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
                    sadapter = new SimpleAdapter(MyList.this,list2,R.layout.list_item,
                            new String[]{"title","detail","date"},
                            new int[]{R.id.item_title,R.id.item_detail,R.id.item_date});
                    list.setAdapter(sadapter);
                    Log.i("aaa","use simpleadapeter" );
                }
                super.handleMessage(msg);
            }
        };
        list.setOnItemClickListener(this);
        list.setEmptyView(findViewById(R.id.nodata));
        list.setOnItemLongClickListener(this);
    }
    @Override
    public void run() {
        Log.i("aaa","子线程：");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Listitems=new ArrayList<HashMap<String,String>>();

        //获取当前时间
        Date now= Calendar.getInstance().getTime();
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        String datestr = sd.format(now);

        if(datestr.equals(logdate)){
            Log.i("aaa","时间相等！！不需要从网上更新");
            RateManager manager=new RateManager(this);
            for(RateItem i:manager.listAll()){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", i.getCurName());
                map.put("detail", i.getCurRate());
                map.put("date", datestr);
                Listitems.add(map);
            }
        }
        else {
            Log.i("aaa","时间不等 需要从网上更新");
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

                    rate = String.valueOf(100f / Float.parseFloat(rate));

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("title", name);
                    map.put("detail", rate);

                    // Date now= Calendar.getInstance().getTime();
                    //@SuppressLint("SimpleDateFormat") SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
                    //String datestr = sd.format(now);
                    map.put("date", datestr);
                    Listitems.add(map); //添加对象
                    db.add(new RateItem(name,rate));
                }
                RateManager manager = new RateManager(this);
                manager.deleteAll();
                manager.addAll(db);

                SharedPreferences data = getSharedPreferences("rate", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=  data.edit();
                editor.putString("update",datestr);
                editor.apply();
                Log.i("aaa","更新日期结束");

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Message msg= handler.obtainMessage(2);
        msg.obj=Listitems;
        handler.sendMessage(msg);
        Log.i("aaa","zi xiancheng over" );
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        HashMap<String,String> map=(HashMap<String,String>)list.getItemAtPosition(i);
        String title = map.get("title");
        final String detail = map.get("detail");
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
        //startActivity(rate_one);

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("'"+title+"'"+"汇率计算");
        final View v = View.inflate(getApplication(),R.layout.activity_each_rate,null);
        builder.setView(v).setPositiveButton("确定", null) ;//获取数据
        Log.i("aaa","1" );

        ((TextView)v.findViewById(R.id.chosen_rate)).setText(title);
        EditText input=v.findViewById(R.id.calc);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                TextView out =v.findViewById(R.id.out_print);
                if(editable.length()>0){
                    float val = Float.parseFloat(editable.toString());
                    out.setText(String.valueOf(  Float.parseFloat(detail)*val));   //计算汇率
                }
                else{
                    out.setText("");
                }
            }
        });

        builder.create().show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        Log.i("aaa","onItemLongClick" );
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("删除数据").setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        HashMap<String,String> map=(HashMap<String,String>)list.getItemAtPosition(i);
                        Listitems.remove(map);
                        Log.i("aaa","1" );
                        sadapter.notifyDataSetChanged();
                        Log.i("aaa","2" );
                    }
                }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}
