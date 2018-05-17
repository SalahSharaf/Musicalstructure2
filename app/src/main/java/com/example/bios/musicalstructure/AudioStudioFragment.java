package com.example.bios.musicalstructure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AudioStudioFragment extends AppCompatActivity {

    SeekBar seekbar;
    ImageButton playpause;
    ArrayList<Audio> listOfAudios;
    TextView textView;
    TextView textDuration;
    TextView textPosition;
    public static AudioStudioFragment getInstance() {
        return new AudioStudioFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiostudio_layout);
        listOfAudios = getIntent().getParcelableArrayListExtra("List");
        seekbar = findViewById(R.id.seekBar);
        seekbar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.DST_ATOP));
        seekbartask();
        ImageView imageView = findViewById(R.id.cover);
        imageView.setImageBitmap(SongsFragment.item.getAlbumArt());
        CircleImageView circleImageView = findViewById(R.id.circularCover);
        circleImageView.setImageBitmap(SongsFragment.item.getAlbumArt());
        playpause = findViewById(R.id.btnplay);
        if (SongsFragment.isPlaying) {
            playpause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
            playpause.setImageResource(R.drawable.ic_pause_black_24dp);
        }
        textView = findViewById(R.id.txtSongNameinStudio);
        textView.setText(listOfAudios.get(SongsFragment.number).getaName());
        textDuration=findViewById(R.id.txtDuration);
        textDuration.setText(getFormattedDuration(SongsFragment.item.getDuration()));
        textPosition=findViewById(R.id.txtPosition);
    }

    public void playPause(View view) {
        ImageButton btn = (ImageButton) view;
        if (SongsFragment.isPlaying) {
            SongsFragment.isPlaying = false;
            btn.setImageResource(R.drawable.ic_pause_black_24dp);
            SongsFragment.mediaPlayer.pause();
        } else {
            SongsFragment.isPlaying = true;
            btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            if (SongsFragment.mediaPlayer != null) {
                SongsFragment.mediaPlayer.seekTo(seekbar.getProgress());
                SongsFragment.mediaPlayer.start();
            } else {
                SongsFragment.mediaPlayer = MediaPlayer.create(this, Uri.parse(SongsFragment.item.getaPath()));
                seekbartask();
                SongsFragment.mediaPlayer.seekTo(seekbar.getProgress());
                SongsFragment.mediaPlayer.start();
            }
        }
    }

    public void seekbartask() {
        seekbar.setMax(SongsFragment.item.getDuration());
        if (SongsFragment.mediaPlayer != null) {
            seekbar.setProgress(SongsFragment.mediaPlayer.getCurrentPosition());
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        SongsFragment.mediaPlayer.seekTo(seekbar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    SongsFragment.mediaPlayer.seekTo(seekbar.getProgress());
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                seekbar.setProgress(SongsFragment.mediaPlayer.getCurrentPosition());
                                textPosition.setText(getFormattedDuration(SongsFragment.mediaPlayer.getCurrentPosition()));
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public void next(View view) {
        if (SongsFragment.number != listOfAudios.size() - 1) {
            SongsFragment.number += 1;
            SongsFragment.item = listOfAudios.get(SongsFragment.number);
            if (SongsFragment.isPlaying) {
                SongsFragment.mediaPlayer.release();
                SongsFragment.mediaPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(SongsFragment.item.getaPath()));
                SongsFragment.mediaPlayer.start();
                seekbartask();
            }
            textView.setText(SongsFragment.item.getaName());
        }
    }

    public void previous(View view) {
        if (SongsFragment.number != 0) {
            SongsFragment.number -= 1;
            SongsFragment.item = listOfAudios.get(SongsFragment.number);
            if (SongsFragment.isPlaying) {
                SongsFragment.mediaPlayer.release();
                SongsFragment.mediaPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(SongsFragment.item.getaPath()));
                SongsFragment.mediaPlayer.start();
                seekbartask();
            }
            textView.setText(SongsFragment.item.getaName());
        }
    }

    public void goback(View view) {
        finish();
    }
    public String getFormattedDuration(long duration) {
        String formate = String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        return formate;
    }

}