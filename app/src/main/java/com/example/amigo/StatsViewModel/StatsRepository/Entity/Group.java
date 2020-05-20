package com.example.amigo.StatsViewModel.StatsRepository.Entity;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "group_table")
public class Group implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String title;

    private String description;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private Bitmap icon;

    @ColumnInfo(name = "creation_date")
    private Date creationDate;

    //TODO: add group icon
    public Group(String title, String description, Bitmap icon) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.creationDate = Calendar.getInstance().getTime();
    }

    public Group(Group other){
        id = other.id;
        title = other.getTitle();
        description = other.getDescription();
        icon = other.getIcon().copy(other.getIcon().getConfig(), other.getIcon().isMutable());
        this.creationDate = other.getCreationDate();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Bitmap getIcon(){
        return icon;
    }
}
