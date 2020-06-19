package com.example.bios.musicalstructure;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {

    ListView listView;
    TextView numOfSongs;
    List<Audio> listOfAudios;
    public static int number = 0;
    public static Audio workingAudio;
    Bundle bundle;
    public static MediaPlayer mediaPlayer;
    public static boolean isPlaying;
    public static ImageButton btnprevious, btnnext, btnplay;
    public static TextView artist;
    public static TextView songName;

    public static SongsFragment getInstance() {
        return new SongsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfAudios = MainActivity.audios;
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                }
            }
        }).start();*/
/*
        if (SongsFragment.mediaPlayer != null) {

        }
*/

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songfragment_layout, null);
        listView = view.findViewById(R.id.listOfSongs);
        numOfSongs = view.findViewById(R.id.txtnumberOfSongs);
        songName = view.findViewById(R.id.txtsongName);
        artist = view.findViewById(R.id.txtsongArtisit);
        listView = view.findViewById(R.id.listOfSongs);
        btnplay = view.findViewById(R.id.btnsongplay);
        btnnext = view.findViewById(R.id.btnsongNext);
        btnprevious = view.findViewById(R.id.btnsongPrevious);
        if (workingAudio != null) {
            songName.setText(workingAudio.getaName());
            artist.setText(workingAudio.getaArtist());
        }
        listView.setAdapter(new MyListViewAdapter(getContext(), (ArrayList<Audio>) listOfAudios));
        if (savedInstanceState != null) {
            bundle = getFragmentManager().getFragment(savedInstanceState, "KEY").getArguments();
            number = bundle.getInt("position");
        }
        LinearLayout linearLayout = view.findViewById(R.id.namePattern);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AudioStudioFragment.class);
                intent.putParcelableArrayListExtra("List", (ArrayList<? extends Parcelable>) listOfAudios);
                startActivity(intent);
            }
        });
        setOnCLickListiners();
        return view;
    }

    private void setOnCLickListiners() {
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = (ImageButton) view;
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    ///it is now playing
                    mediaPlayer.pause();
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                } else if (mediaPlayer == null) {
                    //t is now playing working
                    workingAudio = listOfAudios.get(0);
                    mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(workingAudio.getaPath()));
                    mediaPlayer.start();
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                } else if (mediaPlayer != null && mediaPlayer.isPlaying() == false) {
                    // it is now stopped
                    mediaPlayer.start();
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                }
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = (ImageButton) view;
                if (number < listOfAudios.size() && mediaPlayer != null) {
                    number += 1;
                    workingAudio = listOfAudios.get(number);
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(workingAudio.getaPath()));
                    mediaPlayer.start();
                    btnplay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    songName.setText(workingAudio.getaName());
                    artist.setText(workingAudio.getaArtist());
                }
            }
        });
        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = (ImageButton) view;
                if (number != 0 && mediaPlayer != null) {
                    number -= 1;
                    workingAudio = listOfAudios.get(number);
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(workingAudio.getaPath()));
                    mediaPlayer.start();
                    btnplay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    songName.setText(workingAudio.getaName());
                    artist.setText(workingAudio.getaArtist());
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", number);
        getFragmentManager().putFragment(outState, "KEY", this);
    }

}
