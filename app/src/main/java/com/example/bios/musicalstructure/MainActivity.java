package com.example.bios.musicalstructure;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    ViewPager viewpager;
    ImageView imgbackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = findViewById(R.id.btngenres);
        final Button btn2=findViewById(R.id.btnplaylist);
        final Button btn3=findViewById(R.id.btnsongs);
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
                    } catch (InterruptedException e) {}
                }
            }
        });
        checkbackground.start();
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
}
