package com.example.musicmedia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.musicmedia.R;
import com.example.musicmedia.viewmodel.ButtonPlayViewModel;
import com.example.musicmedia.viewmodel.DataViewModel;
import com.example.musicmedia.viewmodel.DataViewModelOnline;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentPlayerInfo extends Fragment {
    View view;

    CircleImageView circleImageView;
    TextView txtPlayerSong, txtPlayerSinger;
    DataViewModel viewModel;
    DataViewModelOnline viewModelOnline;
    ButtonPlayViewModel buttonPlayViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_info, container, false);
        circleImageView = view.findViewById(R.id.imgView);
        txtPlayerSong = view.findViewById(R.id.txtPlayerSong);
        txtPlayerSinger = view.findViewById(R.id.txtPlayerSinger);

        // nhận dữ liệu và set dữ liệu offline
        viewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), song -> {
            txtPlayerSong.setText(song.getName());
            txtPlayerSinger.setText(song.getSinger());
        });

        //nhận dữ liệu online và set dữ liệu
        viewModelOnline = new ViewModelProvider(getActivity()).get(DataViewModelOnline.class);
        viewModelOnline.getSelected().observe(getViewLifecycleOwner(), songOnline -> {
            txtPlayerSong.setText(songOnline.getName());
            txtPlayerSinger.setText(songOnline.getArtist());
            Picasso.with(circleImageView.getContext()).load(songOnline.getImage()).into(circleImageView);
        });

        // xử lí nút play pause
        buttonPlayViewModel = new ViewModelProvider(getParentFragment()).get(ButtonPlayViewModel.class);
        buttonPlayViewModel.getSelected().observe(getViewLifecycleOwner(), item -> {
            if (item == 0) {
                stopAnimation();
            }
            else {
                startAnimation();
            }
        });



        return view;
    }

    //twins Circle
    public void startAnimation() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                circleImageView.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        circleImageView.animate().rotationBy(360).withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    public void stopAnimation() {
        circleImageView.animate().cancel();
    }
}
