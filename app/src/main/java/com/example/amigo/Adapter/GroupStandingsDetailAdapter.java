package com.example.amigo.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.InterClass.StandingsDetail;

public class GroupStandingsDetailAdapter extends ListAdapter<StandingsDetail, GroupStandingsDetailAdapter.StandingsHolder> {

    private Context context;
    private OnItemLongClickListener onItemLongClickListener;

    public GroupStandingsDetailAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<StandingsDetail>  DIFF_CALLBACK = new DiffUtil.ItemCallback<StandingsDetail>() {
        @Override
        public boolean areItemsTheSame(@NonNull StandingsDetail oldItem, @NonNull StandingsDetail newItem) {
            return oldItem.player.id == newItem.player.id
                    && oldItem.group.id == newItem.group.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull StandingsDetail oldItem, @NonNull StandingsDetail newItem) {
            return oldItem.player.getFirstName().equals(newItem.player.getFirstName())
                    && oldItem.player.getLastName().equals(newItem.player.getLastName())
                    && oldItem.group.getTitle().equals(newItem.group.getTitle())
                    && oldItem.group.getDescription().equals(newItem.group.getDescription())
                    && oldItem.group.getCreationDate().equals(newItem.group.getCreationDate())
                    && oldItem.standings.getGames() == newItem.standings.getGames()
                    && oldItem.standings.getWins() == newItem.standings.getWins()
                    && oldItem.standings.getLosses() == newItem.standings.getLosses()
                    && oldItem.standings.getGoals() == newItem.standings.getGoals()
                    && oldItem.standings.getAssists() == newItem.standings.getAssists()
                    && oldItem.standings.getRating() == newItem.standings.getRating();
        }
    };

    //todo: fix wrong standings color when changed
    @NonNull
    @Override
    public StandingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standings_item, parent, false);
        return new StandingsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StandingsHolder holder, int position) {
        StandingsDetail currentStandingsDetail = getItem(position);
        String playerName = (position+1) + " " + currentStandingsDetail.player.getFirstName() + " " + currentStandingsDetail.player.getLastName();
        holder.name.setText(playerName);
        holder.rating.setText(String.valueOf(currentStandingsDetail.standings.getRating()));
        holder.games.setText(String.valueOf(currentStandingsDetail.standings.getGames()));
        holder.wins.setText(String.valueOf(currentStandingsDetail.standings.getWins()));
        holder.losses.setText(String.valueOf(currentStandingsDetail.standings.getLosses()));
        holder.goals.setText(String.valueOf(currentStandingsDetail.standings.getGoals()));
        holder.assists.setText(String.valueOf(currentStandingsDetail.standings.getAssists()));
        if(position % 2 == 0)
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_overlay)));
        else
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dark_overlay)));

    }


    public void setContext(Context context){
        this.context = context;
    }

    class StandingsHolder extends RecyclerView.ViewHolder{
        //region declare field instances(name, rating, games wins, losses, goals, assists)
        private TextView name;
        private TextView rating;
        private TextView games;
        private TextView wins;
        private TextView losses;
        private TextView goals;
        private TextView assists;
        //endregion
        //region define field instances
        public StandingsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.player_name);
            games = itemView.findViewById(R.id.games_number);
            wins = itemView.findViewById(R.id.wins_number);
            losses = itemView.findViewById(R.id.losses_number);
            goals = itemView.findViewById(R.id.goals_number);
            assists = itemView.findViewById(R.id.assists_number);
            rating = itemView.findViewById(R.id.rating_number);

            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        v.setBackgroundColor(context.getColor(R.color.standingsRowEdited));
                        onItemLongClickListener.onItemLongClick(standingsDetail.get(position));
                        return true;
                    }
                    return false;
                }
            });*/
        }
        //endregion
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(StandingsDetail standingsDetail);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
