package com.example.bios.musicalstructure;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewpager;
    ImageView imgbackground;
    public static List<Audio> audios;
    final int READ_STORAGE = 7;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = findViewById(R.id.btngenres);
        final Button btn2 = findViewById(R.id.btnplaylist);
        final Button btn3 = findViewById(R.id.btnsongs);
        btn.setActivated(true);
        btn.setSelected(true);
        viewpager = findViewById(R.id.viewpager);
        MyFragmentPagerAdapter fg = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(fg);
        imgbackground = findViewById(R.id.imgbackground);
        Thread checkbackground = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    switch (viewpager.getCurrentItem()) {
                        case 0:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgbackground.setImageResource(R.drawable.background1);
                                    btn.setActivated(true);
                                }
                            });
                            break;
                        case 1:

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgbackground.setImageResource(R.drawable.background2);
                                    btn2.setActivated(true);
                                }
                            });
                            break;
                        case 2:

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgbackground.setImageResource(R.drawable.background3);
                                    btn3.setActivated(true);
                                }
                            });
                            break;
                        case 3:

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgbackground.setImageResource(R.drawable.background4);
                                }
                            });
                            break;
                        case 4:

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgbackground.setImageResource(R.drawable.background5);
                                }
                            });
                            break;
                        case 5:

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgbackground.setImageResource(R.drawable.background6);
                                }
                            });
                            break;
                        default:
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        checkbackground.start();
        accessingMediaData();
    }

    public void genresLayout(View view) {
        viewpager.setCurrentItem(0);
    }

    public void PlaylistLayout(View view) {
        viewpager.setCurrentItem(1);
    }

    public void SongsLayout(View view) {
        viewpager.setCurrentItem(2);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void accessingMediaData() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                if (!MainActivity.this.isFinishing()) {
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();
                }

            } else {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 7);
            }
        } else {
            getAllMediaMp3Files();
        }

    }

    private void getAllMediaMp3Files() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MainActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG).show();
        } else if (cursor != null && cursor.moveToNext()) {

            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int date = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
            int track = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int isMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);
            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumArt = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            audios = new ArrayList<>();
            do {
                File fileA = new File(cursor.getString(data));
                String artistA = cursor.getString(artist);
                String albumA = cursor.getString(album);
                String trackA = cursor.getString(track);
                String titleA = cursor.getString(title);
                Long durationA = cursor.getLong(duration);
                int isMusicA = cursor.getInt(isMusic);
                int dateA = cursor.getInt(date);
                String albumArtString = "";
                Bitmap albumArtBitmap;
                try {
                    albumArtString = cursor.getString(albumArt);
                } catch (Exception e) {

                }
                albumArtBitmap = stringToBitMap(albumArtString);
                if (isMusicA != 0) {
                    audios.add(new Audio(fileA.getPath(), titleA, albumA, artistA, albumArtBitmap, durationA));
                }

            } while (cursor.moveToNext());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                    getAllMediaMp3Files();
                } else {
                    if (!MainActivity.this.isFinishing()) {
                        AlertDialog.Builder alert_builder = new AlertDialog.Builder(getApplicationContext());
                        alert_builder.setMessage("External Storage Permission is Required.\nto access all media files music and videos");
                        alert_builder.setTitle("Please Grant Permission.");
                        alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                            }
                        });
                        alert_builder.setNeutralButton("Cancel", null);
                        AlertDialog dialog = alert_builder.create();
                        dialog.show();

                    }
                }
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    accessingMediaData();
                }
                break;
        }
    }

    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
