package com.example.amigo.StatsViewModel.StatsRepository.Entity;


import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "player_table")
public class Player implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "picture")
    private Bitmap picture;

    public Player(String firstName, String lastName, Bitmap picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Bitmap getPicture(){return picture;}
}
