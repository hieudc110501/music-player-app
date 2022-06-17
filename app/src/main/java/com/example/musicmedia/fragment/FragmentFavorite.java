package com.example.musicmedia.fragment;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicmedia.R;
import com.example.musicmedia.activity.MainActivity;
import com.example.musicmedia.adapter.SongFavoriteAdapter;
import com.example.musicmedia.adapter.SongOnlineAdapter;
import com.example.musicmedia.model.SongOnline;
import com.example.musicmedia.service.MusicPlayerService;
import com.example.musicmedia.viewmodel.DataViewModelOnline;
import com.example.musicmedia.viewmodel.SongOnlineViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavorite extends Fragment {
    View view;
    DataViewModelOnline dataViewModelOnline;
    SongOnlineViewModel songOnlineViewModel;
    MainActivity mainActivity;
    SongOnlineAdapter songAdapter;
    List<SongOnline> list = new ArrayList<SongOnline>();
    RecyclerView recyclerView;

    BroadcastReceiver broadcastReceiver;
    int pos = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mainActivity = (MainActivity) getActivity();

        recyclerView = view.findViewById(R.id.recycler_view_favorite);
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

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_fragment_favorite"));
        clickSong();
        ChangeSongData();

        return view;
    }

    private void handleLayoutMusic(int action) {
        switch (action) {
            case MusicPlayerService.ACTION_PREVIOUS:
                startMusic(songAdapter.getPreviousFavItem(pos));
                break;
            case MusicPlayerService.ACTION_NEXT:
                startMusic(songAdapter.getNextFavItem(pos));
                break;
        }
    }

    void startMusic(SongOnline song) {
        Toast.makeText(getContext(), "Đang tải dữ liệu", Toast.LENGTH_SHORT).show();
        pos = songAdapter.getPos();

        //mainActivity.gotoFragmentOnline(song);
        mainActivity.sendDataOnline(song);

        Intent intent = new Intent(getActivity().getBaseContext(), MusicPlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", song);
        bundle.putBoolean("isFav", true);
        intent.putExtras(bundle);

        getActivity().startService(intent);
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

    //set data recycle view and update data
    public void ChangeSongData() {
        songOnlineViewModel = new ViewModelProvider(getActivity()).get(SongOnlineViewModel.class);
        songOnlineViewModel.getAllSong().observe(getViewLifecycleOwner(), new Observer<List<SongOnline>>() {
            @Override
            public void onChanged(List<SongOnline> songOnlines) {
                list.removeAll(songOnlines);
                for (int i = 0; i < songOnlines.size(); i++) {
                    if (songOnlines.get(i).getFavorite()) {
                        list.add(songOnlines.get(i));
                    }
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

}
