package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Myadapter extends ArrayAdapter {
    private static final String TAG = "MyAdapter";

    public Myadapter(Context context, int resource, ArrayList<HashMap<String, String>> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            //转化布局文件
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Map<String, String> map = (Map<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.item_title);
        TextView detail = (TextView) itemView.findViewById(R.id.item_detail);
        TextView date = (TextView) itemView.findViewById(R.id.item_date);
        title.setText(map.get("title"));
        detail.setText(map.get("detail"));
        date.setText(map.get("date"));
        return itemView;
    }
}