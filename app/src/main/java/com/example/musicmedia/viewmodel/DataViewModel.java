package com.example.musicmedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicmedia.model.Song;

public class DataViewModel extends ViewModel {
    private final MutableLiveData<Song> selected = new MutableLiveData<Song>();
    public void select(Song song) {
        selected.setValue(song);
    }

    public LiveData<Song> getSelected() {
        return selected;
    }
}
