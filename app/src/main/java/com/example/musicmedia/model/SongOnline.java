package com.example.musicmedia.model;

import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "song")
public class SongOnline implements Serializable {
    private String Artist;
    private String Category;
    private Boolean Favorite;
    private String Image;
    private String Lyrics;
    private String Name;
    private String Path;
    private int Time;
    private String ID;

    public SongOnline()
    {

    }

    public  SongOnline (String ID, String Artist, String Category, Boolean Favorite, String Image, String Lyrics, String Name, String Path, int Time)
    {
        this.ID = ID;
        this.Artist=Artist;
        this.Category=Category;
        this.Favorite = Favorite;
        this.Image = Image;
        this.Lyrics = Lyrics;
        this.Name = Name;
        this.Path = Path;
        this.Time = Time;
    }


    public String getArtist() {
        return Artist;
    }

    public void setArtist(String Artist) {
        this.Artist = Artist;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public Boolean getFavorite() {
        return Favorite;
    }

    public void setFavorite(Boolean Favorite) {
        this.Favorite = Favorite;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image){this.Image = Image; }

    public String getLyrics() {return Lyrics;}

    public void setLyrics(String Lyrics) {
        this.Lyrics = Lyrics;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String Path) {
        this.Path = Path;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }
}

