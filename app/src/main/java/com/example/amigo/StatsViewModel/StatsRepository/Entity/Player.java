package com.example.amigo.StatsViewModel.StatsRepository.Entity;


import android.graphics.Bitmap;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_table")
public class Player implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    private String name;


    @ColumnInfo(name = "picture")
    private Bitmap picture;

    public Player(String name, Bitmap picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }


    public Bitmap getPicture(){return picture;}
}
