package com.example.amigo.Adapter;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Player;
import com.mikhaellopez.circularimageview.CircularImageView;

public class PlayerAdapter extends ListAdapter<Player, PlayerAdapter.PlayerHolder> {

    private OnItemClickListener clickListener;

    public PlayerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Player> DIFF_CALLBACK = new DiffUtil.ItemCallback<Player>() {
        @Override
        public boolean areItemsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return oldItem.getFirstName().equals(newItem.getFirstName())
                    && oldItem.getLastName().equals(newItem.getLastName());
        }
    };

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);
        return new PlayerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
        Player currentPlayer = getItem(position);
        String name = currentPlayer.getFirstName() + " " + currentPlayer.getLastName();
        holder.textViewName.setText(name);
        holder.imgPicture.setImageDrawable(new BitmapDrawable(Resources.getSystem(), currentPlayer.getPicture()));
    }


    public Player getPlayerAt(int position){
        return getItem(position);
    }

    class PlayerHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private CircularImageView imgPicture;


        public PlayerHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.player_name);
            imgPicture = itemView.findViewById(R.id.player_picture);

            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                        longClickListener.onItemLongClick(groups.get(position));
                        return true;
                    }
                    return false;
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (clickListener != null && position != RecyclerView.NO_POSITION)
                        clickListener.onItemClick(getItem(position));
                }
            });
        }
    }

    /*public interface OnItemLongClickListener{
        void onItemLongClick(Group group);
    }

    public void setOnLongClickListener(OnItemLongClickListener listener){
        this.longClickListener = listener;
    }*/

    public interface OnItemClickListener{
        void onItemClick(Player player);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.clickListener = listener;
    }
}