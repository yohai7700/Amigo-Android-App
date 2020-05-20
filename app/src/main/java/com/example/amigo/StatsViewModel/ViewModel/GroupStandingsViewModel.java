package com.example.amigo.StatsViewModel.ViewModel;

import android.app.Application;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.StatsRepository.InterClass.StandingsDetail;
import com.example.amigo.StatsViewModel.StatsRepository.Repository.StandingsRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GroupStandingsViewModel extends AndroidViewModel {

    private StandingsRepository standingsRepository;
    private LiveData<List<Standings>> standings;
    private LiveData<List<StandingsDetail>> standingsDetail;
    int groupID;

    public GroupStandingsViewModel(@NonNull Application application, int groupID)
    {
        super(application);
        standingsRepository = new StandingsRepository(application);
        standings = standingsRepository.getStandingsByGroupRatingDesc(groupID);
        this.groupID = groupID;
        standingsDetail = standingsRepository.getStandingsDetailByGroupRetingDesc(groupID);
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
        return standings;
    }

    public LiveData<List<StandingsDetail>> getAllStandingsDetail(){
        return standingsDetail;
    }

}
