package com.example.amigo.StatsViewModel.ViewModel;

import android.app.Application;

import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;
import com.example.amigo.StatsViewModel.StatsRepository.Repository.GroupRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GroupViewModel extends AndroidViewModel {

    private GroupRepository repository;

    public GroupViewModel(@NonNull Application application) {
        super(application);
        repository = new GroupRepository(application);
    }

    public void insert(Group group){
        repository.insert(group);
    }

    public void update(Group group){
        repository.update(group);
    }

    public void delete(Group group){
        repository.delete(group);
    }

    public LiveData<List<Group>> getAllGroups(){
        return repository.getAllGroups();
    }

    public LiveData<Group> getGroup(int id){return repository.getGroup(id);}
}
