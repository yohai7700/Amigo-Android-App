package com.example.amigo.StatsViewModel.ViewModel;

import android.app.Application;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.StatsRepository.Repository.StandingsRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class StandingsViewModel extends AndroidViewModel {

    private StandingsRepository standingsRepository;

    public StandingsViewModel(@NonNull Application application) {
        super(application);
        standingsRepository = new StandingsRepository(application);
    }

    public void insert(Standings standings){
        standingsRepository.insert(standings);
    }

    public void update(Standings standings){
        standingsRepository.update(standings);
    }

    public void delete(Standings standings){
        standingsRepository.delete(standings);
    }

    public LiveData<List<Standings>> getAllStandings(){
        return standingsRepository.getAllStandings();
    }

    public LiveData<List<Standings>>  getGroupStandings(int groupID){
        return standingsRepository.getStandingsByGroupRatingDesc(groupID);
    }
}
