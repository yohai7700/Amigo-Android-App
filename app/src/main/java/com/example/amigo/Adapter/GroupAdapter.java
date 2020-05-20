package com.example.amigo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amigo.R;
import com.example.amigo.StatsViewModel.StatsRepository.Entity.Group;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends ListAdapter<Group,GroupAdapter.GroupHolder> {

    private OnItemLongClickListener longClickListener;
    private OnItemClickListener clickListener;
    private Context context;


    public GroupAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Group> DIFF_CALLBACK = new DiffUtil.ItemCallback<Group>() {
        @Override
        public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getCreationDate().equals(newItem.getCreationDate());
        }
    };

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        Group currentGroup = getItem(position);
        holder.textViewTitle.setText(currentGroup.getTitle());
        holder.textViewDescription.setText(currentGroup.getDescription());
        holder.textViewCreationDate.setText(DateFormat.getDateInstance().format(currentGroup.getCreationDate()));
        if (currentGroup.getIcon() != null)
            holder.circleImageViewIcon.setImageBitmap(currentGroup.getIcon());
        }

    public Group getGroupAt(int position){
        return getItem(position);
    }

    class GroupHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewCreationDate;
        private CircleImageView circleImageViewIcon;


        private GroupHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.group_title);
            textViewDescription = itemView.findViewById(R.id.group_description);
            textViewCreationDate = itemView.findViewById(R.id.group_creation_date);
            circleImageViewIcon = itemView.findViewById(R.id.group_icon);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                        longClickListener.onItemLongClick(getItem(position));
                        return true;
                    }
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (clickListener != null && position != RecyclerView.NO_POSITION) {

                        clickListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(Group group);
    }

    public void setOnLongClickListener(OnItemLongClickListener listener){
        this.longClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(Group group);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.clickListener = listener;
}
}