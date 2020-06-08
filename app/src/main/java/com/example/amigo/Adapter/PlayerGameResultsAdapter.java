package com.example.amigo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerGameResultsAdapter extends RecyclerView.Adapter<PlayerGameResultsAdapter.PlayerGameResultsHolder> {

    private List<Player> players = new ArrayList<>();
    private List<Integer> goals;
    private List<Integer> assists;
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
        Player currentPlayer = players.get(position);
        String playerName = currentPlayer.getName();
        holder.playerName.setText(playerName);
        holder.goals.setText(String.valueOf(goals.get(position)));
        holder.assists.setText(String.valueOf(assists.get(position)));

    }


    @Override
    public int getItemCount() {
        return players.size();
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
                    onItemClickListener.onItemClick(players.get(position));
                }
            });
        }
    }

    public void setStats(List<Player> players, List<Integer> goals, List<Integer> assists) {
        this.players = players;
        this.assists = assists;
        this.goals = goals;
        notifyDataSetChanged();

    }

    public interface OnItemClickListener{
        void onItemClick(Player player);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
