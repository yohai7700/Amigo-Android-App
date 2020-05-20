package com.example.amigo.StatsViewModel.StatsRepository.Entity;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "standings_table",
        primaryKeys = {"group_id","player_id"},
        foreignKeys = {
            @ForeignKey(entity = Group.class,
                parentColumns = "id",
                childColumns = "group_id", onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Player.class,
                parentColumns = "id",
                childColumns = "player_id", onDelete = ForeignKey.CASCADE)
        },
        indices = @Index(value = "player_id"))
public class Standings implements Serializable {
    @ColumnInfo(name = "group_id")
    private int groupID;
    @ColumnInfo(name = "player_id")
    private int playerID;

    private int games;
    private int wins;
    private int losses;
    private int goals;
    private int assists;
    private int rating;

    public Standings(int groupID, int playerID, int games, int wins, int losses, int goals, int assists, int rating) {
        this.groupID = groupID;
        this.playerID = playerID;
        this.games = games;
        this.wins = wins;
        this.losses = losses;
        this.goals = goals;
        this.assists = assists;
        this.rating = rating;
    }
    @Ignore
    public Standings(int groupID, int playerID){
        this.groupID = groupID;
        this.playerID = playerID;
        games = wins = losses = goals = assists = rating = 0;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getRating() {
        return Math.round(100 * ((float)(0.6 * wins + 0.3 * getDraws() + 0.3 * goals + 0.2 * assists)) /games);
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private int getDraws(){return games - (wins + losses);}

    private int calculateRating(){
        return Math.round(100 * ((float)(0.6 * wins + 0.3 * getDraws() + 0.3 * goals + 0.2 * assists)) /games);
    }

    public Standings updatedStats(boolean win, boolean lose, int goals, int assists){
        Standings updatedPlayer = new Standings(this.groupID, this.playerID);
        updatedPlayer.setGames(this.games + 1);

        updatedPlayer.setGoals(goals + this.goals);
        updatedPlayer.setAssists(this.assists + assists);
        if(win) {
            updatedPlayer.setWins(this.wins + 1);
            updatedPlayer.setLosses(this.losses);
        }
        else if(lose) {
            updatedPlayer.setLosses(this.losses + 1);
            updatedPlayer.setWins(this.wins);
        }else{
            updatedPlayer.setLosses(this.losses);
            updatedPlayer.setWins(this.wins);
        }
        updatedPlayer.setRating(updatedPlayer.calculateRating());
        return updatedPlayer;
    }
}
