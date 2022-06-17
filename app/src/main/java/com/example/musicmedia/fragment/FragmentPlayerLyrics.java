package com.example.musicmedia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicmedia.R;
import com.example.musicmedia.viewmodel.DataViewModelOnline;

public class FragmentPlayerLyrics extends Fragment {

    DataViewModelOnline viewModelOnline;
    TextView txtLyrics;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_lyrics, container, false);
        txtLyrics = view.findViewById(R.id.txt_song_lyrics);

        //lấy lyrics và set
        viewModelOnline = new ViewModelProvider(getActivity()).get(DataViewModelOnline.class);
        viewModelOnline.getSelected().observe(getViewLifecycleOwner(), songOnline -> {
            txtLyrics.setText(songOnline.getLyrics());
        });
        return view;
    }
}
