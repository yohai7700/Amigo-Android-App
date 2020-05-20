package com.example.amigo.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.amigo.Adapter.PlayerAssigningAdapter;
import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.example.amigo.StatsViewModel.StatsRepository.InterClass.StandingsDetail;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TeamAssignmentActivity extends AppCompatActivity {
    public static String EXTRA_GROUP_ID = "com.example.amigo.Activities.TeamAssignmentActivity.EXTRA_GROUP_ID";
    public static String EXTRA_PLAYERS_DETAILS = "com.example.amigo.Activities.TeamAssignmentActivity.EXTRA_PLAYERS_DETAILS";

    public static int RUN_GAME_REQUEST = 1;

    RecyclerView playerAssignmentRecyclerView;
    private List<Player> redPlayers = new ArrayList<>();
    private List<Player> bluePlayers = new ArrayList<>();
    private List<StandingsDetail> players;
    private PlayerAssigningAdapter playerAssigningAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_assignment);

        setTitle("Teams Assignment");
        final Intent intent = getIntent();
        players = (List<StandingsDetail>) intent.getSerializableExtra(EXTRA_PLAYERS_DETAILS);
        /*
        List<Integer> teamsRating = new ArrayList<>();
        List<List<StandingsDetail>> teams = divideToEqualTeams(players, 3);
        for (List<StandingsDetail> team: teams)
            teamsRating.add(teamTotalRating(team));
            */
        //region sets RecyclerView for players
        playerAssignmentRecyclerView = findViewById(R.id.player_assigning_recycler_view);
        playerAssignmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        playerAssignmentRecyclerView.setHasFixedSize(true);
        //endregion implements RecyclerView for red team

        //region sets players - adapter
        playerAssigningAdapter = new PlayerAssigningAdapter();
        playerAssignmentRecyclerView.setAdapter(playerAssigningAdapter);
        playerAssigningAdapter.setContext(this);
        playerAssigningAdapter.setPlayers(players);
        //endregion

        playerAssigningAdapter.setOnItemCheckListener(new PlayerAssigningAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(StandingsDetail standingsDetail, String currentTeam) {
                if (currentTeam.equals(PlayerAssigningAdapter.RED_TEAM))
                    redPlayers.add(standingsDetail.player);
                else
                    bluePlayers.add(standingsDetail.player);
            }

            @Override
            public void onItemUncheck(StandingsDetail standingsDetail, String currentTeam) {
                if (currentTeam.equals(PlayerAssigningAdapter.RED_TEAM))
                    redPlayers.remove(standingsDetail.player);
                else
                    bluePlayers.remove(standingsDetail.player);
            }
        });
        //TODO: remember to fix assigning bug

        //region initing team button
        final MaterialButton teamButton = findViewById(R.id.team_button);
        teamButton.setText(R.string.red_team_name);
        teamButton.setBackgroundColor(ContextCompat.getColor(this, R.color.redTeamColor));
        playerAssigningAdapter.setCurrentTeam(PlayerAssigningAdapter.RED_TEAM);
        teamButton.setTextColor(Color.WHITE);
        //endregion initiating team button

        //region implements onClickListener for team button
        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamButton.getText().equals("Red Team")) {
                    teamButton.setText(R.string.blue_team_name);
                    teamButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blueTeamColor));
                    playerAssigningAdapter.setCurrentTeam(PlayerAssigningAdapter.BLUE_TEAM);
                } else {
                    teamButton.setText(R.string.red_team_name);
                    teamButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.redTeamColor));
                    playerAssigningAdapter.setCurrentTeam(PlayerAssigningAdapter.RED_TEAM);
                }
            }
        });
        //endregion implements onClickListener for team button

        //region sets FAB run game
        FloatingActionButton runGameFAB = findViewById(R.id.run_game_button);
        runGameFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (redPlayers.isEmpty() || bluePlayers.isEmpty()) {
                    Toast.makeText(TeamAssignmentActivity.this, "Each team must have at least one player", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent startGame = new Intent(TeamAssignmentActivity.this, MatchResultsActivity.class);
                startGame.putExtra(MatchResultsActivity.EXTRA_RED_PLAYERS, (Serializable) redPlayers);
                startGame.putExtra(MatchResultsActivity.EXTRA_BLUE_PLAYERS, (Serializable) bluePlayers);
                startActivityForResult(startGame, RUN_GAME_REQUEST);
            }
        });
        //endregion
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RUN_GAME_REQUEST && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        } else {
            setResult(RESULT_CANCELED, data);
        }
    }

    private List<List<StandingsDetail>> divideToEqualTeams(List<StandingsDetail> oldPlayers, int numOfTeams) {
        List<StandingsDetail> players = new ArrayList<>(oldPlayers);
        for (StandingsDetail player : players)
            if(player.standings.getGames() == 0)
                players.remove(player);
        List<List<StandingsDetail>> teams = new ArrayList<>();
        for(int i = 0; i < numOfTeams; i++)
            teams.add(new ArrayList<StandingsDetail>());
        List<Integer> teamsRating = new ArrayList<>();
        while (!players.isEmpty()) {
            StandingsDetail maxPlayer = findMaxRatingPlayer(players);
            teams.get(findMinRatingAndMaxSizeTeamIndex(teams)).add(maxPlayer);
            players.remove(maxPlayer);
        }
        return teams;
    }

    private int findMinRatingAndMaxSizeTeamIndex(List<List<StandingsDetail>> teams) {
        if (teams == null || teams.isEmpty())
            return -1;
        int minRating = teamTotalRating(teams.get(0));
        for (int i = 1; i < teams.size(); i++)
            if (teamTotalRating(teams.get(i)) < minRating)
                minRating = teamTotalRating(teams.get(i));
        int maxSize = Integer.MIN_VALUE;
        int minTeamIndex = -1;
        for (List<StandingsDetail> team : teams)
            if (teamTotalRating(team) == minRating)
                if(team.size() > maxSize) {
                    minTeamIndex = teams.indexOf(team);
                    maxSize = team.size();
                }
        return minTeamIndex;
    }

    private int teamTotalRating(List<StandingsDetail> team) {
        if (team == null || team.isEmpty())
            return -1;
        int totalRating = 0;
        for (StandingsDetail standings : team)
            totalRating += standings.standings.getRating();
        return totalRating;
    }

    private StandingsDetail findMaxRatingPlayer(List<StandingsDetail> players){
        StandingsDetail maxPlayer = null;
        if(players == null || players.isEmpty())
            return null;
        int maxRating = Integer.MIN_VALUE;
        for (StandingsDetail player: players) {
            if(player.standings.getRating() > maxRating){
                maxRating = player.standings.getRating();
                maxPlayer = player;
            }
        }
        return maxPlayer;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("RED_PLAYERS", (Serializable) redPlayers);
        outState.putSerializable("BLUE_PLAYERS", (Serializable) bluePlayers);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List<StandingsDetail> redPlayers = (List<StandingsDetail>) savedInstanceState.getSerializable("RED_PLAYERS");
        List<StandingsDetail> bluePlayers = (List<StandingsDetail>) savedInstanceState.getSerializable("BLUE_PLAYERS");
        for(StandingsDetail player : redPlayers)
            playerAssigningAdapter.getOnItemCheckListener().onItemCheck(player, PlayerAssigningAdapter.RED_TEAM);
        for(StandingsDetail player : bluePlayers)
            playerAssigningAdapter.getOnItemCheckListener().onItemCheck(player, PlayerAssigningAdapter.BLUE_TEAM);
    }
}
