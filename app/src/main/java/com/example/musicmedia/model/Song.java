package com.example.musicmedia.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "song")
public class Song implements Serializable {
    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name="singer")
    private String singer;

    @ColumnInfo(name="category")
    private String category;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "time")
    private Integer time;

    @ColumnInfo(name="image")
    private String image;

    @ColumnInfo(name="lyric")
    private String lyric;

    @ColumnInfo(name="favorite")
    private Boolean favorite;


    @Ignore

    public Song(String name, String singer, String category, String url, Integer time, String image, String lyric, Boolean favorite) {
        this.name = name;
        this.singer = singer;
        this.category = category;
        this.url = url;
        this.time = time;
        this.image = image;
        this.lyric = lyric;
        this.favorite = favorite;
    }
    public Song(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}

