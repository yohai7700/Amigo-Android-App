package com.example.amigo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amigo.R;
import com.example.amigo.StatsViewModel.PlayerPerformance;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerGameResultsAdapter extends RecyclerView.Adapter<PlayerGameResultsAdapter.PlayerGameResultsHolder> {

    private List<PlayerPerformance> playersPerformances = new ArrayList<>();
    private List<String> playersNames;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public PlayerGameResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_game_result_item, parent, false);
        return new PlayerGameResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerGameResultsHolder holder, int position) {
        PlayerPerformance currentPlayerPerformance = playersPerformances.get(position);
        String playerName = playersNames.get(position);
        holder.playerName.setText(playerName);
        holder.goals.setText(currentPlayerPerformance.getGoals() + "");
        holder.assists.setText(currentPlayerPerformance.getAssists() + "");

    }


    @Override
    public int getItemCount() {
        return playersPerformances.size();
    }

    class PlayerGameResultsHolder extends RecyclerView.ViewHolder{

        private TextView playerName;
        private TextView goals;
        private TextView assists;

        public PlayerGameResultsHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.result_player_name);
            goals = itemView.findViewById(R.id.result_goals_number);
            assists = itemView.findViewById(R.id.result_assists_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onItemClick(playersPerformances.get(position));
                }
            });
        }
    }

    public void setPlayers(List<PlayerPerformance> playersPerformances, List<String> playersNames) {
        this.playersPerformances = playersPerformances;
        this.playersNames = playersNames;
        notifyDataSetChanged();

    }

    public interface OnItemClickListener{
        void onItemClick(PlayerPerformance playerPerformance);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
