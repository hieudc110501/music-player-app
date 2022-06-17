package com.example.musicmedia.fragment;

import static com.example.musicmedia.R.drawable.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicmedia.R;
import com.example.musicmedia.adapter.PlayerViewPagerAdapter;
import com.example.musicmedia.viewmodel.ButtonPlayViewModel;
import com.example.musicmedia.viewmodel.DataViewModelOnline;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import me.relex.circleindicator.CircleIndicator3;

public class FragmentPlayer extends Fragment {
    View view;


    ImageView btnPlay, btnNext, btnPrevious, btnBack, btnFavorite, btnRepeat;
    TextView txtSongCurrent, txtSongTotal;
    SeekBar playSeekBar;
    PlayerViewPagerAdapter playerViewPagerAdapter;
    ViewPager2 playerViewPager;
    CircleIndicator3 circleIndicator3;
    ButtonPlayViewModel buttonPlayViewModel;
    BarVisualizer barVisualizer;
    DataViewModelOnline dataViewModelOnline;

    BroadcastReceiver broadcastReceiver;

    private Handler handler = new Handler();

    public static final String TAG = FragmentPlayer.class.getName();
    String songName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    boolean check = false;
    boolean isPlaying;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        Init();
        PlayerViewPager();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
//                songOnline = (SongOnline) bundle.get("object_song");
//                isPlaying = bundle.getBoolean("isPlaying");
//                pos = bundle.getInt("pos");
                isPlaying = bundle.getBoolean("isPlaying",false);

                if (isPlaying){
                    btnPlay.setImageResource(ic_pause);
                }
                int action = bundle.getInt("action");

                handleLayoutMusic(action);
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_fragment_player"));

        //View
        buttonPlayViewModel = new ViewModelProvider(this).get(ButtonPlayViewModel.class);

        //nhận dữ liệu btnFavorite và set, update
        dataViewModelOnline = new ViewModelProvider(getActivity()).get(DataViewModelOnline.class);
        dataViewModelOnline.getSelected().observe(getViewLifecycleOwner(), songOnline -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference favorite = db.
                    collection("MySong").
                    document(songOnline.getID());
            if (songOnline.getFavorite()) {
                btnFavorite.setImageResource(R.drawable.ic_favorite2);
            }
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (songOnline.getFavorite()) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite1);
                        songOnline.setFavorite(false);
                        favorite.update("Favorite",false);
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_favorite2);
                        songOnline.setFavorite(true);
                        favorite.update("Favorite",true);
                    }
                }
            });
        });


        // back về trang trước
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        mediaPlayer = new MediaPlayer();

        //click play and pause
        playSeekBar.setMax(100);
        btnPlay.setImageResource(ic_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(update);
                    //mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play);
                    buttonPlayViewModel.select(0);
                } else {
                    //mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause);
                    updateSeekBar();
                    buttonPlayViewModel.select(1);
                }
//                txtSongTotal.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
            }
        });

       /* int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            barVisualizer.setAudioSessionId(audioSessionId);
        }*/
        loadMusic();

        // chỉnh thanh seekbar
        playSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SeekBar seekBar = (SeekBar) view;
                int playPos = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPos);
                txtSongCurrent.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });


        return view;
    }

    private void handleLayoutMusic(int action) {
        switch (action){

        }
    }


    private void PlayerViewPager() {
        playerViewPagerAdapter = new PlayerViewPagerAdapter(this);
        playerViewPager.setAdapter(playerViewPagerAdapter);
        circleIndicator3.setViewPager(playerViewPager);
    }

    private void Init() {
        btnRepeat = view.findViewById(R.id.btnRepeat);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnBack = view.findViewById(R.id.btnBack);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        txtSongCurrent = view.findViewById(R.id.txtSongCurrent);
        txtSongTotal = view.findViewById(R.id.txtSongTotal);
        playSeekBar = view.findViewById(R.id.seekBar);
        playerViewPager = view.findViewById(R.id.view_pager_player);
        circleIndicator3 = view.findViewById(R.id.circle_indicator_3);
        //barVisualizer = view.findViewById(R.id.bar_visualizer);
    }

    // load nhạc
    private void loadMusic() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.seeyouagain);
    }

    //update seekbar
    private Runnable update = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            txtSongCurrent.setText(milliSecondsToTimer(currentDuration));
        }
    };

    private void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            playSeekBar.setProgress((int) (((float)mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(update, 1000);
        }
    }

    //minutes and seconds
    private String milliSecondsToTimer(long milliSeconds) {
        String timeString = "";
        String secondsString;

        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        timeString += minutes + ":" + secondsString;
        return timeString;
    }


}
