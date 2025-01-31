package com.example.amigo.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.amigo.Adapter.GroupStandingsDetailAdapter;
import com.example.amigo.R;
import com.example.amigo.StatsViewModel.PlayerPerformance;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.StatsRepository.InterClass.StandingsDetail;
import com.example.amigo.StatsViewModel.ViewModel.GroupStandingsViewModel;
import com.example.amigo.StatsViewModel.ViewModelFactory.GroupStandingsViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This activity shows standings of a group
 * @author Yohai Mazuz
 */
public class GroupStandingsActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_ID = "com.example.amigo.Activities.GroupStandingsActivity.EXTRA_GROUP_ID";
    public static final String EXTRA_GROUP_TITLE = "com.example.amigo.Activities.GroupStandingsActivity.EXTRA_GROUP_TITLE";

    public static final String STATE_GROUP_ID = "GROUP_ID";

    public static final int RUN_GAME_REQUEST = 1;

    private int groupID;
    private GroupStandingsDetailAdapter adapter;
    private RecyclerView recyclerView;
    private GroupStandingsViewModel groupStandingsViewModel;

    private List<StandingsDetail> standings;
    //@SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_standings);
        //region gets Data
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(EXTRA_GROUP_TITLE));
        groupID = intent.getIntExtra(EXTRA_GROUP_ID, -1);
        //endregion
        setPlayGameFAB();
        //region sets group end case(can't open)
        if (groupID == -1) {
            Toast.makeText(this, "Couldn't open group", Toast.LENGTH_SHORT).show();
            finish();
        }
        //endregion
        setRecyclerView();
        setAdapter();
        setViewModel();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RUN_GAME_REQUEST && resultCode == RESULT_OK) {
            //region retrieve data
            List<List<PlayerPerformance>> playersPerformances =
                    (List<List<PlayerPerformance>>)data.getSerializableExtra(MatchResultsActivity.EXTRA_PLAYERS_PERFORMANCE);
            //endregion
            //region updating players standings
            if(playersPerformances.size() == 2) {
                int winnerIndex = getWinnerTeamIndex(playersPerformances);
                for(List<PlayerPerformance> playersTeam : playersPerformances) {
                    int teamIndex = playersPerformances.indexOf(playersTeam);
                    for (PlayerPerformance playerPerformance : playersTeam) {
                        StandingsDetail tempStands = new StandingsDetail();
                        for (StandingsDetail stands : standings)
                            if (stands.player.id == playerPerformance.getID())
                                tempStands = stands;
                        Standings playerStandings = new Standings(groupID, playerPerformance.getID(),
                                tempStands.standings.getGames(),
                                tempStands.standings.getWins(),
                                tempStands.standings.getLosses(),
                                tempStands.standings.getGoals(),
                                tempStands.standings.getAssists(),
                                tempStands.standings.getRating());
                        //region cases of win, loss or draw
                        if (winnerIndex == teamIndex)
                            groupStandingsViewModel.update(playerStandings.updatedStats(true, false,
                                    playerPerformance.getGoals(),
                                    playerPerformance.getAssists()));
                        else if(winnerIndex == -1)
                            groupStandingsViewModel.update(playerStandings.updatedStats(false, false,
                                    playerPerformance.getGoals(),
                                    playerPerformance.getAssists()));
                        else
                            groupStandingsViewModel.update(playerStandings.updatedStats(false, true,
                                    playerPerformance.getGoals(),
                                    playerPerformance.getAssists()));
                        //endregion
                    }
                }
            }
            //endregion
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_standings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_standings_menu:
                addPlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addPlayer() {
        Intent intent = new Intent(GroupStandingsActivity.this, AddStandingsActivity.class);
        intent.putExtra(AddStandingsActivity.EXTRA_GROUP_ID, groupID);
        startActivity(intent);
    }

    /**
     * assumes 2 team. return the index of the winning team or -1 if draw
     * @param playersPerformances the teams performances
     * @return index of winning team
     */
    private int getWinnerTeamIndex(List<List<PlayerPerformance>> playersPerformances){
        int index = -1;
        List<Integer> goalsNumber = new ArrayList<>();
        int sum = 0;
        for (PlayerPerformance playerPerformance : playersPerformances.get(0))
            sum += playerPerformance.getGoals();
        goalsNumber.add(sum);
        sum = 0;
        for (PlayerPerformance playerPerformance : playersPerformances.get(1))
            sum += playerPerformance.getGoals();
        goalsNumber.add(sum);
        if(goalsNumber.get(0) > goalsNumber.get(1))
            return 0;
        if(goalsNumber.get(0) < goalsNumber.get(1))
            return 1;
        return -1;
    }

    /**
     * sets FAB that start game, moves to team assigning activity.
     */
    private void setPlayGameFAB(){
        FloatingActionButton playGameFABtn = (findViewById(R.id.play_game_button));
        playGameFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupStandingsActivity.this, TeamAssignmentActivity.class);
                intent.putExtra(TeamAssignmentActivity.EXTRA_GROUP_ID, groupID);
                startActivityForResult(intent, RUN_GAME_REQUEST);
            }
        });
    }

    /**
     * sets recycler view that shows standings
     */
    private void setRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.standings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //region sets recycler-view background opacity - 180/255
        Drawable background = recyclerView.getBackground();
        background.setAlpha(180);
        //endregion
    }

    /**
     * sets adapter for recycler view
     */
    private void setAdapter(){
        final GroupStandingsDetailAdapter groupStandingsAdapter = new GroupStandingsDetailAdapter();
        groupStandingsAdapter.setContext(getApplicationContext());
        recyclerView.setAdapter(groupStandingsAdapter);
    }

    /**
     * sets view model of standings
     */
    private void setViewModel(){
        groupStandingsViewModel = new ViewModelProvider(this, new GroupStandingsViewModelFactory(getApplication(), groupID)).get(GroupStandingsViewModel.class);
        groupStandingsViewModel.getAllStandingsDetail().observe(this, new Observer<List<StandingsDetail>>() {
            @Override
            public void onChanged(List<StandingsDetail> standingsDetails) {
                adapter.submitList(standingsDetails);
                standings = standingsDetails;
            }
        });
    }
}
