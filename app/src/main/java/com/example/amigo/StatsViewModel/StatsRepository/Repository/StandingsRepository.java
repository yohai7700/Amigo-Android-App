package com.example.amigo.StatsViewModel.StatsRepository.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.amigo.StatsViewModel.StatsRepository.Dao.StandingsDao;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.StatsRepository.InterClass.StandingsDetail;
import com.example.amigo.StatsViewModel.StatsRepository.StatsDataBase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class StandingsRepository {
    private StandingsDao standingsDao;

    public StandingsRepository(Application application){
        StatsDataBase statsDataBase = StatsDataBase.getInstance(application);
        standingsDao = statsDataBase.standingsDao();
    }

    public void insert(Standings standings){
        new InsertAsyncTask(standingsDao).execute(standings);
    }

    public void update(Standings standings){
        new UpdateAsyncTask(standingsDao).execute(standings);
    }

    public void delete(Standings standings){
        new DeleteAsyncTask(standingsDao).execute(standings);
    }

    public LiveData<List<Standings>> getStandingsByGroupRatingDesc(int id){
        return standingsDao.getStandingsByGroup(id);
    }

    public LiveData<List<Standings>> getStandingsByPlayer(int id){
        return standingsDao.getStandingsByPlayer(id);
    }

    public LiveData<Standings> getStandings(int groupID, int playerID){
        return standingsDao.getStandings(playerID, groupID);
    }

    public LiveData<List<Standings>> getAllStandings(){
        return standingsDao.getAllStandings();
    }

    public LiveData<List<StandingsDetail>> getAllStandingssDetail() { return standingsDao.getAllStandingsDetail();}

    public LiveData<List<StandingsDetail>> getStandingsDetailByGroupRetingDesc(int id) { return standingsDao.getStandingsDetailByGroupRatingDesc(id);}

    private static class InsertAsyncTask extends AsyncTask<Standings, Void, Void>{
        private StandingsDao standingsDao;

        public InsertAsyncTask(StandingsDao standingsDao) {
            this.standingsDao = standingsDao;
        }

        @Override
        protected Void doInBackground(Standings... standings) {
            standingsDao.insert(standings[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Standings, Void, Void>{
        private StandingsDao standingsDao;

        public UpdateAsyncTask(StandingsDao standingsDao) {
            this.standingsDao = standingsDao;
        }

        @Override
        protected Void doInBackground(Standings... standings) {
            standingsDao.update(standings[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Standings, Void, Void>{
        private StandingsDao standingsDao;

        public DeleteAsyncTask(StandingsDao standingsDao) {
            this.standingsDao = standingsDao;
        }

        @Override
        protected Void doInBackground(Standings... standings) {
            standingsDao.delete(standings[0]);
            return null;
        }
    }
}
