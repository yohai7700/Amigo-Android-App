package com.example.amigo.StatsViewModel.StatsRepository.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.StatsRepository.InterClass.StandingsDetail;

import java.util.List;

@Dao
public interface StandingsDao {
    @Insert
    public void insert(Standings standings);

    @Update
    public void update(Standings standings);

    @Delete
    public void delete(Standings standings);

    @Query("DELETE FROM standings_table" +
            " WHERE group_id = :groupID AND player_id = :playerID")
    public void delete(int playerID, int groupID);

    @Query("SELECT * " +
            "FROM standings_table " +
            "WHERE group_id = :groupID " +
            "ORDER BY rating DESC")
    public LiveData<List<Standings>> getStandingsByGroup(int groupID);

    @Query("SELECT * " +
            "FROM standings_table " +
            "WHERE player_id = :playerID " +
            "ORDER BY rating DESC")
    public LiveData<List<Standings>> getStandingsByPlayer(int playerID);

    @Query("SELECT * " +
            "FROM standings_table " +
            "WHERE group_id = :groupID AND player_id = :playerID")
    public LiveData<Standings> getStandings(int playerID, int groupID);

    @Query("SELECT * " +
            "FROM standings_table")
    public LiveData<List<Standings>> getAllStandings();

    @Query("SELECT S.group_id AS standings_group_id, " +
            "S.player_id AS standings_player_id, " +
            "S.games AS standings_games, " +
            "S.wins AS standings_wins, " +
            "S.losses AS standings_losses, " +
            "S.goals AS standings_goals, " +
            "S.assists AS standings_assists, " +
            "S.rating AS standings_rating, " +
            "G.id AS group_id, " +
            "G.title AS group_title," +
            "G.description AS group_description," +
            "G.creation_date AS group_creation_date, " +
            "G.icon AS group_icon, " +
            "P.id AS player_id, " +
            "P.first_name AS player_first_name," +
            "P.last_name AS player_last_name, " +
            "P.picture AS player_picture " +
            "FROM standings_table AS S " +
            "INNER JOIN player_table AS P ON S.player_id = P.id " +
            "INNER JOIN group_table AS G ON G.id = S.group_id " +
            "WHERE group_id = :groupID " +
            "ORDER BY S.rating DESC")
    public LiveData<List<StandingsDetail>> getStandingsDetailByGroupRatingDesc(int groupID);

    @Query("SELECT S.group_id AS standings_group_id, " +
            "S.player_id AS standings_player_id, " +
            "S.games AS standings_games, " +
            "S.wins AS standings_wins, " +
            "S.losses AS standings_losses, " +
            "S.goals AS standings_goals, " +
            "S.assists AS standings_assists, " +
            "S.rating AS standings_rating, " +
            "G.id AS group_id, " +
            "G.title AS group_title," +
            "G.description AS group_description," +
            "G.creation_date AS group_creation_date, " +
            "G.icon AS group_icon, " +
            "P.id AS player_id, " +
            "P.first_name AS player_first_name," +
            "P.last_name AS player_last_name, " +
            "P.picture AS player_picture " +
            "FROM standings_table AS S " +
            "INNER JOIN player_table AS P ON S.player_id = P.id " +
            "INNER JOIN group_table AS G ON G.id = S.group_id")
    public LiveData<List<StandingsDetail>> getAllStandingsDetail();
}
