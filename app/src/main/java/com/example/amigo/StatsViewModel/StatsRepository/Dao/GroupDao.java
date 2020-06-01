package com.example.amigo.StatsViewModel.StatsRepository.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;

import java.util.List;

@Dao
public interface GroupDao {

    @Insert
    public void insert(Group group);

    @Update
    public void update(Group group);

    @Delete
    public void delete(Group group);

    @Query("DELETE FROM group_table WHERE id ==:id")
    public void delete(int id);

    @Query("SELECT * FROM group_table ORDER BY creation_date")
    public LiveData<List<Group>> getAllGroups();

    @Query("SELECT * FROM group_table WHERE id == :id")
    public LiveData<Group> getGroup(int id);
}
