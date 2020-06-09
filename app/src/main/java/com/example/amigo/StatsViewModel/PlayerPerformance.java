package com.example.amigo.StatsViewModel;

import java.io.Serializable;
import java.util.List;

public class PlayerPerformance implements Serializable {
    private int id, goals, assists;

    public PlayerPerformance(int id){
        this.id = id;
        goals = 0;
        assists = 0;
    }

    public void addGoal(){ goals++; }
    public void reduceGoal(){ goals --; }

    public void addAssist(){ assists++; }
    public void reduceAssist(){ assists --; }

    public int getID(){ return id; }

    public int getGoals() { return goals; }

    public int getAssists() { return assists; }

    public static int getTeamGoals(List<PlayerPerformance> playerPerformances){
        int sum = 0;
        for(PlayerPerformance playerPerformance : playerPerformances)
            sum += playerPerformance.goals;
        return sum;
    }
}
