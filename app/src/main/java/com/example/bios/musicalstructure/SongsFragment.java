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

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    ListView listView;
    TextView numOfSongs;
    ArrayList<Audio> listOfAudios;
    public static int number = 0;
    public static Audio item;
    Bundle bundle;
    public static MediaPlayer mediaPlayer;
    public static boolean isPlaying;
    ImageButton btnplay;
    ImageButton btnprevious, btnnext;
     TextView artist;
     TextView songName;

    public static SongsFragment getInstance() {
        return new SongsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songfragment_layout, null);
        listView = view.findViewById(R.id.listOfSongs);
        numOfSongs = view.findViewById(R.id.txtnumberOfSongs);
        songName = view.findViewById(R.id.txtsongName);
        artist = view.findViewById(R.id.txtsongArtisit);
        if (savedInstanceState != null) {
            bundle = getFragmentManager().getFragment(savedInstanceState, "KEY").getArguments();
            number = bundle.getInt("position");
        }
         LinearLayout linearLayout = view.findViewById(R.id.namePattern);
         linearLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getContext(), AudioStudioFragment.class);
                 intent.putParcelableArrayListExtra("List",listOfAudios);
                 startActivity(intent);
             }
         });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", number);
        getFragmentManager().putFragment(outState, "KEY", this);
    }

}
