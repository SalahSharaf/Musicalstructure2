package com.example.bios.musicalstructure;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AudioStudioActivity extends AppCompatActivity {

    SeekBar seekbar;
    ImageButton playpause;
    ArrayList<Audio> listOfAudios;
    TextView name;
    TextView textDuration;
    TextView textPosition;
    CircleImageView circleImageView;
    ImageView imageView;
    Thread checkbackground;
    final public Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    Uri uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiostudio_layout);
        listOfAudios = (ArrayList<Audio>) MainActivity.audios;
        playpause = findViewById(R.id.btnplay);
        seekbar = findViewById(R.id.seekBar);
        circleImageView = findViewById(R.id.circularCover);
        imageView = findViewById(R.id.cover);
        textPosition = findViewById(R.id.txtPosition);
        textDuration = findViewById(R.id.txtDuration);
        name = findViewById(R.id.txtSongNameinStudio);
        name.setText(SongsFragment.workingAudio.getaName());
        textPosition.setText(getFormattedDuration(SongsFragment.mediaPlayer.getCurrentPosition()));
        textDuration.setText(getFormattedDuration(SongsFragment.mediaPlayer.getDuration()));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    SongsFragment.mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        checkbackground = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (SongsFragment.mediaPlayer != null && SongsFragment.mediaPlayer.getCurrentPosition() < SongsFragment.mediaPlayer.getDuration()) {
                        seekbar.setMax(SongsFragment.mediaPlayer.getDuration());
                        seekbar.setProgress(SongsFragment.mediaPlayer.getCurrentPosition());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textPosition.setText(getFormattedDuration(SongsFragment.mediaPlayer.getCurrentPosition()));
                                textDuration.setText(getFormattedDuration(SongsFragment.mediaPlayer.getDuration()));
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IllegalStateException exception) {
                }
            }
        });
        checkbackground.start();
        SongsFragment.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                SongsFragment.number += 1;
                SongsFragment.workingAudio = MainActivity.audios.get(SongsFragment.number);
                SongsFragment.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(SongsFragment.workingAudio.getaPath()));
                SongsFragment.mediaPlayer.start();
                SongsFragment.listView.setAdapter(SongsFragment.adapter);
                SongsFragment.adapter.notifyDataSetChanged();
                SongsFragment.songName.setText(SongsFragment.workingAudio.getaName());
                SongsFragment.artist.setText(SongsFragment.workingAudio.getaArtist());
                name.setText(SongsFragment.workingAudio.getaName());
                uri = ContentUris.withAppendedId(sArtworkUri, MainActivity.audios.get(SongsFragment.number).getAlbumID());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                    circleImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkbackground = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (SongsFragment.mediaPlayer != null && SongsFragment.mediaPlayer.getCurrentPosition() < SongsFragment.mediaPlayer.getDuration()) {
                            seekbar.setMax(SongsFragment.mediaPlayer.getDuration());
                            seekbar.setProgress(SongsFragment.mediaPlayer.getCurrentPosition());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textPosition.setText(getFormattedDuration(SongsFragment.mediaPlayer.getCurrentPosition()));
                                    textDuration.setText(getFormattedDuration(SongsFragment.mediaPlayer.getDuration()));
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                checkbackground.start();
            }
        });
        uri = ContentUris.withAppendedId(sArtworkUri, MainActivity.audios.get(SongsFragment.number).getAlbumID());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
            circleImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (SongsFragment.mediaPlayer != null && SongsFragment.mediaPlayer.isPlaying()) {
            playpause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
        } else {
            playpause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkbackground = new Thread(new Runnable() {
            @Override
            public void run() {
                while (SongsFragment.mediaPlayer != null && SongsFragment.mediaPlayer.getCurrentPosition() < SongsFragment.mediaPlayer.getDuration()) {
                    seekbar.setMax(SongsFragment.mediaPlayer.getDuration());
                    seekbar.setProgress(SongsFragment.mediaPlayer.getCurrentPosition());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textPosition.setText(getFormattedDuration(SongsFragment.mediaPlayer.getCurrentPosition()));
                            textDuration.setText(getFormattedDuration(SongsFragment.mediaPlayer.getDuration()));
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        checkbackground.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkbackground.interrupt();
    }

    public void playPause(View view) {
        if (SongsFragment.mediaPlayer != null && SongsFragment.mediaPlayer.isPlaying()) {
            SongsFragment.mediaPlayer.pause();
            playpause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        } else {
            SongsFragment.mediaPlayer.start();
            playpause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
        }
    }

    public void previous(View view) {
        if (SongsFragment.number != 0 && SongsFragment.mediaPlayer != null) {
            SongsFragment.mediaPlayer.release();
            SongsFragment.number -= 1;
            SongsFragment.workingAudio = MainActivity.audios.get(SongsFragment.number);
            SongsFragment.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(SongsFragment.workingAudio.getaPath()));
            SongsFragment.mediaPlayer.start();
            //SongsFragment.adapter.notifyDataSetChanged();
            SongsFragment.songName.setText(SongsFragment.workingAudio.getaName());
            SongsFragment.artist.setText(SongsFragment.workingAudio.getaArtist());
            name.setText(SongsFragment.workingAudio.getaName());
        }
    }

    public void next(View view) {
        if (SongsFragment.number < MainActivity.audios.size() && SongsFragment.mediaPlayer != null) {
            SongsFragment.mediaPlayer.release();
            SongsFragment.number += 1;
            SongsFragment.workingAudio = MainActivity.audios.get(SongsFragment.number);
            SongsFragment.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(SongsFragment.workingAudio.getaPath()));
            SongsFragment.mediaPlayer.start();
           // SongsFragment.adapter.notifyDataSetChanged();
            SongsFragment.songName.setText(SongsFragment.workingAudio.getaName());
            SongsFragment.artist.setText(SongsFragment.workingAudio.getaArtist());
            name.setText(SongsFragment.workingAudio.getaName());
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