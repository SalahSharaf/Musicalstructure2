package com.example.bios.musicalstructure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class PlayListFragment extends Fragment {

    public static PlayListFragment getInstance() {
        return new PlayListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlistfragment_layout, null);
        ImageButton btn = view.findViewById(R.id.btnshowpopupmenu);
        ImageButton btn1 = view.findViewById(R.id.btnshowpopupmenu1);
        ImageButton btn2 = view.findViewById(R.id.btnshowpopupmenu2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getContext(), v);
                menu.getMenuInflater().inflate(R.menu.playlist_popupmenu, menu.getMenu());
                menu.show();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getContext(), v);
                menu.getMenuInflater().inflate(R.menu.playlist_popupmenu, menu.getMenu());
                menu.show();

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getContext(), v);
                menu.getMenuInflater().inflate(R.menu.playlist_popupmenu, menu.getMenu());
                menu.show();

            }
        });
        return view;
    }
}
