package com.example.amigo.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.amigo.Adapter.PlayerGameResultsAdapter;
import com.example.amigo.R;
import com.example.amigo.StatsViewModel.PlayerPerformance;
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
    public static String EXTRA_RED_PLAYERS_NAMES = "com.example.amigo.Activities.GameResultActivity.EXTRA_RED_PLAYERS_NAMES";
    public static String EXTRA_BLUE_PLAYERS_NAMES = "com.example.amigo.Activities.GameResultActivity.EXTRA_BLUE_PLAYERS_NAMES";

    public static String EXTRA_PLAYERS_PERFORMANCE = "com.example.amigo.Activities.MatchResultsActivity.EXTRA_PLAYERS_PERFORMANCE";

    enum ButtonClick{GOAL, ASSIST, NONE}
    enum Team{RED, BLUE}
    //region initiating vars

    private final List<PlayerPerformance> redPlayersPerformance = new ArrayList<>(),
                                            bluePlayersPerformance = new ArrayList<>();
    //endregion

    private final PlayerGameResultsAdapter redTeamAdapter = new PlayerGameResultsAdapter(),
                                            blueTeamAdapter = new PlayerGameResultsAdapter();

    private List<String> redPlayersNames, bluePlayersNames;

    private ButtonClick buttonClicked;

    private RecyclerView redTeamRecyclerView;
    private RecyclerView blueTeamRecyclerView;
    private MaterialTextView score;
    private MaterialTextView buttonMessage;
    private MaterialButton finishGameButton;
    private AppCompatImageButton addGoalButton;
    private AppCompatImageButton addAssistButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_results);
        setTitle("Set Match");

        redTeamRecyclerView = findViewById(R.id.red_team_recycler_view);
        blueTeamRecyclerView = findViewById(R.id.blue_team_recycler_view);;
        score = findViewById(R.id.game_score);
        buttonMessage = findViewById(R.id.button_message);
        finishGameButton = findViewById(R.id.finish_game_button);
        addGoalButton = findViewById(R.id.add_goal_button);
        addAssistButton = findViewById(R.id.add_assist_button);

        //region retrieve data
        setPlayers();
        updateGameScore();
        buttonMessage.setVisibility(View.INVISIBLE);
        buttonClicked = ButtonClick.NONE;

        setActionButtonsOnClickListener();
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
                                List<List<PlayerPerformance>> playersPerformance = new ArrayList<>();
                                playersPerformance.add(redPlayersPerformance);
                                playersPerformance.add(bluePlayersPerformance);
                                //endregion
                                //region sets data
                                Intent data = new Intent();
                                data.putExtra(EXTRA_PLAYERS_PERFORMANCE, (Serializable) playersPerformance);
                                //endregion
                                setResult(RESULT_OK, data);
                                finish();
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
        setRecyclerViews();
        setAdapters();
        //endregion
        //region sets adapters
        //endregion
    }

    private void setRecyclerView(Team team){
        RecyclerView recyclerView = team == Team.RED ? redTeamRecyclerView : blueTeamRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void setRecyclerViews(){
        setRecyclerView(Team.RED);
        setRecyclerView(Team.BLUE);
    }

    private void setAdapters(){
        redTeamRecyclerView.setAdapter(redTeamAdapter);
        blueTeamRecyclerView.setAdapter(blueTeamAdapter);
        redTeamAdapter.setPlayers(redPlayersPerformance, redPlayersNames);
        blueTeamAdapter.setPlayers(bluePlayersPerformance, bluePlayersNames);
    }

    private void setPlayers(Team team, Intent intent){
        List<Integer> players = (List<Integer>) intent.getSerializableExtra(
                team == Team.BLUE ? EXTRA_BLUE_PLAYERS: EXTRA_RED_PLAYERS);
        List<PlayerPerformance> playerPerformances = team == Team.BLUE ? bluePlayersPerformance : redPlayersPerformance;
        for(int playerID : players)
            playerPerformances.add(new PlayerPerformance(playerID));
        if(team == Team.BLUE)
            redPlayersNames = (List<String>)intent.getSerializableExtra(EXTRA_RED_PLAYERS_NAMES);
        else
            bluePlayersNames = (List<String>)intent.getSerializableExtra(EXTRA_BLUE_PLAYERS_NAMES);
        setAdapterOnItemClickListener(Team.BLUE);
        setAdapterOnItemClickListener(Team.RED);
    }

    private void setPlayers(){
        Intent intent = getIntent();
        setPlayers(Team.BLUE, intent);
        setPlayers(Team.RED, intent);
    }

    private void updateGameScore(){
        String gameScore = PlayerPerformance.getTeamGoals(redPlayersPerformance) + " - " + PlayerPerformance.getTeamGoals(bluePlayersPerformance);
        score.setText(gameScore);
    }

    private void setAdapterOnItemClickListener(Team team){
        final PlayerGameResultsAdapter adapter = team == Team.BLUE ? blueTeamAdapter : redTeamAdapter;
        final List<PlayerPerformance> playersPerformances = team == Team.BLUE ? bluePlayersPerformance : redPlayersPerformance;
        adapter.setOnItemClickListener(new PlayerGameResultsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlayerPerformance playerPerformance) {
                if (playersPerformances.contains(playerPerformance)) {
                    switch (buttonClicked) {
                        case GOAL:
                            playerPerformance.addGoal();
                            updateGameScore();
                            break;
                        case ASSIST:
                            playerPerformance.addAssist();
                            break;
                    }
                    adapter.notifyDataSetChanged();
                }
                buttonClicked = ButtonClick.NONE;
                buttonMessage.setText("");
                buttonMessage.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setActionButtonsOnClickListener(){
        setActionButtonOnClickListener(ButtonClick.GOAL);
        setActionButtonOnClickListener(ButtonClick.ASSIST);
    }

    private void setActionButtonOnClickListener(final ButtonClick buttonClick){
        AppCompatImageButton button = buttonClick == ButtonClick.GOAL ? addGoalButton : addAssistButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = buttonClick;
                switch (buttonClick){
                    case GOAL:
                        buttonMessage.setText(R.string.goal_button_Message);
                        break;
                    case ASSIST:
                        buttonMessage.setText(R.string.assist_button_Message);
                }
                buttonMessage.setVisibility(View.VISIBLE);
            }
        });
    }

}
