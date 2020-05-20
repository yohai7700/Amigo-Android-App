package com.example.amigo.StatsViewModel.ViewModelFactory;

import android.app.Application;

import com.example.amigo.StatsViewModel.ViewModel.PlayerStandingsViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PlayerStandingsViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private int playerID;

    public PlayerStandingsViewModelFactory(Application application, int playerID) {
        this.application = application;
        this.playerID = playerID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PlayerStandingsViewModel(application, playerID);
    }
}
