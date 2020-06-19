package com.example.bios.musicalstructure;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MyListViewAdapter extends ArrayAdapter<Audio> {
    Context context;
    ArrayList<Audio> list;
    Thread thread;

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
        final Button btn = convertView.findViewById(R.id.name);
        btn.setTag(position);
        btn.setText(list.get(position).getaName());
        TextView txtDuration = convertView.findViewById(R.id.txtDuration);
        long duration = list.get(position).getDuration();
        txtDuration.setText(getFormattedDuration(duration));

        if (SongsFragment.mediaPlayer != null) {
            SongsFragment.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    SongsFragment.number += 1;
                    SongsFragment.workingAudio = list.get(SongsFragment.number);
                    SongsFragment.mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(SongsFragment.workingAudio.getaPath()));
                    SongsFragment.mediaPlayer.start();
                    btn.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                }
            });
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (SongsFragment.mediaPlayer != null) {
                    SongsFragment.mediaPlayer.release();
                    SongsFragment.mediaPlayer = null;
                }
                SongsFragment.number = (Integer) v.getTag();
                SongsFragment.workingAudio = list.get((Integer) v.getTag());
                SongsFragment.isPlaying = true;
                SongsFragment.mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(SongsFragment.workingAudio.getaPath()));
                SongsFragment.mediaPlayer.start();
                final Button btn = (Button) v;
                btn.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                SongsFragment.songName.setText(list.get((Integer) v.getTag()).getaName());
                SongsFragment.artist.setText(list.get((Integer) v.getTag()).getaArtist());
                SongsFragment.btnplay.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (SongsFragment.workingAudio == list.get((Integer) v.getTag())) {
                            btn.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                        } else {
                            btn.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

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
