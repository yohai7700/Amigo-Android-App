package com.example.amigo.StatsViewModel.ViewModelFactory;

import android.app.Application;

import com.example.amigo.StatsViewModel.ViewModel.GroupStandingsViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class GroupStandingsViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private int groupID;

    public GroupStandingsViewModelFactory(Application application, int groupID) {
        this.application = application;
        this.groupID = groupID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GroupStandingsViewModel(application, groupID);
    }
}
