package com.example.amigo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amigo.Adapter.PlayerAdapter;
import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.ViewModel.PlayerViewModel;
import com.example.amigo.StatsViewModel.ViewModel.StandingsViewModel;
import com.example.amigo.Utility.PictureHandling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddStandingsActivity extends AppCompatActivity {
    public static final String EXTRA_GROUP_ID = "com.example.amigo.Activities.AddStandingsActivity.EXTRA_GROUP_ID";
    public static final int AMOUNT_OF_COLUMNS = 3, ADD_PLAYER_REQUEST = 1, DEFAULT_PLAYER_PHOTO = R.drawable.stock_profile;

    private PlayerViewModel playerViewModel;
    private StandingsViewModel standingsViewModel;
    private int groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_standings);
        //region sets title and retrieve groupID
        setTitle(R.string.add_player_title);
        Intent intent = getIntent();
        groupID = intent.getIntExtra(EXTRA_GROUP_ID, -1);
        //endregion
        //region sets FAB
        FloatingActionButton buttonAddPlayer = findViewById(R.id.add_player_button);
        buttonAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStandingsActivity.this, AddEditPlayerActivity.class);
                startActivityForResult(intent, ADD_PLAYER_REQUEST);
            }
        });
        //endregion
        //region sets RecyclerView
        RecyclerView playerListRecyclerView = findViewById(R.id.add_standings_player_list_recycler_view);
        playerListRecyclerView.setLayoutManager(new GridLayoutManager(this, AMOUNT_OF_COLUMNS));
        playerListRecyclerView.setHasFixedSize(true);
        //endregion
        //region sets PlayerAdapter
        final PlayerAdapter playerAdapter = new PlayerAdapter();
        playerListRecyclerView.setAdapter(playerAdapter);
        //endregion
        //region sets ViewModels
        standingsViewModel = new ViewModelProvider(this).get(StandingsViewModel.class);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        if (groupID == -1)
            playerViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
                @Override
                public void onChanged(List<Player> players) {
                    playerAdapter.submitList(players);
                }
            });
        else
            playerViewModel.getAllPlayersNotInGroup(groupID).observe(this, new Observer<List<Player>>() {
                @Override
                public void onChanged(List<Player> players) {
                    playerAdapter.submitList(players);
                }
            });
        //endregion
        //region sets ItemTouchHelper(swipe to delete)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                playerViewModel.delete(playerAdapter.getPlayerAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(playerListRecyclerView);
        playerAdapter.setOnClickListener(new PlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Player player) {
                standingsViewModel.insert(new Standings(groupID, player.id));
            }
        });
        //endregion
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PLAYER_REQUEST && resultCode == RESULT_OK) {
            //region inserts new player
            String fName = data.getStringExtra(AddEditPlayerActivity.EXTRA_PLAYER_FIRST_NAME);
            String lName = data.getStringExtra(AddEditPlayerActivity.EXTRA_PLAYER_LAST_NAME);
            Uri pictureUri = data.getParcelableExtra(AddEditPlayerActivity.EXTRA_PLAYER_PICTURE_URI);
            Bitmap pictureBM = PictureHandling.getCompressedBitmap(this, pictureUri, DEFAULT_PLAYER_PHOTO);
            Player newPlayer = new Player(fName, lName, pictureBM);
            playerViewModel.insert(newPlayer);
            //endregion
        } else
            Toast.makeText(getApplicationContext(), "Can't add player", Toast.LENGTH_SHORT).show();
    }
}
