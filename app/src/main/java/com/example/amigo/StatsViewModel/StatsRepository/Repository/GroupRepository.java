package com.example.amigo.StatsViewModel.StatsRepository.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.amigo.StatsViewModel.StatsRepository.Dao.GroupDao;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;
import com.example.amigo.StatsViewModel.StatsRepository.StatsDataBase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class GroupRepository {
    private GroupDao groupDao;

    public GroupRepository(Application application){
        StatsDataBase statsDataBase = StatsDataBase.getInstance(application);
        groupDao = statsDataBase.groupDao();
    }

    public void insert(Group group){
        new GroupRepository.InsertAsyncTask(groupDao).execute(group);
    }

    public void update(Group group){
        new GroupRepository.UpdateAsyncTask(groupDao).execute(group);
    }

    public void delete(Group group){
        new GroupRepository.DeleteAsyncTask(groupDao).execute(group);
    }

    public LiveData<Group> getGroup(int id){
        return groupDao.getGroup(id);
    }

    public LiveData<List<Group>> getAllGroups(){
        return groupDao.getAllGroups();
    }

    private static class InsertAsyncTask extends AsyncTask<Group, Void, Void> {
        private GroupDao groupDao;

        public InsertAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(Group... group) {
            groupDao.insert(group[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Group, Void, Void>{
        private GroupDao groupDao;

        public UpdateAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(Group... group) {
            groupDao.update(group[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Group, Void, Void>{
        private GroupDao groupDao;

        public DeleteAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(Group... group) {
            groupDao.delete(group[0]);
            return null;
        }
    }
}
