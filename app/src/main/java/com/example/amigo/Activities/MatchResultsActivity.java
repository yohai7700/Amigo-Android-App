package com.example.amigo.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.amigo.Adapter.PlayerGameResultsAdapter;
import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchResultsActivity extends AppCompatActivity {
    public static String EXTRA_RED_PLAYERS = "com.example.amigo.Activities.GameResultActivity.EXTRA_RED_PLAYERS";
    public static String EXTRA_BLUE_PLAYERS = "com.example.amigo.Activities.GameResultActivity.EXTRA_BLUE_PLAYERS";

    public static String EXTRA_PLAYERS_MATCH = "com.example.amigo.Activities.MatchResultsActivity.EXTRA_PLAYERS_MATCH";
    public static String EXTRA_GOALS_MATCH = "com.example.amigo.Activities.MatchResultsActivity.EXTRA_GOALS_MATCH";
    public static String EXTRA_ASSISTS_MATCH = "com.example.amigo.Activities.MatchResultsActivity.EXTRA_ASSISTS_MATCH";

    public static final int NONE_CLICKED = 0, GOAL_CLICKED = 1, ASSIST_CLICKED = 2;
    //region initing vars
    private List<Player> redPlayers = new ArrayList<>();
    private List<Integer> redGoals = new ArrayList<>();
    private List<Integer> redAssists = new ArrayList<>();
    private List<Player> bluePlayers = new ArrayList<>();
    private List<Integer> blueGoals = new ArrayList<>();
    private List<Integer> blueAssists = new ArrayList<>();
    //endregion

    private int redGoalsNumber;
    private int blueGoalsNumber;
    private int buttonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_results);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Set Match");
        //region retrieve data
        Intent intent = getIntent();
        redPlayers = (List<Player>) intent.getSerializableExtra(EXTRA_RED_PLAYERS);
        bluePlayers = (List<Player>) intent.getSerializableExtra(EXTRA_BLUE_PLAYERS);
        redGoalsNumber = blueGoalsNumber = 0;
        //endregion
        //region sets textviews
        final MaterialTextView score = findViewById(R.id.game_score);
        String gameScore = redGoalsNumber + " - " + blueGoalsNumber;
        score.setText(gameScore);
        final MaterialTextView buttonMessage = findViewById(R.id.button_message);
        buttonMessage.setVisibility(View.INVISIBLE);
        //endregion
        //region initing players
        for (Player player : redPlayers) {
            redGoals.add(0);
            redAssists.add(0);
        }

        for (Player player : bluePlayers) {
            blueGoals.add(0);
            blueAssists.add(0);
        }
        //endregion
        //region sets button
        buttonClicked = NONE_CLICKED;
        MaterialButton finishGameButton = findViewById(R.id.finish_game_button);
        finishGameButton.setText(R.string.finish_match_button_text);
        AppCompatImageButton addGoalButton = findViewById(R.id.add_goal_button);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = GOAL_CLICKED;
                buttonMessage.setText(R.string.goal_button_Message);
                buttonMessage.setVisibility(View.VISIBLE);
            }
        });

        AppCompatImageButton addAssistButton = findViewById(R.id.add_assist_button);
        addAssistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = ASSIST_CLICKED;
                buttonMessage.setText(R.string.assist_button_Message);
                buttonMessage.setVisibility(View.VISIBLE);
            }
        });
        //endregion
        //region finishes game and send data
        finishGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //region sets EXTRA data
                                List<List<Player>> players = new ArrayList<>();
                                players.add(redPlayers);
                                players.add(bluePlayers);
                                List<List<Integer>> goals = new ArrayList<>();
                                goals.add(redGoals);
                                goals.add(blueGoals);
                                List<List<Integer>> assists = new ArrayList<>();
                                assists.add(redAssists);
                                assists.add(blueAssists);
                                //endregion
                                //region sets data
                                Intent data = new Intent();
                                data.putExtra(EXTRA_PLAYERS_MATCH, (Serializable) players);
                                data.putExtra(EXTRA_GOALS_MATCH, (Serializable) goals);
                                data.putExtra(EXTRA_ASSISTS_MATCH, (Serializable) assists);
                                //endregion
                                setResult(RESULT_OK, data);
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MatchResultsActivity.this);
                builder.setMessage("Are you sure you want to finish the match?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();
            }
        });
        //endregion
        //region setting recycler views
        RecyclerView redteamRecyclerView = findViewById(R.id.red_team_recycler_view);
        redteamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        redteamRecyclerView.setHasFixedSize(true);

        RecyclerView blueteamRecyclerView = findViewById(R.id.blue_team_recycler_view);
        blueteamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        blueteamRecyclerView.setHasFixedSize(true);
        //endregion
        //region sets adapters
        final PlayerGameResultsAdapter redTeamAdapter = new PlayerGameResultsAdapter();
        redteamRecyclerView.setAdapter(redTeamAdapter);

        final PlayerGameResultsAdapter blueTeamAdapter = new PlayerGameResultsAdapter();
        blueteamRecyclerView.setAdapter(blueTeamAdapter);
        redTeamAdapter.setStats(redPlayers, redGoals, redAssists);
        blueTeamAdapter.setStats(bluePlayers, blueGoals, blueAssists);

        redTeamAdapter.setOnItemClickListener(new PlayerGameResultsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Player player) {
                switch (buttonClicked) {
                    case GOAL_CLICKED:
                        if (redPlayers.contains(player)) {
                            redGoals.set(redPlayers.indexOf(player), redGoals.get(redPlayers.indexOf(player)) + 1);
                            redTeamAdapter.setStats(redPlayers, redGoals, redAssists);
                            redGoalsNumber++;
                            String gameScore = redGoalsNumber + " - " + blueGoalsNumber;
                            score.setText(gameScore);
                        }
                        break;
                    case ASSIST_CLICKED:
                        if (redPlayers.contains(player)) {
                            redAssists.set(redPlayers.indexOf(player), redAssists.get(redPlayers.indexOf(player)) + 1);
                            redTeamAdapter.setStats(redPlayers, redGoals, redAssists);
                        }
                        break;
                    default:
                }
                buttonClicked = NONE_CLICKED;
                buttonMessage.setText("");
                buttonMessage.setVisibility(View.INVISIBLE);
            }
        });

        blueTeamAdapter.setOnItemClickListener(new PlayerGameResultsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Player player) {
                switch (buttonClicked) {
                    case GOAL_CLICKED:
                        if (bluePlayers.contains(player)) {
                            blueGoals.set(bluePlayers.indexOf(player), blueGoals.get(bluePlayers.indexOf(player)) + 1);
                            blueTeamAdapter.setStats(bluePlayers, blueGoals, blueAssists);
                            blueGoalsNumber++;
                            String gameScore = redGoalsNumber + " - " + blueGoalsNumber;
                            score.setText(gameScore);
                        }
                        break;
                    case ASSIST_CLICKED:
                        if (bluePlayers.contains(player)) {
                            blueAssists.set(bluePlayers.indexOf(player), blueAssists.get(bluePlayers.indexOf(player)) + 1);
                            blueTeamAdapter.setStats(bluePlayers, blueGoals, blueAssists);
                        }
                        break;
                    default:
                }
                buttonClicked = NONE_CLICKED;
                buttonMessage.setText("");
                buttonMessage.setVisibility(View.INVISIBLE);
            }
        });
        //endregion
    }
}
