package com.example.amigo.StatsViewModel.ViewModel;

import android.app.Application;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.StatsRepository.Repository.StandingsRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PlayerStandingsViewModel extends AndroidViewModel {

    private StandingsRepository repository;
    private int playerID;
    private LiveData<List<Standings>> standings;

    public PlayerStandingsViewModel(@NonNull Application application, int playerID) {
        super(application);
        repository = new StandingsRepository(application);
        this.playerID = playerID;
        standings = repository.getStandingsByPlayer(this.playerID);
    }

    public void insert(Standings standings){
        repository.insert(standings);
    }

    public void update(Standings standings){
        repository.update(standings);
    }

    public void delete(Standings standings){
        repository.delete(standings);
    }

    public LiveData<List<Standings>> getAllStandings(){
        return standings;
    }


}
