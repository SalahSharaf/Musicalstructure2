package com.example.bios.musicalstructure;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {

    public static ListView listView;
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
    public static MyListViewAdapter adapter;

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
        if (listOfAudios != null) {
            adapter = new MyListViewAdapter(getContext(), (ArrayList<Audio>) listOfAudios);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

/*
        if (savedInstanceState != null) {
            bundle = getFragmentManager().getFragment(savedInstanceState, "KEY").getArguments();
            number = bundle.getInt("position");
        }
*/
        LinearLayout linearLayout = view.findViewById(R.id.namePattern);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {

                    Intent intent = new Intent(getContext(), AudioStudioActivity.class);
                    intent.putParcelableArrayListExtra("List", (ArrayList<? extends Parcelable>) listOfAudios);
                    startActivity(intent);
                }
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
                } else if (mediaPlayer == null && listOfAudios != null) {
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
                if (listOfAudios != null && number < listOfAudios.size() && mediaPlayer != null) {
                    number += 1;
                    workingAudio = listOfAudios.get(number);
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(workingAudio.getaPath()));
                    mediaPlayer.start();
                    btnplay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    songName.setText(workingAudio.getaName());
                    artist.setText(workingAudio.getaArtist());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = (ImageButton) view;
                if (listOfAudios != null && number != 0 && mediaPlayer != null) {
                    number -= 1;
                    workingAudio = listOfAudios.get(number);
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(workingAudio.getaPath()));
                    mediaPlayer.start();
                    btnplay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    songName.setText(workingAudio.getaName());
                    artist.setText(workingAudio.getaArtist());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        listOfAudios = MainActivity.audios;
        listView.setAdapter(new MyListViewAdapter(getContext(), (ArrayList<Audio>) listOfAudios));
        if (SongsFragment.mediaPlayer != null && SongsFragment.mediaPlayer.isPlaying()) {
            btnplay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
        } else {
            btnplay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", number);
        getFragmentManager().putFragment(outState, "KEY", this);
    }

}
