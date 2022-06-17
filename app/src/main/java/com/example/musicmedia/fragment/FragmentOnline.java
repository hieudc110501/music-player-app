package com.example.musicmedia.fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicmedia.R;
import com.example.musicmedia.activity.MainActivity;
import com.example.musicmedia.adapter.SongOnlineAdapter;
import com.example.musicmedia.model.Song;
import com.example.musicmedia.model.SongOnline;
import com.example.musicmedia.service.MusicPlayerService;
import com.example.musicmedia.viewmodel.DataViewModelOnline;
import com.example.musicmedia.viewmodel.SongOnlineViewModel;
import com.example.musicmedia.viewmodel.SongViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentOnline extends Fragment {

    View view;
    DataViewModelOnline dataViewModelOnline;
    SongOnlineAdapter songAdapter;
    MainActivity mainActivity;
    SongOnlineViewModel songOnlineViewModel;
    List<SongOnline> list = new ArrayList<>();
    RecyclerView recyclerView;

    BroadcastReceiver broadcastReceiver;
    int pos = -1;

    @SuppressLint("FragmentLiveDataObserve")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        mainActivity = (MainActivity) getActivity();


        recyclerView = view.findViewById(R.id.recycler_view_online);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

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
                int action = bundle.getInt("action");

                handleLayoutMusic(action);
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_fragment_online"));

        clickSong();
        ChangeSongData();

        return view;
    }

    private void handleLayoutMusic(int action) {
        switch (action) {
            case MusicPlayerService.ACTION_PREVIOUS:
                startMusic(songAdapter.getPreviousItem(pos));
                break;
            case MusicPlayerService.ACTION_NEXT:
                startMusic(songAdapter.getNextItem(pos));
                break;
        }
    }

    //click song to go to player
    public void clickSong() {
        songAdapter = new SongOnlineAdapter(new SongOnlineAdapter.IClickItemListener() {
            @Override
            public void onClickItemSong(SongOnline song) {
                startMusic(song);
            }
        });
    }

    void startMusic(SongOnline song) {
        Toast.makeText(getContext(), "Đang tải dữ liệu", Toast.LENGTH_SHORT).show();
        pos = songAdapter.getPos();

        //mainActivity.gotoFragmentOnline(song);
        mainActivity.sendDataOnline(song);

        Intent intent = new Intent(getActivity().getBaseContext(), MusicPlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", song);
//        bundle.putBoolean("isFav", false);
        intent.putExtras(bundle);

        getActivity().startService(intent);
    }

    //set data recycle view and update data
    public void ChangeSongData() {
        songOnlineViewModel = new ViewModelProvider(getActivity()).get(SongOnlineViewModel.class);
        songOnlineViewModel.getAllSong().observe(getViewLifecycleOwner(), new Observer<List<SongOnline>>() {
            @Override
            public void onChanged(List<SongOnline> songOnlines) {
                list.removeAll(songOnlines);
                for (SongOnline x : songOnlines) {
                    list.add(x);
                }

                songAdapter.setSongs(list);
                recyclerView.setAdapter(songAdapter);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // click to search
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //songAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                songAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public int getPos() {
        return pos;
    }
}
