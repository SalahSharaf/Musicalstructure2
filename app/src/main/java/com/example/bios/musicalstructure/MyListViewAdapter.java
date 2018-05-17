package com.example.bios.musicalstructure;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                }
            }
        }).start();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_layout, parent, false);
        }
        Button btn = convertView.findViewById(R.id.name);
        btn.setTag(position);
        btn.setText(list.get(position).getaName());
        TextView imageView = convertView.findViewById(R.id.imageCover);
        long duration = list.get(position).getDuration();
        imageView.setText(getFormattedDuration(duration));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SongsFragment.mediaPlayer != null) {
                    SongsFragment.mediaPlayer.release();
                    SongsFragment.mediaPlayer = null;
                }
                SongsFragment.number = (Integer) v.getTag();
                SongsFragment.item = list.get((Integer) v.getTag());
                SongsFragment.isPlaying = true;
                SongsFragment.mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(SongsFragment.item.getaPath()));
                SongsFragment.mediaPlayer.start();
                Button btn = (Button) v;
                btn.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            }
        });
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
