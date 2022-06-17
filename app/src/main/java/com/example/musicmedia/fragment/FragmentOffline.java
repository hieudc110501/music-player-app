package com.example.musicmedia.fragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.legacy.app.FragmentCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicmedia.R;
import com.example.musicmedia.activity.MainActivity;
import com.example.musicmedia.adapter.SongAdapter;
import com.example.musicmedia.model.Song;
import com.example.musicmedia.service.MusicPlayerService;
import com.example.musicmedia.viewmodel.SongViewModel;

import java.util.List;

public class FragmentOffline extends Fragment {
    SongViewModel songViewModel;
    SongAdapter songAdapter;
    MainActivity mainActivity;
    ImageView btnFavorite;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offline, container, false);

        mainActivity = (MainActivity) getActivity();

        if(checkPermission()==false){

        }

        String[] projection={
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };
        String selection = MediaStore.Audio.Media.IS_DOWNLOAD + " != 0";

        String mp3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] Arguments=new String[]{mp3};
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_offline);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        SongAdapter adapter = new SongAdapter(new SongAdapter.IClickItemListener() {
            @Override
            public void onClickItemSong(Song song) {
                mainActivity.gotoFragment(song);
                mainActivity.sendData(song);

                Intent intent = new Intent(getActivity().getBaseContext(), MusicPlayerService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song", song);
                intent.putExtras(bundle);

                getActivity().startService(intent);
            }

        });

        recyclerView.setAdapter(adapter);
        songViewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        songViewModel.getAllSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                Uri songLibraryUri;
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
                    songLibraryUri= MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                }
                else{
                    songLibraryUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                Cursor cursor=view.getContext().getContentResolver().query(songLibraryUri, null, null,null,null);
                while(cursor.moveToNext()){
                    Integer idColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    Integer nameColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    Integer singerColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                    Integer urlColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    Integer timeColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

                    Long id=cursor.getLong(idColumn);
                    String name=cursor.getString(nameColumn);
                    String singer=cursor.getString(singerColumn);
                    String url=cursor.getString(urlColumn);
                    Integer time=cursor.getInt(timeColumn);
                    if (url.endsWith(".mp3")==true) {
                            songs.add(new Song(name, singer, null, url, time, null, null, false));
                    }
                }
                adapter.setSongs(songs);
                Toast.makeText(getView().getContext(), "onChanged", Toast.LENGTH_SHORT).show();
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                songViewModel.delete(adapter.getSongAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
        return view;
    }
    boolean checkPermission(){
        int result=ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
