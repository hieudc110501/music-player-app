package com.example.musicmedia.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.musicmedia.dao.SongDao;
import com.example.musicmedia.database.SongDatabase;
import com.example.musicmedia.model.Song;

import java.util.List;

public class SongRepository {
    private SongDao songDao;
    private LiveData<List<Song>> allSongs;

    public  SongRepository(Application application){
        SongDatabase dataBase=SongDatabase.getInstance(application);
        songDao=dataBase.songDao();
        allSongs=songDao.getAllSongs();
    }
    public void insert(Song song){
        new InsertNoteAsyncTask(songDao).execute(song);
    }
    public void update(Song song){
        new UpdateNoteAsyncTask(songDao).execute(song);
    }
    public void delete(Song song){
        new DeleteNoteAsyncTask(songDao).execute(song);
    }
    public LiveData<List<Song>> getAllSongs(){
        return allSongs;
    }
    private static class InsertNoteAsyncTask extends AsyncTask<Song,Void,Void>{
        private SongDao songDao;

        private InsertNoteAsyncTask(SongDao songDao){
            this.songDao=songDao;
        }
        @Override
        protected Void doInBackground(Song... songs) {
            songDao.insertSong(songs[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Song,Void,Void>{
        private SongDao songDao;

        private UpdateNoteAsyncTask(SongDao songDao){
            this.songDao=songDao;
        }
        @Override
        protected Void doInBackground(Song... songs) {
            songDao.updateSong(songs[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<Song,Void,Void>{
        private SongDao songDao;

        private DeleteNoteAsyncTask(SongDao songDao){
            this.songDao=songDao;
        }
        @Override
        protected Void doInBackground(Song... songs) {
            songDao.deleteSong(songs[0]);
            return null;
        }
    }
}
