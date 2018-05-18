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
        ReadStoragePermissionGranted();
        final TextView songName = view.findViewById(R.id.txtsongName);
        final TextView artist = view.findViewById(R.id.txtsongArtisit);
        if (savedInstanceState != null) {
            bundle = getFragmentManager().getFragment(savedInstanceState, "KEY").getArguments();
            number = bundle.getInt("position");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    item = listOfAudios.get(number);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            songName.setText(item.getaName());
                            artist.setText(item.getaArtist());
                            if (isPlaying) {
                                btnplay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        number += 1;
                                        item = listOfAudios.get(number);
                                        mediaPlayer.release();
                                        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(item.getaPath()));
                                        mediaPlayer.start();
                                    }
                                });
                            } else {
                                btnplay.setImageResource(R.drawable.ic_pause_black_24dp);
                            }
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (isPlaying) {

                    }
                }
            }
        }).start();
        PlayMusicFunction(view);
        checkCurrentSongColor();
        btnpreviousEvent(view);
        btnnextEvent(view);
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

    private void btnnextEvent(View view) {
        btnprevious = view.findViewById(R.id.btnsongPrevious);
        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number != listOfAudios.size() - 1)
                    number += 1;
                item = listOfAudios.get(number);
                if (isPlaying) {
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(item.getaPath()));
                    mediaPlayer.start();
                }
            }
        });
    }

    private void btnpreviousEvent(View view) {
        btnnext = view.findViewById(R.id.btnsongNext);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number != 0) {
                    number -= 1;
                    item = listOfAudios.get(number);
                    if (isPlaying) {
                        mediaPlayer.release();
                        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(item.getaPath()));
                        mediaPlayer.start();
                    }
                }
            }
        });
    }

    private void checkCurrentSongColor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < listView.getCount(); i++) {
                        try {
                            View view = getViewByPosition(i, listView);
                            final Button btn = (Button) ((ViewGroup) view).getChildAt(0);
                            if (i != number) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn.setTextColor(getResources().getColor(R.color.colorPrimaryLight));

                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    }
                                });
                            }
                        } catch (NullPointerException ex) {
                            System.out.println("Error");
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void PlayMusicFunction(View view) {
        btnplay = view.findViewById(R.id.btnsongplay);
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPlaying) {
                    isPlaying = true;
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(item.getaPath()));
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                        mediaPlayer.start();
                    }
                } else if (isPlaying) {
                    isPlaying = false;
                    mediaPlayer.pause();
                    // here i pause the media player
                }
            }
        });

    }

    public ArrayList<Audio> getAllAudioFromDevice(Context context) {

        ArrayList<Audio> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.Albums.ALBUM_ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.TITLE};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {

                Audio audioModel = new Audio();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);
                try {
                    String albumartpath = c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    Bitmap p = BitmapFactory.decodeFile(albumartpath);
                    audioModel.setAlbumArt(p);
                }catch (IllegalArgumentException ex){}
                //Long albumId = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
               // Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
               // Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                ////getting duration
                audioModel.setDuration(Integer.valueOf(c.getString(4)));
                ///getting name
                audioModel.setaName(c.getString(5));
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                tempAudioList.add(audioModel);
            }
            c.close();
        }
        return tempAudioList;
    }

    public void ReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                listOfAudios = getAllAudioFromDevice(getContext());
                MyListViewAdapter listViewAdapter = new MyListViewAdapter(getContext(), listOfAudios);
                listView.setAdapter(listViewAdapter);
                numOfSongs.setText(listOfAudios.size() + " Songs");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 3: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listOfAudios = getAllAudioFromDevice(getContext());
                    MyListViewAdapter listViewAdapter = new MyListViewAdapter(getContext(), listOfAudios);
                    listView.setAdapter(listViewAdapter);
                    numOfSongs.setText(listOfAudios.size() + " Songs");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("permissions hasn't been granted");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ReadStoragePermissionGranted();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", number);
        getFragmentManager().putFragment(outState, "KEY", this);
    }

}
