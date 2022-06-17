package com.example.musicmedia.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.musicmedia.dao.SongDao;
import com.example.musicmedia.model.Song;

@Database(entities = {Song.class},version = 1)
public abstract class SongDatabase extends RoomDatabase {
    private static SongDatabase instance;
    public abstract SongDao songDao();
    public static synchronized SongDatabase getInstance(Context context){
        if(instance == null){
            instance= Room.databaseBuilder(context.getApplicationContext(), SongDatabase.class,"song_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private SongDao songDao;

        private PopulateDbAsyncTask(SongDatabase db){
            songDao=db.songDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
