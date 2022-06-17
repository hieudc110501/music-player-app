package com.example.musicmedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ButtonPlayViewModel extends ViewModel {
    private final MutableLiveData<Integer> selected = new MutableLiveData<Integer>();
    public void select(Integer i) {
        selected.setValue(i);
    }

    public LiveData<Integer> getSelected() {
        return selected;
    }
}
