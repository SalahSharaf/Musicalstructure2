package com.example.bios.musicalstructure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_layout, parent, false);
        }
        Button btn = convertView.findViewById(R.id.name);
        btn.setText(list.get(position).getaName());
        TextView imageView = convertView.findViewById(R.id.imageCover);
        long duration = list.get(position).getDuration();
        imageView.setText(getFormattedDuration(duration));
        return convertView;
    }

    public String getFormattedDuration(long duration) {
        String formate = String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        return formate;
    }

}
