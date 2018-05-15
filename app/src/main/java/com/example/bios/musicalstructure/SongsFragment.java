package com.example.bios.musicalstructure;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    ListView listView;
    TextView textView;
    ArrayList<Audio> listOfAudios;

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
        textView = view.findViewById(R.id.txtNumberOfSongs);
        listView = view.findViewById(R.id.listOfSongs);
        ReadStoragePermissionGranted();
        return view;
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
                //String albumartpath = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
                Long albumId = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                Bitmap p = BitmapFactory.decodeFile(albumArtUri.toString());
                audioModel.setAlbumArt(p);
                ////getting duration
                audioModel.setDuration(Long.valueOf(c.getString(4)));
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
                for (Audio audio : listOfAudios) {
                    Log.e("new Audio ", audio.getaName());
                }
                MyListViewAdapter listViewAdapter = new MyListViewAdapter(getContext(), listOfAudios);
                listView.setAdapter(listViewAdapter);
                textView.setText(String.valueOf(listOfAudios.size() + " Songs"));
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
                    for (Audio audio : listOfAudios) {
                        Log.e("new Audio ", audio.getaName() + " Songs");
                    }
                    MyListViewAdapter listViewAdapter = new MyListViewAdapter(getContext(), listOfAudios);
                    listView.setAdapter(listViewAdapter);
                    textView.setText(String.valueOf(listOfAudios.size()));
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
}
