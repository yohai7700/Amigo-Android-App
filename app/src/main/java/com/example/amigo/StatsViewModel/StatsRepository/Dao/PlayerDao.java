package com.example.amigo.StatsViewModel.StatsRepository.Dao;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insert(Player player);

    @Update
    public void update(Player player);

    @Delete
    public void delete(Player player);

    @Query("DELETE FROM player_table WHERE id ==:id")
    public void delete(int id);

    @Query("SELECT * FROM player_table")
    public LiveData<List<Player>> getAllPlayers();

    @Query("SELECT * FROM player_table WHERE id == :id")
    public LiveData<Player> getPlayer(int id);

    @Query("SELECT * FROM player_table WHERE id NOT IN (:id)")
    public LiveData<List<Player>> getAllPlayersExcept(ArrayList<Integer> id);

    @Query("SELECT * " +
            "FROM player_table " +
            "WHERE id NOT IN " +
            "   (SELECT player_id " +
            "   FROM standings_table " +
            "   WHERE group_id = :groupID)")
    public LiveData<List<Player>> getAllPlayersNotInGroup(int groupID);

    @Query("SELECT * " +
            "FROM player_table " +
            "WHERE id IN " +
            "   (SELECT player_id " +
            "   FROM standings_table " +
            "   WHERE group_id = :groupID)")
    public LiveData<List<Player>> getAllPlayersInGroup(int groupID);
}
