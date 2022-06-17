package com.example.musicmedia.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicmedia.R;
import com.example.musicmedia.adapter.MainViewPagerAdapter;
import com.example.musicmedia.fragment.FragmentAbout;
import com.example.musicmedia.fragment.FragmentFavorite;
import com.example.musicmedia.fragment.FragmentOnline;
import com.example.musicmedia.fragment.FragmentPlayer;
import com.example.musicmedia.fragment.Profile;
import com.example.musicmedia.model.Song;
import com.example.musicmedia.model.SongOnline;
import com.example.musicmedia.service.MusicPlayerService;
import com.example.musicmedia.viewmodel.DataViewModel;
import com.example.musicmedia.viewmodel.DataViewModelOnline;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MainViewPagerAdapter mainViewPagerAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DataViewModel viewModel;
    DataViewModelOnline viewModelOnline;
    RelativeLayout relativeLayoutMusicMain;

    TextView textViewSongName, textViewSingerName;
    ImageView imgSong, imgPre, imgPlayPause, imgNext;

    BroadcastReceiver broadcastReceiver;
    ActionBarDrawerToggle toggle;

    Song song;
    SongOnline songOnline;
    private boolean isPlaying;
    int pos = -1;

    //commit
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_OFFLINE = 1;
    private static final int FRAGMENT_FAVORITE = 2;
    private static final int FRAGMENT_ABOUT = 3;

    private int CurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));

        pageAdapter();
        createToolbar();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        CurrentFragment = FRAGMENT_HOME;
                        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        CurrentFragment = FRAGMENT_OFFLINE;
                        navigationView.getMenu().findItem(R.id.nav_offline).setChecked(true);
                        break;
                    case 2:
                        CurrentFragment = FRAGMENT_FAVORITE;
                        navigationView.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                        break;
                }
            }
        });
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createToolbar() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ;
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#281d3b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    private void init() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        drawerLayout = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.navigation_view);

        relativeLayoutMusicMain = findViewById(R.id.relativeLayoutMusicMain);
        imgSong = findViewById(R.id.img_song_main);
        textViewSongName = findViewById(R.id.textViewName);
        textViewSingerName = findViewById(R.id.textViewSinger);
        imgPre = findViewById(R.id.img_previous_main);
        imgPlayPause = findViewById(R.id.img_play_or_pause_main);
        imgNext = findViewById(R.id.img_next_main);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                songOnline = (SongOnline) bundle.get("object_song");
                isPlaying = bundle.getBoolean("isPlaying");
                pos = bundle.getInt("pos");
                int action = bundle.getInt("action");

                handleLayoutMusic(action);
            }
        };

        relativeLayoutMusicMain.setOnClickListener(view -> {
            gotoFragmentOnline(songOnline);
        });
    }

    private void pageAdapter() {
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        viewPager2.setAdapter(mainViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Online");
                    break;
                case 1:
                    tab.setText("Offline");
                    break;
                case 2:
                    tab.setText("Favorite");
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (CurrentFragment != FRAGMENT_HOME) {
                viewPager2.setCurrentItem(0);
                CurrentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_offline) {
            if (CurrentFragment != FRAGMENT_OFFLINE) {
                viewPager2.setCurrentItem(1);
                CurrentFragment = FRAGMENT_OFFLINE;
            }
        } else if (id == R.id.nav_favorite) {
            if (CurrentFragment != FRAGMENT_FAVORITE) {
                viewPager2.setCurrentItem(2);
                CurrentFragment = FRAGMENT_FAVORITE;
            }
        } else if (id == R.id.nav_graph) {
            if (CurrentFragment != FRAGMENT_ABOUT) {
                replaceFragment(new FragmentAbout());
                CurrentFragment = FRAGMENT_ABOUT;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceViewPager(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_pager, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.draw_layout, fragment);
        transaction.addToBackStack(fragment.getTag());
        transaction.commit();
    }

    // chuyển sang fragment player và gửi dữ liệu sang
    public void gotoFragment(Song song) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentPlayer fragmentPlayer = new FragmentPlayer();
        fragmentTransaction.replace(R.id.draw_layout, fragmentPlayer);
        fragmentTransaction.addToBackStack(FragmentPlayer.TAG);
        fragmentTransaction.commit();
    }

    public void sendData(Song song) {
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);
        viewModel.select(song);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            return;
        }

        if (requestCode == 100 && resultCode == 100) {
//            gotoFragmentOnline(songOnline);
        }
    }

    public void gotoFragmentOnline(SongOnline song) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentPlayer fragmentPlayer = new FragmentPlayer();
        /*Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        fragmentPlayer.setArguments(bundle);*/
        fragmentTransaction.replace(R.id.draw_layout, fragmentPlayer);
        fragmentTransaction.addToBackStack(FragmentPlayer.TAG);
        fragmentTransaction.commit();
    }

    public void sendDataOnline(SongOnline song) {
        viewModelOnline = new ViewModelProvider(this).get(DataViewModelOnline.class);
        viewModelOnline.select(song);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void handleLayoutMusic(int action) {
        switch (action) {
            case MusicPlayerService.ACTION_GO_TO_PLAYER:
                gotoFragmentOnline(songOnline);
                break;
            case MusicPlayerService.ACTION_START:
                relativeLayoutMusicMain.setVisibility(View.VISIBLE);
                setInfoSong();
                setButtonPlayOrPause();
                break;
            case MusicPlayerService.ACTION_PAUSE:
                setButtonPlayOrPause();
                break;
            case MusicPlayerService.ACTION_RESUME:
                setButtonPlayOrPause();
                break;
            case MusicPlayerService.ACTION_CLOSE:
                relativeLayoutMusicMain.setVisibility(View.GONE);
                break;
            case MusicPlayerService.ACTION_NEXT:

                break;
        }
    }

    private void setInfoSong() {
        if (songOnline == null) {
            return;
        }

        Picasso.with(imgSong.getContext())
                .load(songOnline.getImage())
                .placeholder(R.drawable.ic_about)
                .into(imgSong);
        textViewSongName.setText(songOnline.getName());
        textViewSingerName.setText(songOnline.getArtist());

        imgPlayPause.setOnClickListener(view -> {
            if (isPlaying) {
                sendActionToService(MusicPlayerService.ACTION_PAUSE);
            } else {
                sendActionToService(MusicPlayerService.ACTION_RESUME);
            }
        });

        imgPre.setOnClickListener(view -> {
            sendActionToService(MusicPlayerService.ACTION_PREVIOUS);
        });

        imgNext.setOnClickListener(view -> {
            sendActionToService(MusicPlayerService.ACTION_NEXT);
        });
    }

    private void setButtonPlayOrPause() {
        if (isPlaying) {
            imgPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            imgPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("action_music_service", action);
        startService(intent);
    }


}