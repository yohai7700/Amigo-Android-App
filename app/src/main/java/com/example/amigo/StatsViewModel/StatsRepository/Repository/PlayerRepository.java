package com.example.amigo.StatsViewModel.StatsRepository.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.amigo.StatsViewModel.StatsRepository.Dao.PlayerDao;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.example.amigo.StatsViewModel.StatsRepository.StatsDataBase;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class PlayerRepository {
    private PlayerDao playerDao;

    public PlayerRepository(Application application) {
        StatsDataBase statsDataBase = StatsDataBase.getInstance(application);
        playerDao = statsDataBase.playerDao();
    }

    public void insert(Player player) {
        new PlayerRepository.InsertAsyncTask(playerDao).execute(player);
    }

    public void update(Player player) {
        new PlayerRepository.UpdateAsyncTask(playerDao).execute(player);
    }

    public void delete(Player player) {
        new PlayerRepository.DeleteAsyncTask(playerDao).execute(player);
    }

    public LiveData<Player> getPlayer(int id){
        return playerDao.getPlayer(id);
    }

    public LiveData<List<Player>> getAllPlayers(){
        return playerDao.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayerExcept(final List<Player> players){
        ArrayList<Integer> ids = new ArrayList<Integer>(players.size());
        for (int i = 0; i < players.size() ; i++)
            ids.set(i, players.get(i).id);
        return playerDao.getAllPlayersExcept(ids);
    }

    public LiveData<List<Player>> getAllPlayersNotInGroup(int groupID){
        return playerDao.getAllPlayersNotInGroup(groupID);};

    public LiveData<List<Player>> getAllPlayersInGroup(int groupID){
        return playerDao.getAllPlayersInGroup(groupID);
    }

    private static class InsertAsyncTask extends AsyncTask<Player, Void, Void> {
        private PlayerDao playerDao;

        public InsertAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... player) {
            playerDao.insert(player[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Player, Void, Void> {
        private PlayerDao playerDao;

        public UpdateAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... player) {
            playerDao.update(player[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Player, Void, Void> {
        private PlayerDao playerDao;

        public DeleteAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... player) {
            playerDao.delete(player[0]);
            return null;
        }
    }
}
