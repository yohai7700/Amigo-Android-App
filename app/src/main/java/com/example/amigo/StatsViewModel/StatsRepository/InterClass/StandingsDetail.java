package com.example.amigo.StatsViewModel.StatsRepository.InterClass;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;

import java.io.Serializable;

import androidx.room.Embedded;

public class StandingsDetail implements Serializable {
    @Embedded(prefix ="standings_")
    public Standings standings;
    @Embedded(prefix = "group_")
    public Group group;
    @Embedded(prefix = "player_")
    public Player player;
}
