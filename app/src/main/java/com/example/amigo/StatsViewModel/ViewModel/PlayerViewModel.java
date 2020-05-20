package com.example.amigo.StatsViewModel.ViewModel;

import android.app.Application;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.example.amigo.StatsViewModel.StatsRepository.Repository.PlayerRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PlayerViewModel extends AndroidViewModel {

    private PlayerRepository repository;
    private LiveData<List<Player>> allPlayers;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        repository = new PlayerRepository(application);
        allPlayers = repository.getAllPlayers();
    }

    public void insert(Player player){
        repository.insert(player);
    }

    public void update(Player player){
        repository.update(player);
    }

    public void delete(Player player){
        repository.delete(player);
    }

    public LiveData<List<Player>> getAllPlayersExcept(List<Player> players){
        return repository.getAllPlayerExcept(players);
    }

    public  LiveData<List<Player>> getAllPlayersNotInGroup(int groupID){
        return repository.getAllPlayersNotInGroup(groupID);
    }

    public LiveData<List<Player>> getGetAllPlayersInGroup(int groupID){
        return repository.getAllPlayersInGroup(groupID);
    }

    public LiveData<List<Player>> getAllPlayers(){
        return allPlayers;
    }

}
