package com.example.amigo.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Standings;
import com.example.amigo.StatsViewModel.ViewModel.PlayerViewModel;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerAssigningAdapter extends RecyclerView.Adapter<PlayerAssigningAdapter.PlayerAssigningHolder> {
    public static String RED_TEAM = "com.example.amigo.Adapter.PlayerAssigningAdapter.RED_TEAM"; //TODO: change to ENUM
    public static String BLUE_TEAM = "com.example.amigo.Adapter.PlayerAssigningAdapter.BLUE_TEAM";
    public static String NONE_TEAM = "com.example.amigo.Adapter.PlayerAssigningAdapter.NON_TEAM";

    private List<Standings> players = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private String currentTeam;
    private Context context;
    private OnItemCheckListener onItemCheckListener;
    private PlayerViewModel playerViewModel;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPlayerViewModel(PlayerViewModel playerViewModel){
        this.playerViewModel = playerViewModel;
    }

    public String getCurrentTeam() {
        return currentTeam;
    }

    public OnItemCheckListener getOnItemCheckListener() {
        return onItemCheckListener;
    }

    @NonNull
    @Override
    public PlayerAssigningHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_assigning_item, parent, false);
        return new PlayerAssigningHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAssigningHolder holder, int position) {
        Standings currentPlayer = players.get(position);
        String playerName = playerNames.get(players.indexOf(currentPlayer));
        int ratingNum = currentPlayer.getRating();
        String rating = String.valueOf(ratingNum);
        holder.playerRating.setText(rating);
        holder.playerAssignCheckBox.setText(playerName);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setPlayers(List<Standings> players, List<String> playerNames){
        this.players = players;
        this.playerNames = playerNames;
        notifyDataSetChanged();
    }

    class PlayerAssigningHolder extends  RecyclerView.ViewHolder{

        private MaterialCheckBox playerAssignCheckBox;
        private MaterialTextView playerRating;

        private PlayerAssigningHolder(@NonNull View itemView) {
            super(itemView);
            playerRating = itemView.findViewById(R.id.rating_number_assigning);
            playerAssignCheckBox = itemView.findViewById(R.id.player_assigned_check_box);
            playerAssignCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    Standings currentPlayer = players.get(position);
                    if (position != RecyclerView.NO_POSITION){
                        changeCheckBox(currentPlayer, playerNames.get(position), isChecked);
                    }
                }
            });
        }

        private void changeCheckBox(Standings currentPlayer, String name, boolean isChecked){
            if(isChecked){ //changing from non-checked box to a team-checked box
                onItemCheckListener.onItemCheck(currentPlayer.getPlayerID(), name, currentTeam);
                playerAssignCheckBox.setButtonTintList(getTeamColorStateList());
            }
            else if(playerAssignCheckBox.getButtonTintList().equals(getTeamColorStateList())){//switching off a team-checked box
                onItemCheckListener.onItemUncheck(currentPlayer.getPlayerID(), currentTeam);
                playerAssignCheckBox.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.checkBoxDefaultColor)));
            }
            else{
                onItemCheckListener.onItemUncheck(currentPlayer.getPlayerID(), currentTeam.equals(RED_TEAM)?BLUE_TEAM:RED_TEAM); //changing from one team to another
                onItemCheckListener.onItemCheck(currentPlayer.getPlayerID(), name, currentTeam);
                playerAssignCheckBox.setButtonTintList(getTeamColorStateList());
                playerAssignCheckBox.setChecked(true);
            }
        }
    }



    private ColorStateList getTeamColorStateList(){
        if(currentTeam == null)
            return null;
        if(currentTeam.equals(RED_TEAM))
            return ColorStateList.valueOf(ContextCompat.getColor(context, R.color.redTeamColor));
        if(currentTeam.equals(BLUE_TEAM))
            return ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blueTeamColor));
        return null;
    }

    public void setCurrentTeam(String currentTeam){
        this.currentTeam = currentTeam;
    }

    public interface OnItemCheckListener{
        void onItemCheck(int playerID, String name, String currentTeam);
        void onItemUncheck(int playerID, String currentTeam);
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener){
        this.onItemCheckListener = onItemCheckListener;
    }

}
