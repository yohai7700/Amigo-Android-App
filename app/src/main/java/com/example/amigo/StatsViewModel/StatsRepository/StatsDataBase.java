package com.example.amigo.StatsViewModel.StatsRepository;

import android.content.Context;

import com.example.amigo.StatsViewModel.StatsRepository.Dao.GroupDao;
import com.example.amigo.StatsViewModel.StatsRepository.Dao.PlayerDao;
import com.example.amigo.StatsViewModel.StatsRepository.Dao.StandingsDao;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Player.class, Group.class, Standings.class},
        exportSchema = false,
        version = 1)
@TypeConverters(Converters.class)
public abstract class StatsDataBase extends RoomDatabase {
    private static StatsDataBase instance;

    public abstract PlayerDao playerDao();
    public abstract GroupDao groupDao();
    public abstract StandingsDao standingsDao();

    public static synchronized StatsDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(),
                            StatsDataBase.class,
                            "stats_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            /*instance.getOpenHelper().getWritableDatabase().execSQL("" +
                    "CREATE TRIGGER delete_standings_of_group " +
                    "BEFORE DELETE ON group_table " +
                    "FOR EACH ROW " +
                    "BEGIN" +
                    "   DELETE FROM standings_table WHERE group_id = OLD.id;" +
                    "END");*/
        }
    };

    /*private static class PopulateDataBaseAsyncTask extends AsyncTask<Void,Void,Void>{
        private PlayerDao playerDao;
        private GroupDao groupDao;
        private StandingsDao standingsDao;

        private PopulateDataBaseAsyncTask(StatsDataBase statsDataBase){
            playerDao = statsDataBase.playerDao();
            groupDao = statsDataBase.groupDao();
            standingsDao = statsDataBase.standingsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            groupDao.insert(new Group("Mazuz Family", "This is the group of the mazuz brothers"));
            groupDao.insert(new Group("Haredim", "This is the group of the Haredim field"));
            return null;
        }
    }*/
}
