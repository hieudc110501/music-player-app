package com.example.musicmedia.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.musicmedia.model.Song;

import java.util.List;

@Dao
public interface SongDao {
    @Delete
    void deleteSong(Song song);

    @Insert
    void insertSong(Song song);

    @Update
    void updateSong(Song song);

    @Query("SELECT * FROM song ORDER BY id DESC")
    LiveData<List<Song>> getAllSongs();

}
