package com.example.bios.musicalstructure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class MyListViewAdapter extends ArrayAdapter<Audio> {
    Context context;
    ArrayList<Audio> list;

    public MyListViewAdapter(Context context, ArrayList<Audio> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_layout, parent,false);
        }
        Button textView = convertView.findViewById(R.id.name);
        textView.setText(list.get(position).getaName());
        return convertView;
    }
}
