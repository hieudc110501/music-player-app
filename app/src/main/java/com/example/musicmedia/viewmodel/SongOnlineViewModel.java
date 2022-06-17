package com.example.musicmedia.viewmodel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicmedia.model.SongOnline;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SongOnlineViewModel extends ViewModel {
    private MutableLiveData<List<SongOnline>> mListSongLiveData;
    private List<SongOnline> mListSong;
    private String ID;


    public SongOnlineViewModel(){
        mListSongLiveData = new MutableLiveData<>();
        this.ID = null;
        initData();
    }

    public void setID(String ID) {
        this.ID = ID;
        mListSongLiveData = new MutableLiveData<>();
        initData();
    }
    
    private void initData() {
        mListSong = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MySong").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null) {
                            Log.e("Firestore error!", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()) {
                            if(dc.getType() == DocumentChange.Type.ADDED) {
                                SongOnline tSong = dc.getDocument().toObject(SongOnline.class);
                                if(tSong.getID().equals(ID) || ID == null) {
                                    mListSong.add(tSong);
                                }
                            }
                        }

                        mListSongLiveData.setValue(mListSong);
                    }
                });
    }
    public MutableLiveData<List<SongOnline>> getAllSong() {
        return mListSongLiveData;
    }
}
