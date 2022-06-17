package com.example.musicmedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicmedia.model.Song;
import com.example.musicmedia.model.SongOnline;

public class DataViewModelOnline extends ViewModel {
    private final MutableLiveData<SongOnline> selected = new MutableLiveData<SongOnline>();
    public void select(SongOnline song) {
        selected.setValue(song);
    }

    public LiveData<SongOnline> getSelected() {
        return selected;
    }
}
